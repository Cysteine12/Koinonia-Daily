import * as SecureStore from 'expo-secure-store'

export const getSecure = async (key: string) => {
  return await SecureStore.getItemAsync(key)
}

export const saveSecure = async (key: string, value: string) => {
  await SecureStore.setItemAsync(key, value)
}

export const deleteSecure = async (key: string) => {
  await SecureStore.deleteItemAsync(key)
}
