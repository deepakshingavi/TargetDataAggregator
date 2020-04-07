name := "TargetDataAggregator"

version := "1.0"

scalaVersion := "2.12.0"

libraryDependencies ++= Seq(
  "org.scalaj" % "scalaj-http_2.12" % "2.4.2",
  "io.spray" %% "spray-json" % "1.3.5" % "provided",
  "org.apache.spark" % "spark-core_2.12" % "2.4.4",
  "org.apache.spark" % "spark-sql_2.12" % "2.4.4",
  "org.scalatest" % "scalatest_2.12" % "3.3.0-SNAP2" % "provided",
  "org.scalamock" % "scalamock_2.12" % "4.4.0"
)

assemblyMergeStrategy in assembly := {
  case "mozilla/public-suffix-list.txt" => MergeStrategy.discard
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}
