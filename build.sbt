name := "project_burgundy"

version := "1.0"

lazy val `project_burgundy` = (project in file(".")).enablePlugins(PlayJava, LauncherJarPlugin)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

scalaVersion := "2.12.2"

libraryDependencies ++= Seq(javaJdbc, ehcache, ws, javaWs, guice,
  "org.webjars" % "bootstrap" % "4.0.0-beta",
  "com.google.cloud" % "google-cloud-firestore" % "0.25.0-beta",
  "org.mockito" % "mockito-core" % "1.10.19" % "test")

//unmanagedResourceDirectories in Test += baseDirectory ( _ /"target/web/public/test" )