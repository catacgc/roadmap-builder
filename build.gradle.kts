plugins {
    id("io.catacgc.kotlin-react") version "0.3.0"
}

tasks {
    val pegParser by registering(Exec::class) {
        val inputFile = "src/main/resources/parser.pegjs"
        val outputFile = "build/resources/main/parser.js"

        inputs.file(inputFile)
        outputs.file(outputFile)

        commandLine = "node_modules/.bin/pegjs -o $outputFile $inputFile".split(" ")
    }

    named("build") { dependsOn(pegParser) }

    val cleanup by registering(Delete::class) {
        delete("docs")
        delete("github-pages/*")
    }

    val docs by registering(Copy::class) {
        into("docs")

        from("build/bundle")

        from("samples") {
            filter { line ->
                line.replace("!public/index.html/body!", fetchBody())
                    .replace("{{ JS }}", "main.bundle.js?v=" + System.currentTimeMillis())
            }
        }

        outputs.dir("docs")

        dependsOn(cleanup, webpackBuild)
    }

    val cdn by registering(Copy::class) {
        into("github-pages")
        from("docs")
        dependsOn(docs)
    }
}

fun fetchBody(): String {
    return file("$buildDir/resources/main/index.html")
            .readText()
            .split("<body>")[1]
            .split("</body>")[0]
}
