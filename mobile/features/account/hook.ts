import { useQuery } from '@tanstack/react-query';
import { getAccountProfile } from './api';

const useAccountProfile = () => {
  return useQuery({
    queryFn: getAccountProfile,
    queryKey: ['account.profile'],
  });
};

export { useAccountProfile };
