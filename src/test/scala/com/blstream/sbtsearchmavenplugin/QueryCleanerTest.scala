package com.blstream.sbtsearchmavenplugin

import org.specs2.mutable.Specification

class QueryCleanerTest extends Specification
    with QueryCleaner {

  "cleanQuery" should {

    "remove all chars except a-zA-Z0-9-" >> {
      cleanQuery("^a&b%c*") must beRight("abc")
      cleanQuery("_1-23*456 ") must beRight("1-23456")
      cleanQuery("#$%^&*()") must beLeft
      cleanQuery("") must beLeft
    }

    "leave correct query untouched" >> {
      cleanQuery("AbC16-") must beRight("AbC16-")
    }

  }
}
