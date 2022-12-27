# Fluent copy
[![Build Status](https://github.com/geirolz/@PRJ_NAME@/actions/workflows/cicd.yml/badge.svg)](https://github.com/geirolz/@PRJ_NAME@/actions)
[![codecov](https://img.shields.io/codecov/c/github/geirolz/@PRJ_NAME@)](https://codecov.io/gh/geirolz/@PRJ_NAME@)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/3101ec45f0114ad0abde91181c8c238c)](https://www.codacy.com/gh/geirolz/@PRJ_NAME@/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=geirolz/@PRJ_NAME@&amp;utm_campaign=Badge_Grade)
[![Sonatype Nexus (Releases)](https://img.shields.io/nexus/r/com.github.geirolz/@PRJ_NAME@-core_2.13?server=https%3A%2F%2Foss.sonatype.org)](https://mvnrepository.com/artifact/com.github.geirolz/@PRJ_NAME@-core)
[![javadoc.io](https://javadoc.io/badge2/com.github.geirolz/@PRJ_NAME@-core_2.13/javadoc.io.svg)](https://javadoc.io/doc/com.github.geirolz/@PRJ_NAME@-core_2.13)
[![Scala Steward badge](https://img.shields.io/badge/Scala_Steward-helping-blue.svg?style=flat&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAQCAMAAAARSr4IAAAAVFBMVEUAAACHjojlOy5NWlrKzcYRKjGFjIbp293YycuLa3pYY2LSqql4f3pCUFTgSjNodYRmcXUsPD/NTTbjRS+2jomhgnzNc223cGvZS0HaSD0XLjbaSjElhIr+AAAAAXRSTlMAQObYZgAAAHlJREFUCNdNyosOwyAIhWHAQS1Vt7a77/3fcxxdmv0xwmckutAR1nkm4ggbyEcg/wWmlGLDAA3oL50xi6fk5ffZ3E2E3QfZDCcCN2YtbEWZt+Drc6u6rlqv7Uk0LdKqqr5rk2UCRXOk0vmQKGfc94nOJyQjouF9H/wCc9gECEYfONoAAAAASUVORK5CYII=)](https://scala-steward.org)
[![GitHub license](https://img.shields.io/github/license/geirolz/@PRJ_NAME@)](https://github.com/geirolz/@PRJ_NAME@/blob/master/LICENSE)

Scala macros to have fluent copy on case class 

```sbt
libraryDependencies += "com.github.geirolz" %% "fluent-copy" % "@VERSION@"
```

- `copyWith` when `true` adds a method `with$FIELD_NAME(newvalue: $FIELD_TYPE): $CASE_CLASS`.
- `update` when `true` adds a method `update$FIELD_NAME(f: $FIELD_TYPE => $FIELD_TYPE): $CASE_CLASS`.
- `collection` when `true` adds methods:
    - For `Option`
        - `withSome$FIELD_NAME(f: $FIELD_COLLECTION_TYPE): $CASE_CLASS`
        - `withNone$FIELD_NAME: $CASE_CLASS`
    - For collections `Seq`, `List`, `Set`
        - `withOne$FIELD_NAME(f: $FIELD_COLLECTION_TYPE): $CASE_CLASS`
        - `withEmpty$FIELD_NAME: $CASE_CLASS`

```scala mdoc
import com.geirolz.macros.fluent.copy.FluentCopy

@FluentCopy(copyWith = true, update = true, collection = true)
case class Foo(
  value: Int,
  option: Option[Double],
  list: List[String],
  set: Set[String],
  seq: Seq[String]
)
```

Generates

```scala mdoc
implicit class FooFluentConfigOps(i: Foo) {
  def withValue(value: Int): Foo = ???

  def updateValue(f: Int => Int): Foo = ???

  def withOption(option: Option[Double]): Foo = ???

  def updateOption(f: Option[Double] => Option[Double]): Foo = ???

  def withSomeOption(option: Double): Foo = ???

  def withNoneOption: Foo = ???

  def withList(list: List[String]): Foo = ???

  def updateList(f: List[String] => List[String]): Foo = ???

  def withOneList(list: String): Foo = ???

  def withEmptyList: Foo = ???

  def withSet(set: Set[String]): Foo = ???

  def updateSet(f: Set[String] => Set[String]): Foo = ???

  def withOneSet(set: String): Foo = ???

  def withEmptySet: Foo = ???

  def withSeq(seq: Seq[String]): Foo = ???

  def updateSeq(f: Seq[String] => Seq[String]): Foo = ???

  def withOneSeq(seq: String): Foo = ???

  def withEmptySeq: Foo = ???
}
```
