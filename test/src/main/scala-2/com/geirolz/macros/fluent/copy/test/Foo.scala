package com.geirolz.macros.fluent.copy.test

import com.geirolz.macros.fluent.copy.FluentCopyMacros.FluentCaseClass

@FluentCaseClass
case class Foo(a: Int, b: List[String], c: Option[Double])
