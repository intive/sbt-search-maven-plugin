enablePlugins(SbtSearchMavenPlugin)

name := "simple-test"

version := "0.1.0"

TaskKey[Unit]("true-test") := {
  assert(true, "Truth test")
}