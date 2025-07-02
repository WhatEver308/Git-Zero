import axiosInstance from "../utils/axiosInstance";

export interface PaymentQueryResponse {
  strPayment: string;
  strCreatedTime: string;
  strStatus: "Waiting" | "Done" | "Failed";
  optionalUrl?: string;
  dAmount: number;
  iRelatedOrderId: number;
}

class ApiError extends Error {
  constructor(message: string) {
    super(message);
    this.name = 'ApiError';
  }
}

export const queryPaymentStatus = async (
  merchantId: string | number,
  orderId: string | number
): Promise<PaymentQueryResponse> => {
  try {
    const response = await axiosInstance.get<PaymentQueryResponse>(
      `/api/orders/payment/query/${merchantId}/${orderId}`
    );
    return response.data;
  } catch (error: any) {
    throw new ApiError(error.response?.data?.message || '查询支付状态失败');
  }
};
