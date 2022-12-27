package com.geirolz.macros.fluent.copy

import com.geirolz.macros.fluent.copy.test.Foo

class FluentCopySuite extends munit.FunSuite {

  test("Fluent copy") {

    val foo: Foo = Foo(
      a = 1,
      b = List("a", "b"),
      c = None
    )

    assertEquals(
      foo
        .withA(10)
        .withB(List("a", "b", "c"))
        .withC(Some(1.99)),
      Foo(
        a = 10,
        b = List("a", "b", "c"),
        c = Some(1.99)
      )
    )
  }

  test("Fluent update") {

    val foo: Foo = Foo(
      a = 1,
      b = List("a", "b"),
      c = None
    )

    assertEquals(
      foo
        .updateA(_ + 9)
        .updateB(_ :+ "c")
        .updateC(_ => Some(1.99)),
      Foo(
        a = 10,
        b = List("a", "b", "c"),
        c = Some(1.99)
      )
    )
  }
}
