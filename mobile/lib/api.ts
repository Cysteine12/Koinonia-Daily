import axios from 'axios';

const API = axios.create({
  baseURL: process.env.EXPO_PUBLIC_API_URL,
  headers: { 'Content-Type': 'application/json' },
});

export default API;
