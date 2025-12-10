plugins {
	java
	id("org.springframework.boot") version "3.5.6"
	id("io.spring.dependency-management") version "1.1.7"
    kotlin("jvm")
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
description = "Demo project for Spring Boot"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencyManagement{
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2024.0.0")
    }
}
dependencies {
    implementation("com.auth0:java-jwt:4.5.0")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
	implementation(platform("io.mongock:mongock-bom:5.4.1"))
	implementation("io.mongock:mongock-springboot-v3:5.4.1")
    implementation("io.mongock:mongock-springboot-v3")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
//    implementation("org.springframework.cloud:spring-cloud-starter-config")
    implementation("io.mongock:mongodb-springdata-v4-driver")
    implementation("org.mapstruct:mapstruct:1.5.5.Final")
    implementation("com.github.vladimir-bukhtoyarov:bucket4j-core:7.0.0")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
    implementation("org.springframework.security:spring-security-crypto")
    implementation("org.springframework.security:spring-security-config")
    implementation("org.springframework.security:spring-security-web")
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}


// Boot Run application with visual code on Linux
tasks.bootRun {
    mainClass.set("com.example.demo.PosApplication")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
	