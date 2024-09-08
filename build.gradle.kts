import org.gradle.kotlin.dsl.ext as ext1

plugins {
	java
	id("org.springframework.boot") version "2.7.9"
	id("io.spring.dependency-management") version "1.0.15.RELEASE"
}

group = "br.com.camarada"
version = "0.0.1-SNAPSHOT"


repositories {
	mavenCentral()
	maven {
		url = uri("https://repo.spring.io/snapshot")
	}
	maven { setUrl("https://repo.spring.io/milestone") }
	maven { setUrl("https://repo.spring.io/snapshot" )}
}

dependencies {
//	implementation("org.springframework.cloud:spring-cloud-starter-openfeign:4.1.3")
//	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	runtimeOnly("com.mysql:mysql-connector-j")
	implementation ("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-security:2.5.8")
	implementation("org.springframework.boot:spring-boot-starter")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation ("org.springframework.boot:spring-boot-starter-data-jpa")
	compileOnly ("org.projectlombok:lombok")
	annotationProcessor ("org.projectlombok:lombok")
	implementation("com.auth0:java-jwt:4.0.0")
	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	implementation("io.jsonwebtoken:jjwt-impl:0.11.5")
	implementation("io.jsonwebtoken:jjwt-jackson:0.11.5")
	testImplementation ("org.springframework.security:spring-security-test")
	implementation ("org.springframework.boot:spring-boot-starter-mail")

}

tasks.withType<Test> {
	useJUnitPlatform()
}
