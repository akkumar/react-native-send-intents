# react-native-send-intents

Send Android Intents from React Native.

Specifically check for permission on ExternalStorage after Anrdoid 30.

## Installation

```sh
npm install react-native-send-intents
```

## Usage

```js
import {
  checkPermissionForExternalStorage,
  requestPermissionToManageAppAllFiles,
  requestPermissionToManageAllFiles
} from 'react-native-send-intents';

// ...
const permissionGranted = await checkPermissionForExternalStorage();
// permissionGranted is boolean - true or false 

if (!permissionGranted) {
    // If not permissionGranted - request permission
    const permissionRequestSucessful = await requestPermissionToManageAppAllFiles('com.sendintentsexample');
    // permissionRequestSuccessful is string as returned by the request to manage all app files
}
```

Or , alternatively 

```js
if (!permissionGranted) {
    // If not permissionGranted - request permission
    const permissionRequestSucessful = await requestPermissionToManageAllFiles('com.sendintentsexample');
    // permissionRequestSuccessful is string as returned by the request to manage all app files
}
```


## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
