export interface ApiResponse<T = unknown> {
  success: true;
  message?: string;
  data?: T;
}
