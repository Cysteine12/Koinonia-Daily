export type User = {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  photoUrl: string;
  role: 'USER';
  createdAt: Date;
  updatedAt: Date;
};

export const UserRole = ['ADMIN', 'USER'];
