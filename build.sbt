

lazy val commonSettings = Seq(
  organization := "org.ml4ai",
  version := "0.1-SNAPSHOT",
  scalaVersion := "2.11.11"
)

libraryDependencies ++= Seq(
  "org.clulab" %% "reach-main" % "1.5.0",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe" % "config" % "1.3.4"
)

lazy val root = (project in file("."))
    .settings(
      commonSettings,
      name := "elion"
    )


lazy val webApp = (project in file("webapp"))
  .dependsOn(root)
  .enablePlugins(PlayScala)
  .settings(
    commonSettings
  )