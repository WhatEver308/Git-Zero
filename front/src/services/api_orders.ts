import axiosInstance from "../utils/axiosInstance";
import { Address } from '../types';

export interface OrderRequest {
  iUserId: number;
  iMerchantId: number;
  address: Address;
  orderItemList: Array<{
    iMenuItemId: number;
    iQuantity: number;
  }>;
}

export interface OrderResponse {
  order_id: string;
}

class ApiError extends Error {
  constructor(message: string) {
    super(message);
    this.name = 'ApiError';
  }
}

export const createOrder = async (orderData: OrderRequest): Promise<OrderResponse> => {
  try {
    const response = await axiosInstance.post<OrderResponse>('/api/orders', orderData);
    return response.data;
  } catch (error: any) {
    throw new ApiError(error.response?.data?.message || '创建订单失败');
  }
};
