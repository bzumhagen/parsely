name := "seaport"

organization := "com.here.platform.data"

scalaVersion := "2.12.4"

version := (version in ThisBuild).value

// Dependencies
val akkaHttpCorsVersion       = "0.2.1"
val akkaHttpVersion           = "10.1.0"
val akkaVersion               = "2.5.17"
val akkaStreamKafkaVersion    = "0.20"
val commonsLang3Version       = "3.6"
val scaffeineVersion          = "2.5.0"
val aspectjWeaverVersion      = "1.8.13"
val kamonAkkaVersion          = "1.0.1"

// Internal Dependencies
val aaaVersion                 = "0.4.13"
val kafkaJwtJaasClientVersion  = "1.00.00-20180612-47"

val akkaGroup  = "com.typesafe.akka"
val kamonGroup = "io.kamon"

updateOptions := updateOptions.value.withCachedResolution(true)
// TODO conflictManager := ConflictManager.strict

libraryDependencies ++= Seq(
  akkaGroup %% "akka-http" % akkaHttpVersion,
  akkaGroup %% "akka-stream" % akkaVersion,
  akkaGroup %% "akka-http-spray-json" % akkaHttpVersion,
  akkaGroup %% "akka-stream-kafka" % akkaStreamKafkaVersion,
  akkaGroup %% "akka-slf4j" % akkaVersion,
  "org.apache.commons" % "commons-lang3" % commonsLang3Version,
  "com.github.blemale" %% "scaffeine" % scaffeineVersion,
  // Metrics + Tracing dependencies
  kamonGroup %% "kamon-akka-2.5" % kamonAkkaVersion,

  "com.here.platform.kafka.security.jwt" % "kafka-jwt-jaas-client" % kafkaJwtJaasClientVersion,
  "com.here.account" % "here-oauth-client" % aaaVersion
)

// Test Dependencies
val cucumberVersion          = "1.2.5"
val cukesRestVersion         = "0.2.25"
val junitVersion             = "4.12"
val junitInterfaceVersion    = "0.11"
val mockServerVersion        = "3.10.8"
val publicationClientVersion = "0.8.1"
val scalaMockVersion         = "4.1.0"
val scalaTestVersion         = "3.0.5"
val gatlingVersion           = "3.0.0"

libraryDependencies ++= Seq(
  // Component test
  "org.mock-server" % "mockserver-client-java" % mockServerVersion % Test,

  // Performance test, run with "sbt gatling:test"
  "io.gatling" % "gatling-test-framework" % gatlingVersion % Test,
  "io.gatling" % "gatling-graphite" % gatlingVersion % Test,
  "io.gatling.highcharts" % "gatling-charts-highcharts" % gatlingVersion % Test,
  "com.here.account" % "here-oauth-client" % aaaVersion % Test
)

scalaSource in Gatling := sourceDirectory.value / "perf" / "scala"
resourceDirectory in Gatling := sourceDirectory.value / "perf" / "resources"

publishTo := Some("Artifactory Realm" at "https://artifactory.in.here.com/artifactory/here-olp-sit/")

coverageExcludedPackages := """
  com.here.platform.data.seaport.config*;
  com.here.platform.data.seaport.json*;
  com.here.platform.data.seaport.routes.MainRoutes.*;
  com.here.platform.data.seaport.routes.SeaportHandlers;
  com.here.platform.data.seaport;
  com.here.platform.data.WebServer;"""
coverageMinimum := 80
coverageFailOnMinimum := true