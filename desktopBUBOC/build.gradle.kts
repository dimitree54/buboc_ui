plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

kotlin {
    //jvm {}
    sourceSets {
        // val jvmMain by getting  {
        //     dependencies {
        //         implementation(compose.desktop.currentOs)
        //         implementation(project(":sharedBUBOC"))
        //     }
        // }
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
    }
}
