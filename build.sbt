organization := "com.blstream"

name := "sbt-search-maven-plugin"

version := "0.1-SNAPSHOT"

sbtPlugin := true

// Scripted - sbt plugin tests
scriptedSettings
scriptedLaunchOpts <+= version apply { v => "-Dproject.version="+v }