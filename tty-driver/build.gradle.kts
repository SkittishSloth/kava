plugins {
    application
}

dependencies {
    implementation(project(":tty"))
}

application {
    // Define the main class for the application.
    mainClassName = "kava.tty.driver.AppKt"
}
