import API from '@/lib/api';
import type { ApiResponse } from '@/lib/types';
import type { User } from '../user/types';

const getAccountProfile = async (): Promise<ApiResponse<User>> => {
  const { data } = await API.get(`/api/v1/account/profile`);
  return data;
};

export { getAccountProfile };
