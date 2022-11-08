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

## Testing
### Running an IDE instance
1. Run `earthly +ide`. This will open an IDE instance with the plugin installed (requires `java` and `gradle` installed locally);
2. Create a new Java project
3. Create a new file named `Earthfile` and paste the contents of your choice there.

### Running jUnit tests
1. Run `earthly +test`
