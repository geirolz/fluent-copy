package com.geirolz.macros.fluent.copy.test

import com.geirolz.macros.fluent.copy.FluentCopy

@FluentCopy(copy = true, update = true)
case class Foo(a: Int, b: List[String], c: Option[Double])
