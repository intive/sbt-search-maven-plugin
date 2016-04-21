package com.blstream.sbtsearchmavenplugin

import java.io.PrintWriter

trait ArtifactFileHelper {
  self: DependencyReaderWriter =>

  def getFileContents: String = {
    val string = try {
      scala.io.Source.fromFile(artifactFile).getLines.mkString("\n")
    } catch {
      case _: Throwable => ""
    }
    string
  }

  def createEmptyFile: Unit = artifactFile.createNewFile()

  def createFileWithContents(contents: String): Unit = {
    createEmptyFile
    val writer = new PrintWriter(artifactFile)

    writer.write(contents)
    writer.close()
  }

  def before = {
    try {
      artifactFile.delete()
    } catch {
      case (_: Throwable) =>
    }
  }
  def after = before
}
