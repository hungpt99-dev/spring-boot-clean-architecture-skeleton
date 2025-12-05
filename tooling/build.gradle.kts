plugins {
    id("application")
}

val cliMain = (findProperty("cliMainClass") as? String)?.ifBlank { null }
    ?: "com.yourorg.yourapp.cli.CodegenCli"

dependencies {
    // No external dependencies needed currently.
}

application {
    mainClass.set(cliMain)
}

tasks.named<JavaExec>("run") {
    group = "application"
    description = "Run the codegen CLI"
}

tasks.register<JavaExec>("runCli") {
    group = "application"
    description = "Alias to run the codegen CLI"
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass.set(application.mainClass)
}

