import sbt.{CrossVersion, _}

/** cats-xml Created by geirolz on 30/07/2019.
  *
  * @author
  *   geirolz
  */
object ProjectDependencies {

  lazy val common: Seq[ModuleID] = Seq(
    // TEST
    "org.scalameta"  %% "munit"            % "0.7.29" % Test,
    "org.scalameta"  %% "munit-scalacheck" % "0.7.29" % Test,
    "org.typelevel"  %% "cats-laws"        % "2.9.0"  % Test,
    "org.typelevel"  %% "discipline-munit" % "1.0.9"  % Test,
    "org.scalacheck" %% "scalacheck"       % "1.17.0" % Test
  )

  object Core {
    val dedicated: Seq[ModuleID] = Seq(
      "org.scala-lang" % "scala-reflect" % "2.13.10"
    )
  }

  object Docs {
    val dedicated: Seq[ModuleID] = Nil
  }

  object Plugins {
    lazy val compilerPluginsFor2_13: Seq[ModuleID] = Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.13.2" cross CrossVersion.full)
    )
    lazy val compilerPluginsFor3: Seq[ModuleID] = Nil
  }
}
