plugins {
    `java-library`
}

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation("org.lwjgl:lwjgl:3.3.1")
    implementation("org.lwjgl:lwjgl-glfw:3.3.1")
    implementation("org.lwjgl:lwjgl-opengl:3.3.1")
    implementation("org.joml:joml:1.10.5")
    implementation("de.javagl:obj:0.4.0")
    implementation("org.slf4j:slf4j-api:1.7.30")
    runtimeOnly("ch.qos.logback:logback-classic:1.2.3")

    runtimeOnly("org.lwjgl:lwjgl:3.3.1:natives-windows")
    runtimeOnly("org.lwjgl:lwjgl-glfw:3.3.1:natives-windows")
    runtimeOnly("org.lwjgl:lwjgl-opengl:3.3.1:natives-windows")
    runtimeOnly("org.lwjgl:lwjgl:3.3.1:natives-linux")
    runtimeOnly("org.lwjgl:lwjgl-glfw:3.3.1:natives-linux")
    runtimeOnly("org.lwjgl:lwjgl-opengl:3.3.1:natives-linux")
    runtimeOnly("org.lwjgl:lwjgl:3.3.1:natives-macos")
    runtimeOnly("org.lwjgl:lwjgl-glfw:3.3.1:natives-macos")
    runtimeOnly("org.lwjgl:lwjgl-opengl:3.3.1:natives-macos")
    
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
}
