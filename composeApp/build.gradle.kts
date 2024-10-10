import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)

            implementation(libs.circuit.codegen.annotations)
            implementation(libs.circuit.foundation)
            implementation(libs.circuit.runtime.ui)
            implementation(libs.circuitx.overlays)
            implementation(libs.circuitx.effects)

            implementation(libs.kotlinInject.runtime.kmp)
            implementation(libs.kotlinInject.anvil.runtime)
            implementation(libs.kotlinInject.anvil.runtime.optional)
        }
    }

    targets.configureEach {
        if (platformType == KotlinPlatformType.androidJvm) {
            compilations.configureEach {
                compileTaskProvider.configure {
                    compilerOptions {
                        freeCompilerArgs.addAll(
                            "-P",
                            "plugin:org.jetbrains.kotlin.parcelize:additionalAnnotation=tech.mapps.reproductionrepo.parcelize.CommonParcelize",
                        )
                    }
                }
            }
        }
    }
}

android {
    namespace = "tech.mapps.reproductionrepo"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "tech.mapps.reproductionrepo"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    add("kspCommonMainMetadata", libs.circuit.codegen)
    add("kspCommonMainMetadata", libs.kotlinInject.compiler.ksp)
    add("kspCommonMainMetadata", libs.kotlinInject.anvil.compiler)

    add("kspAndroid", libs.circuit.codegen)
    add("kspIosX64", libs.circuit.codegen)
    add("kspIosArm64", libs.circuit.codegen)
    add("kspIosSimulatorArm64", libs.circuit.codegen)

    add("kspAndroid", libs.kotlinInject.anvil.compiler)
    add("kspIosX64", libs.kotlinInject.anvil.compiler)
    add("kspIosArm64", libs.kotlinInject.anvil.compiler)
    add("kspIosSimulatorArm64", libs.kotlinInject.anvil.compiler)

    add("kspAndroid", libs.kotlinInject.compiler.ksp)
    add("kspIosX64", libs.kotlinInject.compiler.ksp)
    add("kspIosArm64", libs.kotlinInject.compiler.ksp)
    add("kspIosSimulatorArm64", libs.kotlinInject.compiler.ksp)
}

ksp {
    arg("me.tatarka.inject.generateCompanionExtensions", "true")
    arg("me.tatarka.inject.dumpGraph", "true")
    arg("circuit.codegen.lenient", "true")
    arg("circuit.codegen.mode", "kotlin_inject_anvil")
    arg(
        "anvil-ksp-extraContributingAnnotations",
        "com.slack.circuit.codegen.annotations.CircuitInject",
    )
    arg(
        "kotlin-inject-anvil-contributing-annotations",
        "com.slack.circuit.codegen.annotations.CircuitInject",
    )
}
