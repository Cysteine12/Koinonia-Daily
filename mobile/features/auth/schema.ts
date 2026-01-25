import { z } from 'zod';

const registerSchema = z.object({
  firstName: z.string({ error: 'First name is invalid' }).min(2, 'First name is invalid'),
  lastName: z.string({ error: 'Last name is invalid' }).min(2, 'Last name is invalid'),
  email: z.email({ error: 'Email is invalid' }),
  password: z.string({ error: 'Password is invalid' }).min(7, 'Password must be minimum of 7 characters'),
});

export type RegisterSchema = z.infer<typeof registerSchema>;

const loginSchema = registerSchema.pick({
  email: true,
  password: true,
});

export type LoginSchema = z.infer<typeof loginSchema>;

const verifyEmailSchema = z.object({
  email: z.email({ error: 'Email is invalid' }),
  otp: z.string().min(6).max(6),
});

export type VerifyEmailSchema = z.infer<typeof verifyEmailSchema>;

export { registerSchema, loginSchema, verifyEmailSchema };
