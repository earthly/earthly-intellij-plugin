# earthly-intellij-plugin

IntelliJ plugin for Earthly language support. 

## Features
- [x] Syntax highlighting for Earthfiles
- [ ] Code completion
- [ ] Formatter
- [ ] Commenter
- [ ] Go To Symbol Contributor
- [ ] ... Let us know!

![Darcula theme](documentation/darcula.png)
![Light theme](documentation/light.png)

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
