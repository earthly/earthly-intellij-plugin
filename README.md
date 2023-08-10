# earthly-intellij-plugin

IntelliJ plugin for Earthly language support. 

Check the currently released versions [on the JetBrains Marketplace](https://plugins.jetbrains.com/plugin/20392-earthly/versions).

## Features
- [x] Syntax highlighting for Earthfiles
- [ ] Code completion
- [ ] Formatter
- [ ] Commenter
- [ ] Go To Symbol Contributor
- [ ] ... Let us know!

![Darcula theme](documentation/darcula.png)
![Light theme](documentation/light.png)

## Requirements

This plugin is dependent on https://github.com/earthly/earthfile-grammar for syntax.

This project has three branches for building intellij plugins. They are:
- py
- go
- main

They should be both built and released in the same order to avoid any odd dependency issues.


## Building

The following command generates a `earthly-intellij-plugin-<version>.zip` package in the current directory:
```
earthly +dist [--version=<version>]
```

## Signing (requires `earthly-technologies` org membership)
The following command generates a `earthly-intellij-plugin-signed-<version>.zip` package in the current directory:
```
earthly +sign [--version=<version>]
```

## Publishing (requires `earthly-technologies` org membership)
The following command generates builds, signs and publish the plugin to the [IntelliJ Marketplace](https://plugins.jetbrains.com/plugin/20392-earthly):
```
earthly --push +publish --version=<version>
```

## Testing
```
earthly +ide
```