# Axiom

[![GitHub Actions Workflow Status](https://img.shields.io/github/actions/workflow/status/Seijii-Dev/Axiom/ci.yml?branch=main&style=for-the-badge&logo=android&link=https%3A%2F%2Fgithub.com%2FSeijii-Dev%2FAxiom%2Factions%2Fworkflows%2Fci.yml)](https://github.com/Seijii-Dev/Axiom/actions/workflows/ci.yml)
[![License](https://img.shields.io/github/license/Seijii-Dev/Axiom?style=for-the-badge&link=LICENSE)](LICENSE)
[![Discord](https://img.shields.io/badge/Discord-5865F2?style=for-the-badge&logo=discord&logoColor=white&link=https%3A%2F%2Fdiscord.gg%2FgTHQTPHNaT)](https://discord.gg/gTHQTPHNaT)
[![Telegram](https://img.shields.io/badge/Telegram-26A5E4?style=for-the-badge&logo=telegram&logoColor=white)](https://t.me/axiom_editor)

Axiom is a modern code editor for Android built with Jetpack Compose and Material 3 Expressive.

## Screenshots

<p align="center">
  <img src="images/screenshot_1.jpg" width="32%" alt="Screenshot 1" />
  <img src="images/screenshot_2.jpg" width="32%" alt="Screenshot 2" />
  <img src="images/screenshot_3.jpg" width="32%" alt="Screenshot 3" />
</p>

## Download

Download the latest release from the [Releases](https://github.com/Seijii-Dev/Axiom/releases/latest) page.

## Building

```bash
git clone --recurse-submodules https://github.com/Seijii-Dev/Axiom.git
cd axiom
./gradlew prepareTreeSitter
./gradlew assembleDebug
```

## Requirements

* Android 9.0 (API 28) or later
* Android Studio Quail or newer
* Android SDK and NDK

## Status

Axiom is under active development. Features and APIs may change between releases.

## Contributing

Contributions, bug reports, and feature requests are welcome.

* Issues: https://github.com/Seijii-Dev/Axiom/issues
* Discussions: https://github.com/Seijii-Dev/Axiom/discussions

## Translations

Help translate Axiom! Visit [Weblate](https://hosted.weblate.org/engage/axiom/) to get started.

[![Translation status](https://hosted.weblate.org/widget/axiom/translations/matrix-blue.svg)](https://hosted.weblate.org/engage/axiom/)

## License

Licensed under the GNU General Public License v3.0 (GPL-3.0). See [LICENSE](LICENSE) for details.
