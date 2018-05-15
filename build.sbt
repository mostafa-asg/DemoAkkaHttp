name := "DemoOnlineShop"

version := "0.1"

scalaVersion := "2.12.6"

libraryDependencies ++= {

  val akkaVersion = "2.5.12"
  val akkaHttpVersion = "10.1.1"
  val slickVersion = "3.2.3"
  val h2DatabaseVersion = "1.4.197"

  Seq(
    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-stream" % akkaVersion ,
    "com.typesafe.slick" %% "slick" % slickVersion,
    "com.h2database" % "h2" % h2DatabaseVersion
  )

}