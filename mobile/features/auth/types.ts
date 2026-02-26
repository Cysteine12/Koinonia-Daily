export const TokenType = {
  ACCESS_TOKEN: 'accessToken',
  REFRESH_TOKEN: 'refreshToken',
} as const;

export type LoginResponse = {
  accessToken: string;
  refreshToken: string;
};
