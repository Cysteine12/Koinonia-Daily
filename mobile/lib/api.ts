import axios from 'axios';

const API = axios.create({
  baseURL: `http://10.13.2.152:8080`,
  headers: { 'Content-Type': 'application/json' },
  timeout: 30000,
});

export const refreshClient = axios.create({
  baseURL: API.defaults.baseURL,
  headers: { 'Content-Type': 'application/json' },
  timeout: 30000,
});

export default API;
