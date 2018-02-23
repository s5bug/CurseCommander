addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)

lazy val root = (project in file(".")).settings(
  name := "CurseCommander",
  version := "0.1",
  scalaVersion := "2.12.4",
  libraryDependencies ++= Seq(
    "org.scalafx" %% "scalafx" % "8.0.144-R12",
    "org.scalafx" %% "scalafxml-core-sfx8" % "0.4",
    "org.jsoup" % "jsoup" % "1.11.2"
  )
)
