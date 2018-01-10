name := "project_burgundy"

version := "1.0"

lazy val `project_burgundy` = (project in file(".")).enablePlugins(PlayJava, LauncherJarPlugin, PlayAkkaHttp2Support)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

scalaVersion := "2.12.2"

libraryDependencies ++= Seq(javaJdbc, ehcache, ws, javaWs, guice,
  "org.webjars" % "bootstrap" % "4.0.0-beta",
  "com.google.cloud" % "google-cloud-firestore" % "0.32.0-beta",
  "com.google.firebase" % "firebase-admin" % "5.7.0",
  "org.mockito" % "mockito-core" % "1.10.19" % "test",
  "com.typesafe.play" %% "play-mailer" % "6.0.1",
  "com.typesafe.play" %% "play-mailer-guice" % "6.0.1",
)
