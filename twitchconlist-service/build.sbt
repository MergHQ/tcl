val ScalatraVersion = "2.7.0-RC1"

organization := "com.twitchconlistservice "

name := "twitchconlist-service"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.12.10"

resolvers += Classpaths.typesafeReleases

libraryDependencies ++= Seq(
  "org.scalatra" %% "scalatra" % ScalatraVersion,
  "org.scalatra" %% "scalatra-scalatest" % ScalatraVersion % "test",
  "ch.qos.logback" % "logback-classic" % "1.2.3" % "runtime",
  "org.eclipse.jetty" % "jetty-webapp" % "9.4.19.v20190610" % "container",
  "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided",
  "com.typesafe" % "config" % "1.4.0",
  "com.github.pureconfig" %% "pureconfig" % "0.12.2",
  "com.softwaremill.sttp.client" %% "core" % "2.0.0-RC9",
  "com.typesafe.play" %% "play-json" % "2.8.1",
  "com.softwaremill.sttp.client" %% "play-json" % "2.0.0-RC9",
  "com.softwaremill.sttp.client" %% "async-http-client-backend-future" % "2.0.0-RC9",
  "org.scalatra" %% "scalatra-json" % ScalatraVersion,
  "com.typesafe.akka" %% "akka-actor" % "2.5.3",
  "net.databinder.dispatch" %% "dispatch-core" % "0.13.1",
  "com.github.seratch" %% "awscala-dynamodb" % "0.8.+",

)

enablePlugins(SbtTwirl)
enablePlugins(ScalatraPlugin)
