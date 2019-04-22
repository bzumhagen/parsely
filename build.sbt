
name := "parsely"

organization := "com.github.bzumhagen"

scalaVersion := "2.12.4"

val akkaGroup = "com.typesafe.akka"

libraryDependencies ++= Seq(
  "com.github.scopt" %% "scopt" % "4.0.0-RC2",
  "com.github.pathikrit" %% "better-files" % "3.7.1",
  akkaGroup %% "akka-http"   % "10.1.8",
  akkaGroup %% "akka-stream" % "2.5.19",
  akkaGroup %% "akka-http-spray-json" % "10.1.8"
)

// Test Dependencies
libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.5" % Test,
  "org.scalamock" %% "scalamock" % "4.1.0" % Test,
  akkaGroup %% "akka-stream-testkit" % "2.5.19",
  akkaGroup %% "akka-http-testkit" % "10.1.8"
)

enablePlugins(BuildInfoPlugin)

buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion)

buildInfoPackage := "com.github.bzumhagen.parsely"

coverageExcludedPackages := """com.github.bzumhagen.parsely.Parsely;"""
coverageMinimum := 80
coverageFailOnMinimum := true