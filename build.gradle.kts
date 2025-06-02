// build.gradle.kts (корень проекта)
tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
