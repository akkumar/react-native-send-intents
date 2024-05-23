# react-native-send-intents

* Send Android Intents from React Native code.

* Specifically check for permission on ExternalStorage after Anrdoid 30.

From Android 30, couple of intents and their flows are necessary to deal with 
external storage. 

** `ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION` and
**  `ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION` settings


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
    // permissionRequestSuccessful is string as returned by the request to 
    // start the intent ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
}
```

Or , alternatively 

```js
if (!permissionGranted) {
    // If not permissionGranted - request permission
    const permissionRequestSucessful = await requestPermissionToManageAllFiles('com.sendintentsexample');
    // permissionRequestSuccessful is string as returned by the request to 
    // start the intent ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
}
```


## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
