import org.gradle.api.JavaVersion
import org.gradle.api.tasks.testing.Test

plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.kotlin.serialization)
  alias(libs.plugins.ksp)
  alias(libs.plugins.room)
  alias(libs.plugins.hilt)
}

ksp {
  arg("room.generateKotlin", "true")
}

android {
  namespace = "alexx.rizz.mytodo"
  compileSdk = libs.versions.compileSdk.get().toInt()

  defaultConfig {
    applicationId = "alexx.rizz.mytodo"
    minSdk = libs.versions.minSdk.get().toInt()
    targetSdk = libs.versions.targetSdk.get().toInt()
    versionCode = 1
    versionName = "0.0.1"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    buildConfigField("int", "VERSION_CODE", "$versionCode")
    buildConfigField("String", "VERSION_NAME", "\"${versionName}\"")
  }

  buildTypes {
    release {
      isMinifyEnabled = true
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
  kotlinOptions {
    jvmTarget = "17"
    freeCompilerArgs += "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
    // раскомментировать для генерации отчетов о compose stability (собирать следует release)
    // freeCompilerArgs += listOf("-P", "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=" + project.projectDir.toPath().toString() + "/build/compose_metrics")
    // freeCompilerArgs += listOf("-P", "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=" + project.projectDir.toPath().toString() + "/build/compose_metrics")
  }
  testOptions {
    unitTests {
      isIncludeAndroidResources = true
      all {
        it.systemProperties["robolectric.logging.enabled"] = "true"
      }
    }
  }
  buildFeatures {
    compose = true
    buildConfig = true
  }
  room {
    schemaDirectory("$projectDir/schemas")
  }
}

dependencies {
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.viewmodel.ktx)
  implementation(libs.androidx.lifecycle.viewmodel.compose)
  implementation(libs.androidx.activity.compose)
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.navigation.compose)
  implementation(libs.androidx.ui)
  implementation(libs.androidx.ui.graphics)
  implementation(libs.androidx.ui.tooling.preview)
  implementation(libs.androidx.material3)
  implementation(libs.reorderable)
  implementation(libs.kotlinx.serialization.json)
  implementation(libs.kotlinx.coroutines)

  debugImplementation(libs.androidx.ui.tooling)
  debugImplementation(libs.androidx.ui.test.manifest)

  // *** testing
  testImplementation(libs.junit)
  testRuntimeOnly(libs.junit5.vintage.engine)
  testImplementation(platform(libs.junit5.bom))
  testImplementation(libs.junit5.jupiter)
  testImplementation(libs.junit5.pioneer)
  testImplementation(libs.kotlinx.coroutines.test)
  testImplementation(libs.kotest.assertions.core)
  testImplementation(libs.kotlin.fixture)
  testImplementation(libs.classgraph)
  testImplementation(libs.automockker)
  testImplementation(libs.androidx.test.core)
  testImplementation(libs.robolectric)
  testImplementation(libs.mockk)

  // *** logging
  implementation(libs.slf4j)
  implementation(libs.logback.android)

  // *** room
  implementation(libs.room.runtime)
  implementation(libs.room.ktx)
  ksp(libs.room.compiler)

  // *** hilt
  implementation(libs.hilt.android)
  implementation(libs.hilt.navigation.compose)
  ksp(libs.hilt.compiler)
}

tasks.withType<Test> {
  useJUnitPlatform()
}