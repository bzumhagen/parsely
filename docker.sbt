enablePlugins(DockerPlugin)
dockerfile in docker := {
  // The assembly task generates a fat JAR file
  val artifact: File = assembly.value
  val artifactTargetPath = s"/app/${artifact.name}"
  val port = 8080

  new Dockerfile {
    from("openjdk:10.0.2-13-jre-slim")
    maintainer("Ben Zumhagen", "bzumhagen@gmail.com")
    add(artifact, artifactTargetPath)
    runRaw("chown -R nobody /app")
    user("nobody")
    expose(port)
    entryPointRaw("java -jar " + artifactTargetPath)
  }
}
imageNames in docker := Seq(
  // Sets the latest tag
  ImageName(s"${organization.value}/${name.value}:latest"),

  // Sets a name with a tag that contains the project version
  ImageName(
    namespace = Some(organization.value),
    repository = name.value,
    tag = Some("v" + version.value)
  )
)