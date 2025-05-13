# Okaeri Messages | okaeri-configs

Based on [OkaeriPoland/okaeri-configs](https://github.com/OkaeriPoland/okaeri-configs), simple Java/POJO config library written with love and Lombok.

- `MOCI18n`: powerful messages with placeholders (recommended)
- `OCI18n`: simple messages (just strings)

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
  <artifactId>okaeri-i18n-configs</artifactId>
  <version>5.1.2</version>
</dependency>
```

### Gradle

Add repository to the `repositories` section:

```groovy
maven { url "https://storehouse.okaeri.eu/repository/maven-public/" }
```

Add dependency to the `maven` section:

```groovy
implementation 'eu.okaeri:okaeri-i18n-configs:5.1.2'
```

## Example

It is recommended to get familiar with [use and configurer (format provider) concept](https://github.com/OkaeriPoland/okaeri-configs#usage) of the okaeri-configs first. For the example usage refer to
the [tests](https://github.com/OkaeriPoland/okaeri-i18n/tree/master/configs/src/test/java/eu/okaeri/i18ntest).
