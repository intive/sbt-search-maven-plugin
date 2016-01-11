organization := "com.blstream"

name := "sbt-search-maven-plugin"

version := "0.1-SNAPSHOT"

sbtPlugin := true

scriptedSettings

scriptedLaunchOpts <+= version apply { v => "-Dproject.version="+v }

libraryDependencies += "net.liftweb" %% "lift-json" % "2.6.2"
