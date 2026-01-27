import { z } from 'zod';

const registerSchema = z.object({
  firstName: z.string({ error: 'First name is invalid' }).min(2, 'First name is invalid'),
  lastName: z.string({ error: 'Last name is invalid' }).min(2, 'Last name is invalid'),
  email: z.email({ error: 'Email is invalid' }),
  password: z.string({ error: 'Password is invalid' }).min(8, 'Password must be minimum of 8 characters'),
});

export type RegisterSchema = z.infer<typeof registerSchema>;

const loginSchema = registerSchema.pick({
  email: true,
  password: true,
});

export type LoginSchema = z.infer<typeof loginSchema>;

const verifyEmailSchema = z.object({
  email: z.email({ error: 'Email is invalid' }),
  otp: z.string({ error: 'OTP is invalid' }).length(6, 'OTP must be 6 digits'),
});

export type VerifyEmailSchema = z.infer<typeof verifyEmailSchema>;

export { loginSchema, registerSchema, verifyEmailSchema };
