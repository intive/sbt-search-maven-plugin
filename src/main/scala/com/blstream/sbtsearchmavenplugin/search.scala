package com.blstream.sbtsearchmavenplugin

import java.net.{ URL, URLConnection }
import sbt.Logger
import scala.util.Try

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
      artifacts => printResults(log)(artifacts)
    )
  }

  private def printResults: Logger => List[Artifact] => Unit =
    log => artifacts => {
      val separator = "%"
      val max = countMaxColumnsSizes(artifacts)
      artifacts.foreach(a =>
        log.info(s"%-${max._1}s %s %-${max._2}s %s %-${max._3}s".format(a.g, separator, a.a, separator, a.latestVersion)))
    }

  private def countMaxColumnsSizes: List[Artifact] => (Int, Int, Int) =
    artifacts =>
      artifacts.foldLeft((0, 0, 0))((m, a) => (Math.max(m._1, a.g.length), Math.max(m._2, a.a.length), Math.max(m._3, a.latestVersion.length)))

}

trait MavenOrgSearcher {

  def query: String => Either[Error, String] =
    queryString => {
      val query = s"http://search.maven.org/solrsearch/select?q=$queryString&rows=20&wt=json"
      val connMaybe = prepareConnection(query)
      connMaybe.right.map {
        conn => scala.io.Source.fromInputStream(conn.getInputStream).mkString
      }
    }

  private def prepareConnection: String => Either[Error, URLConnection] =
    query => {
      Try {
        val conn = new URL(query).openConnection()
        conn.setConnectTimeout(3000)
        conn.setReadTimeout(3000)
        Right(conn)
      }.recover {
        case _: Exception => Left("Connection failure, try again later.")
      }.get
    }

}

trait ResultsParser {

  import net.liftweb.json._
  implicit val formats = DefaultFormats

  def parseResults: String => Either[Error, List[Artifact]] =
    results => {
      val json = parse(results)
      val suggestionsJson = json \ "spellcheck" \ "suggestions"
      if ((json \ "response" \ "numFound").extract[Int] > 0) {
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
