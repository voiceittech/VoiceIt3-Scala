lazy val commonSettings = Seq(
    organization := "io.voiceit",
    version := "1.0.0",
    scalaVersion := "2.12.6"
)

lazy val root = (project in file("."))
  .settings(
      commonSettings,
      name := "VoiceIt2",
      libraryDependencies ++= Seq(
        "com.typesafe.akka" %% "akka-http" % "10.1.3",
        "com.typesafe.akka" %% "akka-stream" % "2.5.12"
      )
  )
