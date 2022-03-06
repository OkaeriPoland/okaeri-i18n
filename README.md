# Okaeri Messages (okaeri-i18n)

![License](https://img.shields.io/github/license/OkaeriPoland/okaeri-i18n)
![Total lines](https://img.shields.io/tokei/lines/github/OkaeriPoland/okaeri-i18n)
![Repo size](https://img.shields.io/github/repo-size/OkaeriPoland/okaeri-i18n)
![Contributors](https://img.shields.io/github/contributors/OkaeriPoland/okaeri-i18n)
[![Discord](https://img.shields.io/discord/589089838200913930)](https://discord.gg/hASN5eX)

Simple yet powerful localization library built with blazing-fast [okaeri-placeholders](https://github.com/OkaeriPoland/okaeri-placeholders).
Part of the [okaeri-platform](https://github.com/OkaeriPoland/okaeri-platform).

## Implementations

- [configs](https://github.com/OkaeriPoland/okaeri-i18n/tree/master/configs): based on [okaeri-configs](https://github.com/OkaeriPoland/okaeri-configs) with support for accessing messages through getters (compile time key checking!)

## Example

See full examples in the [tests](https://github.com/OkaeriPoland/okaeri-i18n/tree/master/configs/src/test/java/eu/okaeri/i18ntest).

```java
// example of manual transformation to String
this.i18n.get(sender, this.messages.getExampleMessage()).apply();
// example of fields use
this.i18n.get(sender, this.messages.getExampleMessage())
    .with("name", "John")
    .with("age", 123)
    .apply();
```
