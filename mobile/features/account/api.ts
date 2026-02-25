import API from '@/lib/api';

const getAccountProfile = async () => {
  const { data } = await API.get(`/api/v1/account/profile`);
  return data;
};

export { getAccountProfile };

