package com.blstream.sbtsearchmavenplugin

import org.specs2.mutable.Specification

class ArtifactsPrinterTest extends Specification
    with ArtifactsPrinter {

  "printArtifacts" should {
    "properly format artifacts output" >> {
      val artifacts = List(
        Artifact("org.scalaz", "z", "1"),
        Artifact("foo", "bar", "baz"),
        Artifact("some", "other", "2")
      )
      val text = printArtifacts("foo")(artifacts)
      val expectedResult =
        s"""Results for foo:
           |[0] "org.scalaz" % "z"     % "1"
           |[1] "foo"        % "bar"   % "baz"
           |[2] "some"       % "other" % "2"""".stripMargin

      text must beEqualTo(expectedResult)
    }

  }
}
