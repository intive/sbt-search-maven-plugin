package com.blstream.sbtsearchmavenplugin

import net.liftweb.json._
import sbt.Logger

case class Artifact(g: String, a: String, latestVersion: String)

trait Search {

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
