javaHome := Some(file(System.getenv("JAVA_HOME")))

name := "tcptap"

version := "0.9.1"

organization := "ch.ocram"

scalaVersion := "2.10.1"

javacOptions ++= Seq("-source", "1.7", "-target", "1.7")

scalacOptions ++= Seq("-deprecation", "-feature")

unmanagedJars in Compile += Attributed.blank(file(System.getenv("JAVA_HOME") + "/jre/lib/jfxrt.jar"))


// libraryDependencies += "org.scalatest" %% "scalatest" % "1.9.1" % "test"

// libraryDependencies += "junit" % "junit" % "4.10" % "test"
libraryDependencies += "org.scalafx" %% "scalafx" % "1.0.0-M2"
