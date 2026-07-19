import * as SecureStore from 'expo-secure-store';
import AsyncStorage from '@react-native-async-storage/async-storage';

export const getSecure = async (key: string) => {
  return await SecureStore.getItemAsync(key);
};

export const saveSecure = async (key: string, value: string) => {
  await SecureStore.setItemAsync(key, value);
};

export const deleteSecure = async (key: string) => {
  await SecureStore.deleteItemAsync(key);
};

export const getStorage = async (key: string) => {
  return await AsyncStorage.getItem(key);
};

export const saveStorage = async (key: string, value: string) => {
  await AsyncStorage.setItem(key, value);
};

export const deleteStorage = async (key: string) => {
  await AsyncStorage.removeItem(key);
};
