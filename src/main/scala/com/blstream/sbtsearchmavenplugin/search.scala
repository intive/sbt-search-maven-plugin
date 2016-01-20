package com.blstream.sbtsearchmavenplugin

import java.net.{ URL, URLConnection }
import sbt.Logger
import scala.util.Try

case class Artifact(g: String, a: String, latestVersion: String)

trait Search {
  self: MavenOrgSearcher with QueryCleaner with ResultsParser with ArtifactsPrinter =>

  def search(args: Seq[String], log: Logger): Unit = {
    val results = for {
      queryString <- args.headOption.toRight[Error]("usage: searchMaven queryString").right
      cleanedQuery <- cleanQuery(queryString).right
      jsonResults <- query(cleanedQuery).right
      artifacts <- parseResults(jsonResults).right
    } yield {
      printArtifacts(cleanedQuery)(artifacts)
    }

    results.fold(
      log.warn(_),
      log.info(_)
    )
  }

}

trait ArtifactsPrinter {

  def printArtifacts: String => List[Artifact] => String =
    query => artifacts => {
      val separator = "%"
      val quotesLength = 2
      val max = countMaxColumnsSizes(artifacts)
      artifacts.map { a =>
        val col1Length = max._1 + quotesLength
        val col2Length = max._2 + quotesLength
        val group = s""""${a.g}""""
        val artifact = s""""${a.a}""""
        val version = s""""${a.latestVersion}""""

        s"%-${col1Length}s %s %-${col2Length}s %s %s".format(group, separator, artifact, separator, version).trim
      }.mkString(s"Results for $query:\n", "\n", "")
    }

  private def countMaxColumnsSizes: List[Artifact] => (Int, Int) =
    artifacts =>
      artifacts.foldLeft((0, 0))((m, a) => (Math.max(m._1, a.g.length), Math.max(m._2, a.a.length)))

}

trait QueryCleaner {
  def cleanQuery: String => Either[Error, String] =
    rawQuery => {
      val pattern = "[^a-zA-Z0-9-]".r
      val q = pattern.replaceAllIn(rawQuery, "")
      if (q.isEmpty) Left("Empty query, only a-zA-Z0-9- allowed")
      else Right(q)
    }
}

trait MavenOrgSearcher {

  def query: String => Either[Error, Json] =
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

  def parseResults: Json => Either[Error, List[Artifact]] =
    results => {
      val json = parse(results)
      val suggestionsJson = json \ "spellcheck" \ "suggestions"
      if ((json \ "response" \ "numFound").extract[Int] > 0) {
        val artifacts = json \ "response" \ "docs"
        Right(artifacts.extract[List[Artifact]])
      } else {
        suggestionsJson.extractOpt[List[String]] match {
          case Some(_) => Left("Artifact not found")
          case None => {
            val suggestions = (suggestionsJson(1) \ "suggestion").extract[List[String]].mkString(", ")
            Left(s"Artifact not found, did you mean: $suggestions?")
          }
        }
      }
    }

}
