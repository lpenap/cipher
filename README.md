
Branch  | Build status  
--------|----------------
Master  | [![Build Status](https://travis-ci.com/lpenap/cipher.svg?branch=master)](https://travis-ci.com/lpenap/cipher)
Develop | [![Build Status](https://travis-ci.com/lpenap/cipher.svg?branch=develop)](https://travis-ci.com/lpenap/cipher)

# Cipher
Java AES/RSA Cipher Application to manage encrypted text.

#### Requirements
* Java 8

#### How it works
Using an RSA Key Pair, your documents are encrypted and stored locally. The password you will generate the first time will be used to encrypt the RSA key pair using AES. Dropbox API is used to store an encrypted copy of your keys and documents in the Dropbox cloud. As long as you remember your password, you will be able to recover your documents from Dropbox if you lose your local files.

#### Installation
The application is distributed as an executable Java Jar. You can get the [latest version here](https://github.com/lpenap/cipher/releases).

## Development Info

This project has the secondary humble objective of be an academic example of software design and java technology using Springboot as the main framework.

#### Technology overview
* Java 8
* Gradle 4.5.1
* Springboot 2.0.2
* Java Swing User Interface
* JPA + Hibernate ORM
* H2 Database
* Lombok
* slf4j for logging
* jiconfont + font-awesome
* JUnit + mockito for testing
* Travis CI

#### Running From Code
* Clone this repo and execute:
```bash
./gradlew bootRun
```
* To assemble an executable Jar with all dependencies and classes, execute:
```bash
./gradlew bootJar
```

#### Configuring Dropbox
The distributed application comes with a predefined Dropbox configuration that can be used in most cases. If you want to distribute your own version, you need to register a new App using [Dropbox's App Console](https://www.dropbox.com/developers/apps)

#### Disclaimer
This Application is not endorsed by Dropbox in any way, and the software is distributed AS IS, without warranties of any kind. Use always a secondary backup for all your sensible data. In no event, will the developer be liable for any special, indirect, incidental or consequential damages in connection with, or arising from the works performed on this application, including but not limited to, loss of data, application programs, use, profits or other advantages, or any replacement of data reconstruction costs.
