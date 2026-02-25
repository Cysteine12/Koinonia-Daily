export const UserRole = {
  USER: 'USER',
  ADMIN: 'ADMIN',
} as const;

export type User = {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  photoUrl: string | null;
  role: (typeof UserRole)[keyof typeof UserRole];
  createdAt: Date;
  updatedAt: Date;
};
