import axios from 'axios';

const API = axios.create({
  baseURL: process.env.EXPO_PUBLIC_API_URL,
  headers: { 'Content-Type': 'application/json' },
  timeout: 30000,
});

export const refreshClient = axios.create({
  baseURL: API.defaults.baseURL,
  headers: { 'Content-Type': 'application/json' },
  timeout: 30000,
})

export default API;
