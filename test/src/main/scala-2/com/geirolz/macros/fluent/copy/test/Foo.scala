package com.geirolz.macros.fluent.copy.test

import com.geirolz.macros.fluent.copy.FluentCopy

@FluentCopy(copyWith = true, update = true, collection = true)
case class Foo(
  value: Int,
  option: Option[Double],
  list: List[String],
  set: Set[String],
  seq: Seq[String]
)
object Foo {
  val default: Foo = Foo(
    value  = 1,
    option = None,
    list   = Nil,
    set    = Set.empty,
    seq    = Seq.empty
  )
}
