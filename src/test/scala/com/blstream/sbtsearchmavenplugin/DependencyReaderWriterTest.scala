package com.blstream.sbtsearchmavenplugin

import org.specs2.mutable.Specification
import org.specs2.specification.{ AfterEach, BeforeEach }

class DependencyReaderWriterTest
    extends Specification
    with BeforeEach with AfterEach
    with DependencyReaderWriterTraits {

  val artifacts = Seq(
    Artifact("org.scalaz", "z", "1"),
    Artifact("foo", "bar", "baz")
  )

  sequential

  "writeArtifactsFile" should {
    "properly write a single artifact to an non existing artifact file" >> {
      val artifact = artifacts(0)
      writeArtifactsFile(artifact)
      val text = getFileContents
      val expectedResult =
        s"""|libraryDependencies += "org.scalaz" % "z" % "1"""".stripMargin

      text must beEqualTo(expectedResult)
    }
  }

  "writeArtifactsFile" should {
    "be able to add artifacts to an existing file" >> {
      artifacts.map(writeArtifactsFile)
      val text = getFileContents
      val expectedResult =
        s"""|libraryDependencies += "org.scalaz" % "z" % "1"
            |libraryDependencies += "foo" % "bar" % "baz"""".stripMargin

      text must beEqualTo(expectedResult)
    }
  }

  "writeArtifactsFile" should {
    "not add duplicates in a file" >> {
      artifacts.map(writeArtifactsFile)
      writeArtifactsFile(artifacts.head)
      val text = getFileContents
      val expectedResult =
        s"""|libraryDependencies += "org.scalaz" % "z" % "1"
           |libraryDependencies += "foo" % "bar" % "baz"""".stripMargin

      text must beEqualTo(expectedResult)
    }
  }

  "readArtifactsFile" should {
    "work even with a non existent file" >> {
      val artifacts = readArtifactsFile
      artifacts must beNone
    }
  }

  "readArtifactsFile" should {
    "work even with an empty file" >> {
      createEmptyFile
      val artifacts = readArtifactsFile
      artifacts must beNone
    }
  }

  "readArtifactsFile" should {
    "work with a corrupted file" >> {
      val corruptedFileContents =
        s"""+= "org.scalaz" % "z" % "1"
           |libraryDependencies += "foo" % "bar" % "baz"
           |libraryDependencies += "foo" %  % "buz"
           |"baz"""".stripMargin
      createFileWithContents(corruptedFileContents)
      val artifacts = readArtifactsFile
      println(artifacts)
      artifacts mustEqual Some(Seq(Artifact("foo", "bar", "baz")))
    }
  }
}

trait DependencyReaderWriterTraits
  extends DependencyReaderWriter
  with ArtifactString
  with sbt.PathExtra
  with ArtifactFileHelper
