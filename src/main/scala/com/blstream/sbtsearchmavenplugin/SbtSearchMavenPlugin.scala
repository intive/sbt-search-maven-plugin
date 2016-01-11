package com.blstream.sbtsearchmavenplugin

import sbt._
import sbt.Keys.streams
import net.liftweb.json._

object SbtSearchMavenPlugin extends AutoPlugin {

  object autoImport {
    lazy val searchMaven = InputKey[Unit]("searchMaven", "Search maven")
  }

  import autoImport._

  override lazy val projectSettings = Seq(
    //TODO: maybe custom parser is a better idea here
    searchMaven := search(complete.DefaultParsers.spaceDelimited("<arg>").parsed, streams.value.log)
  )

  def search(args: Seq[String], log: Logger) {

    implicit val formats = DefaultFormats

    val found = args.headOption.map { head =>
      val query = s"http://search.maven.org/solrsearch/select?q=$head&rows=20&wt=json"
      val results = scala.io.Source.fromURL(query).mkString
      val json = parse(results)
      val artifacts = json \ "response" \ "docs"
      artifacts.extract[List[Artifact]]
    }

    found.getOrElse(Nil)
      .foreach(a => log.info(s"${a.g} %% ${a.a} % ${a.latestVersion}"))
  }

}

case class Artifact(g: String, a: String, latestVersion: String)
