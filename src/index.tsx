import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-send-intents' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go. This is plain old React Native\n';

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

export function checkPermissionForExternalStorage(): Promise<boolean> {
  return SendIntents.checkPermissionForExternalStorage();
}

export function requestPermissionToManageAppAllFiles(
  packageName: string
): Promise<string> {
  return SendIntents.requestPermissionToManageAppAllFiles(packageName);
}

export function requestPermissionToManageAllFiles(
  packageName: string
): Promise<string> {
  return SendIntents.requestPermissionToManageAllFiles(packageName);
}

export function sendIntent(
  action: string,
  uriString: string,
  categories: string[],
  data: string,
  activityCode: number
): Promise<string> {
  return SendIntents.sendIntent(
    action,
    uriString,
    categories,
    data,
    activityCode
  );
}
