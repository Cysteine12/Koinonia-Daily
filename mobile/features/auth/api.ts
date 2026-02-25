import API from '@/lib/api';
import type { ApiResponse } from '@/lib/types';
import type { LoginSchema, LogoutSchema, RegisterSchema, RequestOtpSchema, VerifyEmailSchema } from './schema';
import type { LoginResponse } from './types';

const register = async (payload: RegisterSchema): Promise<ApiResponse> => {
  const { data } = await API.post(`/api/v1/auth/register`, payload);
  return data;
};

const login = async (payload: LoginSchema): Promise<ApiResponse<LoginResponse>> => {
  const { data } = await API.post(`/api/v1/auth/login`, payload);
  return data;
};

const verifyEmail = async (payload: VerifyEmailSchema): Promise<ApiResponse> => {
  const { data } = await API.post(`/api/v1/auth/verify-email`, payload);
  return data;
};

const requestOtp = async (payload: RequestOtpSchema): Promise<ApiResponse> => {
  const { data } = await API.post(`/api/v1/auth/request-otp`, payload);
  return data;
};

const logout = async (payload: LogoutSchema): Promise<ApiResponse> => {
  const { data } = await API.post(`/api/v1/auth/logout`, payload);
  return data;
};

export { login, logout, register, requestOtp, verifyEmail };
