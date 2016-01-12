addSbtPlugin("com.typesafe.sbt" % "sbt-scalariform" % "1.3.0")

libraryDependencies <+= (sbtVersion) { sv =>
  "org.scala-sbt" % "scripted-plugin" % sv
}

// Scripted plugin needs to declare this as a dependency
libraryDependencies += "jline" % "jline" % "2.11"
