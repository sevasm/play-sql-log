name := """play-sql-log"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJpa,
  evolutions,
  cache,
  "org.hibernate" % "hibernate-entitymanager" % "4.3.11.Final",
  "org.hibernate" % "hibernate-ehcache" % "4.3.11.Final"
)
