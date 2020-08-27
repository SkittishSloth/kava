import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
  java
  id("org.jetbrains.kotlin.jvm") version "1.3.72" apply false
  id("com.github.ben-manes.versions") version "0.28.0"
}

subprojects {
  apply<JavaPlugin>()
  apply(plugin = "org.jetbrains.kotlin.jvm")

  repositories {
    jcenter()
    mavenCentral()
  }

  dependencies {
    val kotest_version = "4.1.1"
    val spek_version = "2.0.12"
    
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("io.github.microutils:kotlin-logging:1.8.0.1")
    implementation("ch.qos.logback:logback-classic:1.2.3")
    
    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    // Use the Kotlin JUnit integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
    
    testImplementation("io.kotest:kotest-runner-junit5-jvm:$kotest_version") // for kotest framework
    testImplementation("io.kotest:kotest-assertions-core-jvm:$kotest_version") // for kotest core jvm assertions
    testImplementation("io.kotest:kotest-property-jvm:$kotest_version") // for kotest property test
    
    testImplementation("org.spekframework.spek2:spek-dsl-jvm:$spek_version")
    testRuntimeOnly("org.spekframework.spek2:spek-runner-junit5:$spek_version")

    testImplementation("io.strikt:strikt-core:0.26.1")
  }
  
  tasks.withType<Test> {
    useJUnitPlatform {
      includeEngines("spek2")
    }
    
    this.testLogging {
      outputs.upToDateWhen { false }
      this.showStandardStreams = true
    }
  }
}
  
fun isNonStable(version: String): Boolean {
  val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase().contains(it) }
  val regex = "^[0-9,.v-]+(-r)?$".toRegex()
  val isStable = stableKeyword || regex.matches(version)
  return isStable.not()
}

tasks.named("dependencyUpdates", DependencyUpdatesTask::class.java).configure {
  resolutionStrategy {
    componentSelection {
      all {
        if (isNonStable(candidate.version) && !isNonStable(currentVersion)) {
          reject("Release candidate")
        }
      }
    }
  }
}
