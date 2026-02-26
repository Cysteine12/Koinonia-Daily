export type SuccessResponse<T = undefined> = {
  success: true;
  message?: string;
  data?: T;
};

export type ErrorResponse = {
  success: false;
  status: number;
  error: string;
  message: string;
  path: string;
  code: string;
  timestamp: string;
};

export type ApiResponse<T = undefined> = SuccessResponse<T>;
