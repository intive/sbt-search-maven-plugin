organization := "com.blstream"

name := "sbt-search-maven-plugin"

version := "0.1"

sbtPlugin := true

scriptedSettings

scriptedLaunchOpts <+= version apply { v => "-Dproject.version="+v }

libraryDependencies ++= Seq(
  "net.liftweb" %% "lift-json"    % "2.6.2",
  "org.specs2"  %% "specs2-core"  % "3.7"  % "test")

scalariformSettings
