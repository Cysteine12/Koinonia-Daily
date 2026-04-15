export const USER_ROLE = {
  USER: 'USER',
  ADMIN: 'ADMIN',
} as const;

export type UserRole = (typeof USER_ROLE)[keyof typeof USER_ROLE];

export type User = {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  photoUrl: string | null;
  role: UserRole;
  createdAt: Date;
  updatedAt: Date;
};
