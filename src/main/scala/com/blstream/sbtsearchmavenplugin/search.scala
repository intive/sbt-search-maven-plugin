package com.blstream.sbtsearchmavenplugin

import sbt.Logger

case class Artifact(g: String, a: String, latestVersion: String)

trait Search {
  self: MavenOrgSearcher with ResultsParser =>

  def search(args: Seq[String], log: Logger) {

    val found = args.headOption.map { head =>
      (query andThen parseResults)(head)
    }

    found.getOrElse(Nil)
      .foreach(a => log.info(s"${a.g} %% ${a.a} % ${a.latestVersion}"))
  }

}

trait MavenOrgSearcher {

  def query: String => String =
    queryString => {
      val query = s"http://search.maven.org/solrsearch/select?q=$queryString&rows=20&wt=json"
      scala.io.Source.fromURL(query).mkString
    }

}

trait ResultsParser {

  import net.liftweb.json._
  implicit val formats = DefaultFormats

  def parseResults: String => List[Artifact] =
    results => {
      val json = parse(results)
      val artifacts = json \ "response" \ "docs"
      artifacts.extract[List[Artifact]]
    }

}
