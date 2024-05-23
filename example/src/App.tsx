import { useState, useEffect } from 'react';
import * as React from 'react';
import { StyleSheet, View, Text, Button } from 'react-native';
import {
  checkPermissionForExternalStorage,
  requestPermissionToManageAppAllFiles,
} from 'react-native-send-intents';

export default function App() {
  const [granted, setGranted] = useState(false);

  const updatePermissionsStatus = () => {
    checkPermissionForExternalStorage()
      .then((value) => {
        setGranted(value);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  useEffect(() => {
    updatePermissionsStatus();
  }, []);

  const onPress = () => {
    console.log('onPress');
    requestPermissionToManageAppAllFiles('com.sendintentsexample')
      .then((value) => {
        console.log('granted permission', value);
      })
      .catch((err) => {
        console.log('error granting permission', err);
        updatePermissionsStatus();
      });
  };

  return (
    <View style={styles.container}>
      <Text>Permission Granted: {granted.toString()}</Text>
      {!granted && <Button onPress={onPress} title="Grant Permission" />}
      {granted && <Button onPress={onPress} title="Revoke Permission" />}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
