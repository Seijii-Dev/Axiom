plugins {
    alias(libs.plugins.buildconfig)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.vanniktech.publish)
}

kotlin {
    compilerOptions {
        optIn.add("org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi")
        optIn.add("org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI")
    }
}

group = "io.github.axiom-dev"
version = property("project.version") as String

buildConfig {
    useKotlinOutput {
        internalVisibility = true
    }

    packageName("com.axiom.compiler.plugin")
    buildConfigField("String", "KOTLIN_PLUGIN_ID", "\"io.github.axiom-dev.compiler.plugin\"")
}

dependencies {
    compileOnly(libs.kotlin.compiler)
}
