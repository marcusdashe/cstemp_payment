import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.0.2"
	id("io.spring.dependency-management") version "1.1.0"
	kotlin("jvm") version "1.7.22"
	kotlin("plugin.spring") version "1.7.22"
	kotlin("plugin.jpa") version "1.7.22"
}

group = "org.cstemp"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-mail")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.springframework.session:spring-session-core")
	implementation("com.seerbit:seerbit-java-api:1.0")
//	implementation("lib/seerbit-java-api-v2-1.0.1.jar")
//  https://mvnrepository.com/artifact/com.seerbit/seerbit-java-api
//	implementation("com.seerbit:seerbit-java-api:1.0")

	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
//	runtimeOnly(files("lib/seerbit-java-api-v2-1.0.1.jar"))
//	runtimeOnly(fileTree("lib"){ include("*.jar")})
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	implementation(files("lib/seerbit-java-api-v2-1.0.1.jar"))
//	implementation fileTree("lib"){ include("*.jar")}
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
