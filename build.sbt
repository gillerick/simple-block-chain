ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name := "mini-block-chain",
    idePackagePrefix := Some("io.iohk.atala.swetest")
  )

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.2.15" % "test",
  "org.scalameta" %% "munit" % "0.7.29" % Test,
)
