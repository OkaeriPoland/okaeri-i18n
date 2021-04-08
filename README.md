# Okaeri Messages (okaeri-i18n)

![License](https://img.shields.io/github/license/OkaeriPoland/okaeri-placeholders)
![Total lines](https://img.shields.io/tokei/lines/github/OkaeriPoland/okaeri-placeholders)
![Repo size](https://img.shields.io/github/repo-size/OkaeriPoland/okaeri-placeholders)
![Contributors](https://img.shields.io/github/contributors/OkaeriPoland/okaeri-placeholders)
[![Discord](https://img.shields.io/discord/589089838200913930)](https://discord.gg/hASN5eX)

Simple yet powerful localization library built with blazing-fast [okaeri-placeholders](https://github.com/OkaeriPoland/okaeri-placeholders).
Part of the [okaeri-platform](https://github.com/OkaeriPoland/okaeri-platform).

## Implementations
- [configs](https://github.com/OkaeriPoland/okaeri-i18n/tree/master/configs): based on [okaeri-configs](https://github.com/OkaeriPoland/okaeri-configs) with support for accessing messages through getters (compile time key checking!)

## Installation
### Maven
Add repository to the `repositories` section:
```xml
<repository>
    <id>okaeri-repo</id>
    <url>https://storehouse.okaeri.eu/repository/maven-public/</url>
</repository>
```
Add dependency to the `dependencies` section:
```xml
<dependency>
  <groupId>eu.okaeri</groupId>
  <artifactId>okaeri-i18n</artifactId>
  <version>1.0.0</version>
</dependency>
```
### Gradle
Add repository to the `repositories` section:
```groovy
maven { url "https://storehouse.okaeri.eu/repository/maven-public/" }
```
Add dependency to the `maven` section:
```groovy
implementation 'eu.okaeri:okaeri-i18n:1.0.0'
```

## Example

See full example in the [tests](https://github.com/OkaeriPoland/okaeri-i18n/tree/master/configs/src/test/java/eu/okaeri/i18ntest).

```java
// example of manual transformation to String
this.i18n.get(sender, this.messages.getExampleMessage()).apply()
// example of fields use
return this.i18n.get(sender, this.messages.getExampleMessage())
    .with("name", "John")
    .wih("age", 123)
    .apply();
```
