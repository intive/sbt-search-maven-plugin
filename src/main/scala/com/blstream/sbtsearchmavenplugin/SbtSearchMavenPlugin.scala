package com.blstream.sbtsearchmavenplugin

import sbt._
import sbt.Keys.{ streams, aggregate }

object SbtSearchMavenPlugin extends AutoPlugin with SearchWithDependencies with SearchAndAddDependency {

  override def trigger = allRequirements

  object autoImport {
    lazy val searchMaven = InputKey[Unit]("searchMaven", "Search maven")
    lazy val addDependencyMaven = InputKey[Unit]("addDependencyMaven", "Add a dependency from maven")
  }

  import autoImport._

  override lazy val projectSettings = Seq(
    searchMaven := search(complete.DefaultParsers.spaceDelimited("<arg>").parsed, streams.value.log),
    addDependencyMaven := add(complete.DefaultParsers.spaceDelimited("<arg>").parsed, streams.value.log),
    aggregate in searchMaven := false
  )

}

trait SearchWithDependencies
  extends Search
  with MavenOrgSearcher
  with QueryCleaner
  with ResultsParser
  with ArtifactsPrinter

trait SearchAndAddDependency
  extends AddDependency
  with SearchWithDependencies
  with ArtifactString
  with sbt.PathExtra
  with DependencyReaderWriter

