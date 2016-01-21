organization := "com.blstream"

name := "sbt-search-maven-plugin"

description := "Sbt plugin to search with search.maven.org"

licenses += ("MIT", url("https://opensource.org/licenses/MIT"))

version := "0.1.0"

scalaVersion := "2.10.6"

sbtPlugin := true

scriptedSettings

scriptedLaunchOpts <+= version apply { v => "-Dproject.version="+v }

libraryDependencies ++= Seq(
  "net.liftweb" %% "lift-json"    % "2.6.2",
  "org.specs2"  %% "specs2-core"  % "3.7"  % "test")

scalariformSettings
