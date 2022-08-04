// basic
enablePlugins(AutomateHeaderPlugin)

name := "scala-ssh"
description := "A Scala library providing remote shell access via SSH"
organization := "com.decodified"
organizationHomepage := Some(new URL("http://decodified.com"))
homepage := Some(new URL("https://github.com/sirthias/scala-ssh"))
startYear := Some(2011)
licenses := Seq("Apache 2" → new URL("http://www.apache.org/licenses/LICENSE-2.0.txt"))
headerLicense := Some(HeaderLicense.ALv2("2011-2019", "Mathias Doenitz"))
scmInfo := Some(ScmInfo(url("https://github.com/sirthias/scala-ssh"), "scm:git:git@github.com:sirthias/scala-ssh.git"))
developers := List(
  Developer(id = "sirthias", name = "Mathias Doenitz", email = "", url = url("http://github.com/sirthias")))

scalaVersion := "2.13.1"
crossScalaVersions := Seq("2.12.10", "2.13.1")

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-language:_",
  "-unchecked",
  "-Xlint:_,-missing-interpolator",
  "-Xfatal-warnings",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ybackend-parallelism", "8",
  "-Ywarn-unused:imports,-patvars,-privates,-locals,-implicits,-explicits",
  "-Ycache-macro-class-loader:last-modified",
)

scalacOptions ++= {
  CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, 12)) => Seq(
      "-Yno-adapted-args",
      "-Ywarn-inaccessible",
      "-Ywarn-infer-any",
      "-Ywarn-nullary-override",
      "-Ywarn-nullary-unit",
      "-Xfuture",
      "-Xsource:2.13",
    )
    case Some((2, 13)) => Nil
    case x => sys.error(s"unsupported scala version: $x")
  }
}

scalacOptions in (Compile, console) ~= (_ filterNot(o => o.contains("warn") || o.contains("Xlint")))
scalacOptions in (Test, console) := (scalacOptions in (Compile, console)).value
scalacOptions in (Compile, doc) += "-no-link-warnings"
sourcesInBase := false

// reformat main and test sources on compile
scalafmtOnCompile := true

fork in Test := true

libraryDependencies ++= Seq(
  "com.hierynomus" % "sshj"                              % "0.27.0",
  "org.slf4j"      % "slf4j-api"                         % "1.7.28",
  "com.jcraft"     % "jsch.agentproxy.sshj"              % "0.0.9" % "provided",
  "com.jcraft"     % "jsch.agentproxy.connector-factory" % "0.0.9" % "provided",
  "ch.qos.logback" % "logback-classic"                   % "1.2.3" % "test",
  "org.scalatest"  %% "scalatest"                        % "3.0.8" % "test"
)

// publishing
publishMavenStyle := true
publishArtifact in Test := false
pomIncludeRepository := (_ => false)
publishTo := sonatypePublishToBundle.value

releaseCrossBuild := true
releaseProcess := {
  import ReleaseTransformations._
  Seq[ReleaseStep](
    checkSnapshotDependencies,
    inquireVersions,
    runClean,
    runTest,
    setReleaseVersion,
    commitReleaseVersion,
    tagRelease,
    publishArtifacts,
    releaseStepCommand("sonatypeBundleRelease"),
    setNextVersion,
    commitNextVersion,
    pushChanges
  )
}
