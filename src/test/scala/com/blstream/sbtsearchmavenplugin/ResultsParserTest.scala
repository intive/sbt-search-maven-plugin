package com.blstream.sbtsearchmavenplugin

import org.specs2.mutable.Specification

class ResultsParserTest extends Specification
    with ResultsParser {

  "parseResults" >> {
    "extract artifacts from json" >> {
      val scalazSearch = scala.io.Source.fromURL(getClass.getResource("/scalaz.json")).mkString
      val artifacts = List(
        Artifact("com.workingmouse", "scalaz", "3.0"),
        Artifact("org.scalaz", "scalaz_2.9.3", "7.1.6"),
        Artifact("org.scalaz", "scalaz_2.11", "7.2.0"))
      val parsed = parseResults(scalazSearch)
      parsed must beRight
      parsed.right.get must containTheSameElementsAs(artifacts)
    }

    "extract suggestions if any" >> {
      val shaplessSearch = scala.io.Source.fromURL(getClass.getResource("/shapless-misspelled.json")).mkString
      val suggestions = "Artifact not found, did you mean: shapeless, shapes, staples, seamless, stateless?"
      val parsed = parseResults(shaplessSearch)
      parsed must beLeft
      parsed.left.get must beEqualTo(suggestions)
    }

    "print nothing is found" >> {
      val shaplessSearch = scala.io.Source.fromURL(getClass.getResource("/abcdefg.json")).mkString
      val notFound = "Artifact not found"
      val parsed = parseResults(shaplessSearch)
      parsed must beLeft
      parsed.left.get must beEqualTo(notFound)
    }

  }
}
