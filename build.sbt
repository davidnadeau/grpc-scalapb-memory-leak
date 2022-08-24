val commonSettings = Seq(
  organization := "com.etsy.orca",
  scalaVersion := "2.13.8",
  crossPaths := false
)
Compile / PB.targets := Seq(
  scalapb.gen() -> (Compile / sourceManaged).value / "scalapb"
)

val GatlingVersion = "3.7.6"
val GatlingGrpcVersion = "0.13.0"
val ScalaPBVersion = "0.11.11"
val GatlingExtVersion = "0.3.0"

lazy val root = (project in file("."))
  .enablePlugins(GatlingPlugin)
  .settings(commonSettings: _*)
  .settings(
    name := "grpc-threadpool-benchmarks",
    libraryDependencies ++= Seq(
      "com.thesamet.scalapb" %% "compilerplugin" % ScalaPBVersion,
      "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion % "protobuf",
      "io.grpc" % "grpc-netty" % scalapb.compiler.Version.grpcJavaVersion,
      "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % scalapb.compiler.Version.scalapbVersion,
      "com.github.phisgr" % "gatling-grpc" % GatlingGrpcVersion % "test,it",
      "io.gatling.highcharts" % "gatling-charts-highcharts" % GatlingVersion % "test,it",
      "io.gatling" % "gatling-core" % GatlingVersion,
      "io.gatling" % "gatling-test-framework" % GatlingVersion % "test,it"
    )
  )
