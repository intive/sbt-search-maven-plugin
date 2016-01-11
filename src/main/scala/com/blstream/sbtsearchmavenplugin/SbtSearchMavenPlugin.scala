package com.blstream.sbtsearchmavenplugin

import sbt._
import sbt.Keys.streams

object SbtSearchMavenPlugin extends AutoPlugin with SearchWithDependencies {

  object autoImport {
    lazy val searchMaven = InputKey[Unit]("searchMaven", "Search maven")
  }

  import autoImport._

  override lazy val projectSettings = Seq(
    //TODO: maybe custom parser is a better idea here
    searchMaven := search(complete.DefaultParsers.spaceDelimited("<arg>").parsed, streams.value.log)
  )

}

trait SearchWithDependencies
  extends Search
  with MavenOrgSearcher
  with ResultsParser
