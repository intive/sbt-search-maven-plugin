package com.blstream.sbtsearchmavenplugin

import org.specs2.mutable.Specification
import org.specs2.specification.{ AfterEach, BeforeEach }

class AddDependencyTest extends Specification with BeforeEach with AfterEach with AddDependencyTraits {
  sequential

  val chooseSequence = Seq(1, 2, 1)

  val artifacts = List(Artifact("org.scalaz", "z", "1"), Artifact("foo", "bar", "baz"))

  "chooseArtifact" should {
    "allow a user to pick the chosen artifact out of a list of artifacts" >> {
      val chosenArtifacts = chooseSequence map (i => chooseArtifact(artifacts, choose(i)))
      val expectedArtifacts = chooseSequence map (i => Right(artifacts(i - 1)))
      expectedArtifacts must beEqualTo(chosenArtifacts)
    }
  }

  def choose(index: Integer): Option[String => Option[String]] = {
    def chooseIndex(str: String): Option[String] = {
      Some(s"""$index""")
    }
    Some(chooseIndex)
  }
}

trait AddDependencyTraits
  extends AddDependency
  with Search
  with MavenOrgSearcher
  with QueryCleaner
  with ResultsParser
  with ArtifactsPrinter
  with DependencyReaderWriter
  with ArtifactString
  with sbt.PathExtra
  with ArtifactFileHelper

