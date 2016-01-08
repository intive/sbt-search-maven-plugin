package com.blstream.sbtsearchmavenplugin

import sbt._
import sbt.complete._
//import complete.DefaultParsers._
import sbt.Keys.{ streams, target }
import java.nio.charset.Charset
import net.liftweb.json._

object SbtSearchMavenPlugin extends AutoPlugin {

  object autoImport {
    lazy val searchMaven = InputKey[Unit]("searchMaven", "Search maven")
  }

  import autoImport._

  override lazy val projectSettings = Seq(
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

    found.map(_.map(a => s"${a.g} %% ${a.a} % ${a.latestVersion}").foreach(x => log info x))
  }

}

case class Artifact(g: String, a: String, latestVersion: String)
