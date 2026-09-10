plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.gradle.plugin)
    alias(libs.plugins.buildconfig)
    alias(libs.plugins.vanniktech.publish)
}

buildConfig {
    packageName("com.axiom.compiler.plugin")

    buildConfigField("String", "KOTLIN_PLUGIN_ID", "\"io.github.axiom-dev.compiler.plugin\"")

    val pluginProject = project(":axiom-compiler-plugin")
    buildConfigField("String", "KOTLIN_PLUGIN_GROUP", "\"${pluginProject.group}\"")
    buildConfigField("String", "KOTLIN_PLUGIN_NAME", "\"${pluginProject.name}\"")
    buildConfigField("String", "KOTLIN_PLUGIN_VERSION", "\"${pluginProject.version}\"")

    val apiProject = project(":axiom-api")
    buildConfigField(
        type = "String",
        name = "AXIOM_API_LIBRARY_COORDINATES",
        expression = "\"${apiProject.group}:${apiProject.name}:${apiProject.version}\""
    )

    buildConfigField("String", "KOTLIN_VERSION", "\"${libs.versions.kotlin}\"")
    buildConfigField("String", "AGP_VERSION", "\"${libs.versions.agp}\"")
}

gradlePlugin {
    plugins {
        create("AxiomCompilerGradleSubplugin") {
            id = "io.github.axiom-dev.compiler.plugin"
            displayName = "AxiomPlugin"
            description = "AxiomCompilerGradleSubplugin"
            implementationClass = "com.axiom.compiler.plugin.AxiomCompilerGradleSubplugin"
        }
    }
}

dependencies {
    compileOnly(gradleApi())
    implementation(kotlin("gradle-plugin"))
    implementation(libs.compose.gradle.plugin)
    implementation(libs.kotlin.serialization)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.android.tools)
}
