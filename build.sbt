import sbt.Keys._
import sbt.Resolver

name := "slick-xor"

version := "1.0-SNAPSHOT"

scalaVersion := "2.12.0"

// no more lame code
scalacOptions ++= Seq("-Xfatal-warnings", "-feature", "-deprecation")


libraryDependencies ++= {
  Seq(
    // slick
    "com.typesafe.slick" %% "slick" % "3.2.0-M2",

    // db driver
    "mysql" % "mysql-connector-java" % "6.0.5",


    // logging
    "ch.qos.logback" % "logback-classic" % "1.1.7",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",

    // db migrations
    "org.flywaydb" % "flyway-maven-plugin" % "4.0.3"
      exclude("org.slf4j", "slf4j-nop")
      exclude("org.slf4j", "slf4j-jdk14")
  ) ++ Seq(
    "org.scalatest" %% "scalatest" % "3.0.0" % "test"
  )
}

resolvers ++= Seq(
   Resolver.sonatypeRepo("snapshots")
)

//do not generate scaladoc in dist task
sources in (Compile,doc) := Seq.empty
publishArtifact in (Compile, packageDoc) := false

testOptions in Test += Tests.Argument("-oDF")

scalastyleConfig := baseDirectory.value / "plugins-conf" / "scalastyle-config.xml"

wartremoverErrors ++= Seq(
//  Wart.Any,
  Wart.Any2StringAdd,
  Wart.AsInstanceOf,
  Wart.EitherProjectionPartial,
  Wart.IsInstanceOf,
  Wart.ListOps,
//  Wart.NonUnitStatements,
  Wart.Null,
//  Wart.OptionPartial,
//  Wart.Product,
  Wart.Return,
//  Wart.Serializable,
//  Wart.Throw,
  Wart.TryPartial,
  Wart.While,
  Wart.Var,
  Wart.JavaConversions
)