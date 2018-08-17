lazy val commonSettings = Seq(
    organization := "io.voiceit",
    version := "1.0.6",
    scalaVersion := "2.12.6"
)

lazy val root = (project in file("."))
  .settings(
      commonSettings,
      name := "VoiceIt2",
      libraryDependencies +=  "org.scalaj" %% "scalaj-http" % "2.4.1",
      libraryDependencies += "com.typesafe.play" %% "play-json" % "2.6.7" % "test",
      libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test",
      libraryDependencies += "commons-io" % "commons-io" % "2.6" % "test"
  )
