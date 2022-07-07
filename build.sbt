ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

Compile / PB.targets := Seq(
  scalapb.gen() -> (Compile / sourceManaged).value / "scalapb"
)

lazy val root = (project in file("."))
  .settings(
    name := "grpc-memory-leak",
    libraryDependencies ++= Seq(
      "com.thesamet.scalapb" %% "compilerplugin" % "0.11.10",
      "io.grpc" % "grpc-core" % "1.45.0",
      "io.grpc" % "grpc-api" % "1.45.0",
      "io.grpc" % "grpc-protobuf" % "1.45.0",
      "io.grpc" % "grpc-stub" % "1.45.0",
      "io.grpc" % "grpc-netty-shaded" % "1.45.0",
      "com.thesamet.scalapb"     %% "scalapb-runtime" % "0.11.10",
      "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % "0.11.10",
      "com.beachape" %% "enumeratum" % "1.7.0",
      "org.typelevel" %% "cats-core" % "2.7.0"
    )
  )