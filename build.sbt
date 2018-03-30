name := "project_burgundy"

version := "1.3"

lazy val `project_burgundy` = (project in file(".")).enablePlugins(PlayJava, LauncherJarPlugin, PlayAkkaHttp2Support, SbtWeb, DockerPlugin)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

scalaVersion := "2.12.4"

libraryDependencies ++= Seq(ehcache, ws, javaWs, guice,
  "org.webjars" % "bootstrap" % "4.0.0-2",
  "com.google.cloud" % "google-cloud-firestore" % "0.42.0-beta",
  "com.google.firebase" % "firebase-admin" % "5.9.0",
  "org.mockito" % "mockito-core" % "1.10.19" % "test",
  "com.typesafe.play" %% "play-mailer" % "6.0.1",
  "com.typesafe.play" %% "play-mailer-guice" % "6.0.1",
  "com.mohiva" %% "play-html-compressor" % "0.7.1",
)

pipelineStages := Seq(gzip)
