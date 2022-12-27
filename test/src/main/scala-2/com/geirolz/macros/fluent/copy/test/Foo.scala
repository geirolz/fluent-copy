package com.geirolz.macros.fluent.copy.test

import com.geirolz.macros.fluent.copy.FluentCopy

@FluentCopy(copy = true, update = true, collection = true)
case class Foo(
  value: Int,
  option: Option[Double],
  list: List[String],
  set: Set[String],
  seq: Seq[String]
)
