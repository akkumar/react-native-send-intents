import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-send-intents' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const SendIntents = NativeModules.SendIntents
  ? NativeModules.SendIntents
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

export function doCheckPermissionForExternalStorage(): Promise<boolean> {
  return SendIntents.checkPermissionForExternalStorage();
}

export function doRequestPermissionForExternalStorage(
  packageName: string
): Promise<boolean> {
  return SendIntents.requestPermissionForExternalStorage(packageName);
}

export function checkPermissionForExternalStorage(): Promise<boolean> {
  return doCheckPermissionForExternalStorage();
}

export function requestPermissionForExternalStorage(
  packageName: string
): Promise<boolean> {
  return doRequestPermissionForExternalStorage(packageName);
}
