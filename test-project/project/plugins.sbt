// build root project
lazy val root = Project("plugins", file(".")) dependsOn(sbtSearchMavenPlugin)

// depends on the awesomeOS project
lazy val sbtSearchMavenPlugin = file("..").getAbsoluteFile.toURI