import API from '@/lib/api';
import { LoginSchema, RegisterSchema } from './schema';

const register = async (payload: RegisterSchema) => {
  const { data } = await API.post(`/api/auth/register`, payload);
  return data;
};

const login = async (payload: LoginSchema) => {
  const { data } = await API.post(`/api/auth/login`, payload);
  return data;
};

const logout = async () => {
  const { data } = await API.post(`/api/auth/logout`);
  return data;
};

export { register, login, logout };
