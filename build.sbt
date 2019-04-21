name := "parsely"

organization := "com.github.bzumhagen"

scalaVersion := "2.12.4"

libraryDependencies ++= Seq(
  "com.github.scopt" %% "scopt" % "4.0.0-RC2",
  "com.github.pathikrit" %% "better-files" % "3.7.1"
)

// Test Dependencies
libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.5" % Test
)

enablePlugins(BuildInfoPlugin)

buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion)

buildInfoPackage := "com.github.bzumhagen.parsely"