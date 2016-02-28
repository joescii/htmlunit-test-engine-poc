name := "htmlunit-test-engine-poc"

organization := "com.joescii"

homepage := Some(url("https://github.com/joescii/htmlunit-test-engine-poc"))

version := "0.0.1"

// Since the plan is to build an sbt plugin...
scalaVersion in Global := "2.10.4"

libraryDependencies += "net.sourceforge.htmlunit" % "htmlunit" % "2.19"
