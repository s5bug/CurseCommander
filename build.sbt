lazy val root = (project in file(".")).settings(
  name := "CurseCommander",
  version := "0.1",
  scalaVersion := "2.12.4",
  libraryDependencies ++= Seq(
    "org.jsoup" % "jsoup" % "1.11.2"
  )
)
