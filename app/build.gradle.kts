import org.gradle.api.JavaVersion

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
  implementation(libs.kotlin.serialization.json)
  implementation(libs.hilt.navigation.compose)
  implementation(libs.reorderable)

  testImplementation(libs.junit)
  // androidTestImplementation(platform(libs.androidx.compose.bom))
  debugImplementation(libs.androidx.ui.tooling)
  debugImplementation(libs.androidx.ui.test.manifest)

  // *** logging
  implementation(libs.slf4j)
  implementation(libs.logback.android)

  // *** room
  implementation(libs.room.runtime)
  implementation(libs.room.ktx)
  ksp(libs.room.compiler)

  // *** hilt
  implementation(libs.hilt.android)
  ksp(libs.hilt.compiler)
}