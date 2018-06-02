name := "timeslot"

version := "1.4"

lazy val `timeslot` = (project in file(".")).enablePlugins(PlayMinimalJava, PlayAkkaHttp2Support, SbtWeb)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

scalaVersion := "2.12.5"

libraryDependencies ++= Seq(ehcache, guice,
  "org.webjars" % "bootstrap" % "4.1.0",
  "com.google.cloud" % "google-cloud-firestore" % "0.49.0-beta",
  "com.google.firebase" % "firebase-admin" % "6.2.0",
  "org.mockito" % "mockito-core" % "2.18.0" % "test",
  "com.typesafe.play" %% "play-mailer" % "6.0.1",
  "com.typesafe.play" %% "play-mailer-guice" % "6.0.1",
  "com.mohiva" %% "play-html-compressor" % "0.7.1",
)

pipelineStages := Seq(gzip)
