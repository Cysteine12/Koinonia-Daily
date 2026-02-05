import API from '@/lib/api';
import type { LoginSchema, LogoutSchema, RegisterSchema } from './schema';

const register = async (payload: RegisterSchema) => {
  const { data } = await API.post(`/api/auth/register`, payload);
  return data;
};

const login = async (payload: LoginSchema) => {
  const { data } = await API.post(`/api/auth/login`, payload);
  return data;
};

const logout = async (payload: LogoutSchema) => {
  const { data } = await API.post(`/api/auth/logout`, payload);
  return data;
};

export { login, logout, register };

