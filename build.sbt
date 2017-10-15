name := "project_burgundy"

version := "1.0"

lazy val `project_burgundy` = (project in file(".")).enablePlugins(PlayJava)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

scalaVersion := "2.12.2"

libraryDependencies ++= Seq(javaJdbc, ehcache, javaWs, guice,
  "org.webjars" % "bootstrap" % "4.0.0-beta",
  "org.webjars" % "jquery" % "3.2.1")

//unmanagedResourceDirectories in Test += baseDirectory ( _ /"target/web/public/test" )

      