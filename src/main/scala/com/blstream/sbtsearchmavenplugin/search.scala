package com.blstream.sbtsearchmavenplugin

import sbt.Logger

case class Artifact(g: String, a: String, latestVersion: String)

trait Search {
  self: MavenOrgSearcher with ResultsParser =>

  def search(args: Seq[String], log: Logger) {

    val found = for {
      queryString <- args.headOption.toRight[Error]("usage: searchMaven queryString").right
      jsonResults <- query(queryString).right
      artifacts <- parseResults(jsonResults).right
    } yield artifacts

    found.fold(
      err => log.warn(err),
      _.foreach(a => log.info(s"${a.g} %% ${a.a} % ${a.latestVersion}"))
    )
  }

}

trait MavenOrgSearcher {

  //TODO add timeout
  def query: String => Either[Error, String] =
    queryString => {
      val query = s"http://search.maven.org/solrsearch/select?q=$queryString&rows=20&wt=json"
      Right(scala.io.Source.fromURL(query).mkString)
    }

}

trait ResultsParser {

  import net.liftweb.json._
  implicit val formats = DefaultFormats

  def parseResults: String => Either[Error, List[Artifact]] =
    results => {
      val json = parse(results)
      val suggestionsJson = json \ "spellcheck" \ "suggestions"
      if ( (json \ "response" \ "numFound").extract[Int] > 0 ) {
        val artifacts = json \ "response" \ "docs"
        Right(artifacts.extract[List[Artifact]])
      } else {
        suggestionsJson.extractOpt[List[String]] match {
          case Some(_) => Left(s"Artifact not found")
          case None => {
            val suggestions = (suggestionsJson(1) \ "suggestion").extract[List[String]].mkString(", ")
            Left(s"Artifact not found, did you mean: $suggestions")
          }
        }
      }
    }

}
