logLevel := Level.Warn

resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.6.7")
addSbtPlugin("org.irundaia.sbt" % "sbt-sassify" % "1.4.11")
addSbtPlugin("com.typesafe.sbt" % "sbt-gzip" % "1.0.2")
