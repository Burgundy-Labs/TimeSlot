name := "project_burgundy"
 
version := "1.0"
      
lazy val `project_burgundy` = (project in file(".")).enablePlugins(PlayJava)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

scalaVersion := "2.12.2"

libraryDependencies ++= Seq( javaJdbc , ehcache , javaWs, guice )

//unmanagedResourceDirectories in Test += baseDirectory ( _ /"target/web/public/test" )

      