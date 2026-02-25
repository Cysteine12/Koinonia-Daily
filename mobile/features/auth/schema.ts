import { z } from 'zod';

const registerSchema = z.object({
  firstName: z
    .string({ error: 'First name is invalid' })
    .min(2, 'First name is invalid')
    .max(50, 'Firstname length cannot exceed 50 characters'),
  lastName: z
    .string({ error: 'Last name is invalid' })
    .min(2, 'Last name is invalid')
    .max(50, 'Last name length cannot exceed 50 characters'),
  email: z.email({ error: 'Email is invalid' }).max(100, 'Email length cannot exceed 100 characters'),
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
  otp: z.string({ error: 'OTP is invalid' }).regex(/^\d{6}$/, 'OTP must be 6 digits'),
});

export type VerifyEmailSchema = z.infer<typeof verifyEmailSchema>;

const requestOtpSchema = z.object({
  email: z.email({ error: 'Email is invalid' }),
});

export type RequestOtpSchema = z.infer<typeof requestOtpSchema>;

const forgotPasswordSchema = z.object({
  email: z.email({ error: 'Email is invalid' }),
});

export type ForgotPasswordSchema = z.infer<typeof forgotPasswordSchema>;

const resetPasswordSchema = z.object({
  email: z.email({ error: 'Email is invalid' }),
  password: z.string({ error: 'New password is invalid' }).min(8, 'New password must be minimum of 8 characters'),
  otp: z.string({ error: 'OTP is invalid' }).regex(/^\d{6}$/, 'OTP must be 6 digits'),
});

export type ResetPasswordSchema = z.infer<typeof resetPasswordSchema>;

const logoutSchema = z.object({
  refreshToken: z.string({ error: 'Refresh token is required' }),
});

export type LogoutSchema = z.infer<typeof logoutSchema>;

export {
  loginSchema,
  logoutSchema,
  registerSchema,
  requestOtpSchema,
  verifyEmailSchema,
  forgotPasswordSchema,
  resetPasswordSchema,
};
