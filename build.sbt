lazy val commonSettings = Seq(
    organization := "io.voiceit",
    version := "2.7.2",
    scalaVersion := "2.12.6"
)

lazy val root = (project in file("."))
  .settings(
      commonSettings,
      name := "VoiceIt3",
      libraryDependencies +=  "org.scalaj" %% "scalaj-http" % "2.4.2",
      libraryDependencies += "com.typesafe.play" %% "play-json" % "2.9.4" % "test",
      libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.16" % "test",
      libraryDependencies += "commons-io" % "commons-io" % "2.13.0" % "test"
  )
