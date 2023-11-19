import java.net.URI

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("maven-publish")
}

android {
    namespace = "com.louis993546.metro"

    compileSdk = rootProject.extra.get("compile_sdk_version") as? Int

    defaultConfig {
        minSdk = rootProject.extra.get("min_sdk_version") as? Int
        targetSdk = rootProject.extra.get("compile_sdk_version") as? Int

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = rootProject.extra.get("kotlin_compiler_version") as? String
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }

    // K2 lint crash
    lint {
        disable.add("MutableCollectionMutableState")
        disable.add("AutoboxingStateCreation")
    }
}

dependencies {
    val composeBom = platform("androidx.compose:compose-bom:${rootProject.extra.get("compose_bom_version")}")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    api("androidx.compose.ui:ui")
    api("androidx.compose.foundation:foundation")
    api("androidx.compose.ui:ui-tooling")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestApi("androidx.compose.ui:ui-test-junit4")
}

afterEvaluate {
    publishing {
        publications {
            register("release", MavenPublication::class.java) {
                from(components["release"])
                val number = System.getenv("GITHUB_RUN_NUMBER") ?: 9999
                groupId = "com.louis993546"
                artifactId = "metro"
                version = "0.$number.0"
            }
        }
        repositories {
            maven {
                name = "GitHubPackages"
                url = URI.create("https://maven.pkg.github.com/louis993546/Metro-Compose")
                credentials {
                    username = System.getenv("GITHUB_ACTOR")
                    password = System.getenv("GITHUB_TOKEN")
                }

            }
        }
    }
}
