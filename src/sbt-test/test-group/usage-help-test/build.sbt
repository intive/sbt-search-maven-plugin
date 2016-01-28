name := "simple-test"

version := "0.1.0"

scalaVersion := "2.10.6"

TaskKey[Unit]("check-search-usage-output") := {
  val lastLog: File = BuiltinCommands.lastLogFile(state.value).get
  val last: String = IO.read(lastLog)
  assert(last.contains("usage:"))
}
