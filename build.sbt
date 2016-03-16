lazy val root = (project in file(".")).enablePlugins(JavaAppPackaging)

name := "simple-spray"

organization := "com.monsanto.demo"

scalaVersion := "2.10.4"

crossScalaVersions := Seq("2.10.4", "2.11.1")

val akka = "2.3.9"

val spray = "1.3.2"



val slickVersion = "3.0.0"

resolvers ++= Seq(
  "Spray repository" at "http://repo.spray.io",
  "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
"scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
)

libraryDependencies ++=
    Seq(
      // -- Logging --
        "ch.qos.logback" % "logback-classic" % "1.1.2",
        "com.typesafe.scala-logging" %% "scala-logging-slf4j" % "2.1.2",
        // -- Akka --
        "com.typesafe.akka" %% "akka-testkit" % akka % "test",
        "com.typesafe.akka" %% "akka-actor" % akka,
        "com.typesafe.akka" %% "akka-slf4j" % akka,
        // -- Spray --
        "io.spray" %% "spray-routing" % spray,
        "io.spray" %% "spray-client" % spray,
        "io.spray" %% "spray-testkit" % spray % "test",
        // -- json --
        "io.spray" %% "spray-json" % "1.3.1",
        // -- config --
        "com.typesafe" % "config" % "1.2.1",
        // -- testing --
        "org.scalatest" %% "scalatest" % "2.2.1" % "test",
        "mysql" % "mysql-connector-java" % "5.1.35",
         "com.typesafe.slick" %% "slick" % "3.0.0",
         "com.rockymadden.stringmetric" %% "stringmetric-core" % "0.27.3"
    )


unmanagedJars in Compile += file("/home/katakonst/Desktop/Untitled.jar")



releaseSettings
