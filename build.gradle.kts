import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	java
	id("java-library")
	id("org.springframework.boot") version "3.4.0"
	id("io.spring.dependency-management") version "1.1.6"
	kotlin("jvm") version "1.9.22"
	kotlin("plugin.spring") version "1.9.22"
}

group = "study.haxagonal"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		// 컴파일, 소스 코드 호환성, 바이트 코드 호환성 21 버전으로 설정
		languageVersion = JavaLanguageVersion.of(21)
		sourceCompatibility = JavaVersion.VERSION_21
		targetCompatibility = JavaVersion.VERSION_21
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
	configureEach {
		// 명시된 모듈 제외
		exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
	}
}


repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

	// logging
	implementation("org.springframework.boot:spring-boot-starter-log4j2")

	// database - postgresql
	runtimeOnly("org.postgresql:postgresql:42.7.4")
	// test database - inmemory
	testRuntimeOnly("com.h2database:h2")

	// flyway - database migration for postgresql
	implementation("org.flywaydb:flyway-database-postgresql")

	// lombok
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")

	// mapstruct
	implementation("org.mapstruct:mapstruct:1.6.3")
	annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")
	annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")

	// test
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation(kotlin("test"))
	testImplementation("io.kotest:kotest-runner-junit5:5.9.1")
	testImplementation("org.jetbrains.kotlin:kotlin-reflect")
	testImplementation("io.mockk:mockk:1.13.12")
}


// gradle 테스트 작업에 대한 설정 (Junit)
tasks.withType<Test> {
	useJUnitPlatform()
}

// 미리보기 기능, mapstruct 설정
tasks.withType<JavaCompile> {
	options.compilerArgs.addAll(listOf("--enable-preview", "-Amapstruct.defaultComponentModel=spring"))
}

// 테스트 실행 시 미리보기 기능 ON
tasks.withType<Test> {
	useJUnitPlatform()
	jvmArgs("--enable-preview")
}

// bootRun 실행 시 미리보기 기능 ON
tasks.named<JavaExec>("bootRun") {
	jvmArgs("--enable-preview")
}

// Kotlin 코드를 Java 21 버전으로 컴파일
tasks.withType<KotlinCompile> {
	compilerOptions {
		jvmTarget = JvmTarget.JVM_21
	}
}

