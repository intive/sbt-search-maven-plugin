package com.blstream.sbtsearchmavenplugin

import sbt._
import sbt.complete._
import complete.DefaultParsers._
import sbt.Keys.{ streams, target }
import java.nio.charset.Charset

object SbtSearchMavenPlugin extends AutoPlugin {

  object autoImport {
    lazy val searchMaven = InputKey[Unit]("searchMaven", "Search maven")
  }

  import autoImport._

  override lazy val projectSettings = Seq(
    searchMaven := search(spaceDelimited("<arg>").parsed, streams.value.log)
  )

  def search(args: Seq[String], log: Logger) {
    args foreach (arg => log info arg)
  }

}