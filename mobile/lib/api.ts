import axios from 'axios';

const API = axios.create({
  baseURL: `http://10.149.133.218:8080`,
  headers: { 'Content-Type': 'application/json' },
  timeout: 30000,
});

export const refreshClient = axios.create({
  baseURL: API.defaults.baseURL,
  headers: { 'Content-Type': 'application/json' },
  timeout: 30000,
});

export default API;
