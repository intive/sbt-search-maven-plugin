name := "simple-test"

version := "0.1.0"

scalaVersion := "2.10.6"

TaskKey[Unit]("true-test") := {
  assert(true, "Truth test")
}