export type User = {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  photoUrl: string | null;
  role: keyof typeof UserRole;
  createdAt: Date;
  updatedAt: Date;
};

export const UserRole = {
  USER: 'USER',
  ADMIN: 'ADMIN',
};
