package com.geirolz.macros.fluent.copy

import com.geirolz.macros.fluent.copy.test.Foo

import scala.collection.immutable.{Seq, Set}

class FluentCopySuite extends munit.FunSuite {

  test("Macro preserves existent companion object") {
    assertEquals(
      obtained = Foo.default,
      expected = Foo(
        value  = 1,
        option = None,
        list   = Nil,
        set    = Set.empty,
        seq    = Seq.empty
      )
    )
  }

  test("Fluent copy") {

    val foo: Foo = Foo(
      value  = 1,
      option = None,
      list   = List("a", "b"),
      set    = Set.empty,
      seq    = Seq.empty
    )

    assertEquals(
      foo
        .withValue(10)
        .withOption(Some(1.99))
        .withList(List("a", "b", "c")),
      Foo(
        value  = 10,
        option = Some(1.99),
        list   = List("a", "b", "c"),
        set    = Set.empty,
        seq    = Seq.empty
      )
    )
  }

  test("Fluent update") {

    val foo: Foo = Foo(
      value  = 1,
      option = None,
      list   = List("a", "b"),
      set    = Set.empty,
      seq    = Seq.empty
    )

    assertEquals(
      foo
        .updateValue(_ + 9)
        .updateOption(_ => Some(1.99))
        .updateList(_ :+ "c"),
      Foo(
        value  = 10,
        option = Some(1.99),
        list   = List("a", "b", "c"),
        set    = Set.empty,
        seq    = Seq.empty
      )
    )
  }

  test("Fluent copy with Option") {

    val fooNone: Foo = Foo(
      value  = 1,
      option = None,
      list   = Nil,
      set    = Set.empty,
      seq    = Seq.empty
    )

    val fooSome: Foo = Foo(
      value  = 1,
      option = Some(1),
      list   = Nil,
      set    = Set.empty,
      seq    = Seq.empty
    )

    assertEquals(
      fooNone.withSomeOption(1.99),
      Foo(
        value  = 1,
        option = Some(1.99),
        list   = Nil,
        set    = Set.empty,
        seq    = Seq.empty
      )
    )

    assertEquals(
      fooSome.withNoneOption,
      Foo(
        value  = 1,
        option = None,
        list   = Nil,
        set    = Set.empty,
        seq    = Seq.empty
      )
    )
  }

  test("Fluent copy with Seq") {

    val fooNil: Foo = Foo(
      value  = 1,
      option = None,
      list   = Nil,
      set    = Set.empty,
      seq    = Seq.empty
    )

    val fooFull: Foo = Foo(
      value  = 1,
      option = None,
      list   = Nil,
      set    = Set.empty,
      seq    = Seq("a")
    )

    assertEquals(
      fooNil.withOneSeq("a"),
      Foo(
        value  = 1,
        option = None,
        list   = Nil,
        set    = Set.empty,
        seq    = Seq("a")
      )
    )

    assertEquals(
      fooFull.withEmptySeq,
      Foo(
        value  = 1,
        option = None,
        list   = Nil,
        set    = Set.empty,
        seq    = Seq.empty
      )
    )
  }

  test("Fluent copy with List") {

    val fooNil: Foo = Foo(
      value  = 1,
      list   = Nil,
      option = None,
      set    = Set.empty,
      seq    = Seq.empty
    )

    val fooFull: Foo = Foo(
      value  = 1,
      list   = List("a"),
      option = None,
      set    = Set.empty,
      seq    = Seq.empty
    )

    assertEquals(
      fooNil.withOneList("a"),
      Foo(
        value  = 1,
        option = None,
        list   = List("a"),
        set    = Set.empty,
        seq    = Seq.empty
      )
    )

    assertEquals(
      fooFull.withEmptyList,
      Foo(
        value  = 1,
        option = None,
        list   = Nil,
        set    = Set.empty,
        seq    = Seq.empty
      )
    )
  }

  test("Fluent copy with Set") {

    val fooNil: Foo = Foo(
      value  = 1,
      option = None,
      list   = Nil,
      set    = Set("a"),
      seq    = Seq.empty
    )

    val fooFull: Foo = Foo(
      value  = 1,
      option = None,
      list   = Nil,
      set    = Set.empty,
      seq    = Seq.empty
    )

    assertEquals(
      fooNil.withOneSet("a"),
      Foo(
        value  = 1,
        option = None,
        list   = Nil,
        set    = Set("a"),
        seq    = Seq.empty
      )
    )

    assertEquals(
      fooFull.withEmptySet,
      Foo(
        value  = 1,
        option = None,
        list   = Nil,
        set    = Set.empty,
        seq    = Seq.empty
      )
    )
  }

}
