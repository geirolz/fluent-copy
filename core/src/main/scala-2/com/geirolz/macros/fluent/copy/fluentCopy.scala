package com.geirolz.macros.fluent.copy

import scala.annotation.{compileTimeOnly, unused, StaticAnnotation}
import scala.reflect.api.Trees
import scala.reflect.macros.whitebox

@compileTimeOnly("enable macro paradise")
class FluentCopy(
  @unused copyWith: Boolean   = FluentCopy.defaultCopyWithStatus,
  @unused update: Boolean     = FluentCopy.defaultUpdateStatus,
  @unused collection: Boolean = FluentCopy.defaultCollectionStatus
) extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro FluentCopyMacros.impl
}
object FluentCopy {
  val defaultCopyWithStatus: Boolean   = true
  val defaultUpdateStatus: Boolean     = true
  val defaultCollectionStatus: Boolean = false
}

object FluentCopyMacros {
  def impl(c: whitebox.Context)(annottees: c.Tree*): c.Expr[Any] = {
    import c.universe.*

    val (isCopyWithEnabled, isUpdateEnabled, isAdvCollectionEnabled) =
      c.prefix.tree match {
        case Apply(Select(New(Ident(TypeName("FluentCopy"))), termNames.CONSTRUCTOR), args) =>
          val namedArgs = args.collect {
            case NamedArg(Ident(TermName(name)), Literal(Constant(value))) =>
              (name, value)
          }.toMap

          (
            namedArgs
              .getOrElse("copyWith", FluentCopy.defaultCopyWithStatus)
              .asInstanceOf[Boolean],
            namedArgs
              .getOrElse("update", FluentCopy.defaultUpdateStatus)
              .asInstanceOf[Boolean],
            namedArgs
              .getOrElse("collection", FluentCopy.defaultCollectionStatus)
              .asInstanceOf[Boolean]
          )
        case _ =>
          c.abort(c.enclosingPosition, "unexpected annotation pattern, use named arguments!")
      }

    def extractClassNameAndFields(classDecl: ClassDef): (c.TypeName, List[Trees#Tree]) =
      try {
        val q"case class $className(..$fields) extends ..$_ { ..$_ }" = classDecl
        (className, fields.toList)
      } catch {
        case _: MatchError =>
          c.abort(c.enclosingPosition, "Annotation is only supported on case class")
      }

    def modifyCompanion(
      compDeclOpt: Option[ModuleDef],
      toAdd: c.Tree,
      className: TypeName
    ): c.universe.Tree = {
      compDeclOpt map { compDecl =>
        val q"object $obj extends ..$bases { ..$body }" = compDecl
        q"""
          object $obj extends ..$bases {
            ..$body
            $toAdd
          }
        """
      } getOrElse {
        // Create a companion object with the formatter
        q"object ${className.toTermName} { $toAdd }"
      }
    }

    def implicitFluentCopyOps(className: c.TypeName, fields: List[Trees#Tree]): c.Tree = {

      val newMethods: Seq[c.universe.Tree] = fields.flatMap { case q"$_ val $tname: $tpt = $_" =>
        val normName = tname.toString().capitalize
        val base = Seq(
          Option.when(isCopyWithEnabled)(
            q"def ${TermName(s"with$normName")}($tname: $tpt): $className = i.copy($tname = $tname)"
          ),
          Option.when(isUpdateEnabled)(
            q"def ${TermName(s"update$normName")}(f: $tpt => $tpt): $className = i.copy($tname = f(i.$tname))"
          )
        ).flatten

        val advanced: Seq[c.universe.Tree] = if (isAdvCollectionEnabled) {
          tpt match {
            case AppliedTypeTree(Ident(TypeName("Option")), List(optType)) =>
              Seq(
                q"def ${TermName(s"withSome$normName")}($tname: $optType): $className = i.copy($tname = Some($tname))",
                q"def ${TermName(s"withNone$normName")}: $className = i.copy($tname = None)"
              )
            case AppliedTypeTree(
                  Ident(TypeName(fname @ ("Seq" | "List" | "Set"))),
                  List(seqType)
                ) =>
              Seq(
                q"def ${TermName(s"withOne$normName")}($tname: $seqType): $className = i.copy($tname = ${TermName(fname)}($tname))",
                q"def ${TermName(s"withEmpty$normName")}: $className = i.copy($tname = ${TermName(fname)}.empty)"
              )
            case _ => Nil
          }
        } else Seq.empty[c.universe.Tree]

        base ++ advanced
      }

      val opsName: c.universe.TypeName = TypeName(s"${className.toTermName}FluentConfigOps")
      q"""  
          implicit class $opsName(i: $className){
            ..$newMethods
          }
         """
    }

    def addFluentCaseClassOps(
      classDecl: ClassDef,
      compDeclOpt: Option[ModuleDef] = None
    ): c.Expr[Any] = {
      val (className, fields)     = extractClassNameAndFields(classDecl)
      val fluentCopyOps: c.Tree   = implicitFluentCopyOps(className, fields)
      val newCompanionObj: c.Tree = modifyCompanion(compDeclOpt, fluentCopyOps, className)

      // Return both the class and companion object declarations
      c.Expr(q"""
        $classDecl
        $newCompanionObj
      """)
    }

    annottees match {
      case (classDecl: ClassDef) :: Nil => addFluentCaseClassOps(classDecl)
      case (classDecl: ClassDef) :: (compDecl: ModuleDef) :: Nil =>
        addFluentCaseClassOps(classDecl, Some(compDecl))
      case _ => c.abort(c.enclosingPosition, "Invalid annottee")
    }
  }
}
