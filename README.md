# project-reserve

The versioned jar can then be found in the `build/libs` folder.
An executable program which allows a user to perform the reserve lottery process outlined in an April 2020 [paper](http://dx.doi.org/10.2139/ssrn.3569307) circulated by Pathak et al. to allocate the anti-viral drug remdesivir.
## Requirements
* [JDK 8](https://aws.amazon.com/corretto/)
* [IntelliJ IDEA](https://www.jetbrains.com/idea/) (not really a requirement, but will make development easier) For instructions on setting up Live Reloading in IntelliJ, see [INTELLIJ.md](INTELLIJ.md)

## Building
This project uses [Gradle](https://docs.gradle.org/current/userguide/userguide.html).
A [Gradle Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html) 
is included at `gradlew` (or `gradlew.bat` for Windows). IntelliJ IDEA will recognize this as a Gradle project and
auto-import tasks to the Gradle Tool Window.  To run the following commands in IntelliJ IDEA, search for the task name
under 'Tasks' in the Gradle Tool Window.

#### Build jar
To build a jar with packaged binaries: `./gradlew clean jar` 

#### Run tests
To run JUnit tests: `./gradlew test` 

## Contributing
Please see [CONTRIBUTING.md](CONTRIBUTING.md) for details on how to submit pull requests and log issues.

## Project Dependencies
* [Kotlin](https://kotlinlang.org/)
* [Gradle](https://gradle.org/)

## MAINTAINERS
* Jack Ferguson (john.everett.ferguson@gmail.com)
* Chetan Patel (cpatel@mit.edu)
