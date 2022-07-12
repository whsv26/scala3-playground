ThisBuild / version := "0.1.0-SNAPSHOT"

lazy val cats = (project in file("./projects/cats"))
  .settings(
    scalaVersion := "3.1.0",
    name := "cats",
    idePackagePrefix := Some("org.whsv26.playground.cats"),
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-core" % "2.7.0",
      "org.typelevel" %% "cats-effect" % "3.3.4"
    )
  )

lazy val zio = (project in file("./projects/zio"))
  .settings(
    scalaVersion := "3.1.0",
    name := "zio",
    idePackagePrefix := Some("org.whsv26.playground.zio"),
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % "1.0.9",
      "dev.zio" %% "zio-streams" % "1.0.9"
    )
  )

lazy val fs2 = (project in file("./projects/fs2"))
  .settings(
    scalaVersion := "3.1.0",
    name := "fs2",
    idePackagePrefix := Some("org.whsv26.playground.fs2"),
    libraryDependencies ++= Seq(
      "co.fs2" %% "fs2-core" % "3.2.5",
      "co.fs2" %% "fs2-io" % "3.2.5"
    )
  )

lazy val stdlib = (project in file("./projects/stdlib"))
  .settings(
    scalaVersion := "3.1.0",
    name := "stdlib",
    idePackagePrefix := Some("org.whsv26.playground.stdlib")
  )
