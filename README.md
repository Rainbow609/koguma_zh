# ![app icon](./.github/images/app-icon.png) Koguma

Koguma a tool that helps you create metadata for Tachiyomi's Local Source entries.

The application uses AniList for getting metadata from online, but metadata can be manually created and edited if desired.

## Features

- Read/Write `details.json` for Tachiyomi Local Source
- Search AniList for metadata

## Libraries

- Coroutines for asynchronous work
- Hilt for dependency injection
- Kotlin Serialization for parsing and writing JSON.
- Ktor for networking
- Jetpack Compose for creating the UI
- Jetpack Compose Navigation for navigating between screens
- Version Catalog for dependency management

## License

```
Copyright (C) 2022 ghostbear

This Source Code Form is subject to the terms of the Mozilla Public
License, v. 2.0. If a copy of the MPL was not distributed with this
file, You can obtain one at http://mozilla.org/MPL/2.0/.
```