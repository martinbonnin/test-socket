plugins {
  id("org.jetbrains.kotlin.multiplatform").version("1.9.20-RC")
}

kotlin {
  macosArm64()
  jvm()

  sourceSets {
    getByName("commonTest") {
      dependencies {
        implementation("io.ktor:ktor-network:2.3.5")
        implementation(kotlin("test"))
      }
    }
  }
}

