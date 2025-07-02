import Cookies from 'js-cookie';
import { Merchant, MenuItem, Order, OrderRequest } from '../types';
import axiosInstance from "../utils/axiosInstance.ts";
import editMenuItemModal from "../components/merchant/EditMenuItemModal.tsx";

export interface MerchantAuthResponse {
  success: boolean;
  data: {
    merchant: Merchant;
    token: string;
  };
  message?: string;
}

export interface MerchantLoginCredentials {
  strUserName: string; // email or phone
  strPassword: string;
}

// used for PUT /api/merchants/menu/{merchant_id}/{item_id}
export interface EditMenuItem extends MenuItem {
  strMenuItemName: string;
  strMenuItemDescription: string;
  dMenuItemPrice: number;
  strImage_url: string;
  striMenuItemCategory: string;
}

export interface MerchantLoginResponse {
  merchant_info: {
    merchant_name: string;
    merchant_id: string;
  };
  token: string;
}

export interface MerchantRegisterData {
  strMerchantName: string;
  strPassword: string;
  strPhone: string;
}

export interface MerchantRegisterResponse {
  merchant_id: number;
}

class ApiError extends Error {
  constructor(message: string) {
    super(message);
    this.name = 'ApiError';
  }
}

// Event system for real-time updates
type EventCallback = (data: any) => void;
const eventListeners: Record<string, EventCallback[]> = {};

const emit = (event: string, data: any) => {
  if (eventListeners[event]) {
    eventListeners[event].forEach(callback => callback(data));
  }
};

const on = (event: string, callback: EventCallback) => {
  if (!eventListeners[event]) {
    eventListeners[event] = [];
  }
  eventListeners[event].push(callback);
};

const off = (event: string, callback: EventCallback) => {
  if (eventListeners[event]) {
    eventListeners[event] = eventListeners[event].filter(cb => cb !== callback);
  }
};

// Auth functions
export const merchantLogin = async (credentials: MerchantLoginCredentials): Promise<MerchantAuthResponse> => {
  try {
    if (!credentials.strUserName || !credentials.strPassword) {
      throw new ApiError('请输入账户和密码');
    }

    Cookies.remove('auth_token');
    Cookies.remove('merchant_auth_token');
    Cookies.remove('merchant_role_type');
    Cookies.remove('current_merchant_id');
    Cookies.remove('user_auth_token');
    Cookies.remove('current_user_id');
    console.log("api merchant login call" + credentials.strUserName + credentials.strPassword);
    const merchant_login_response = await axiosInstance.post<MerchantLoginResponse>("/api/merchants/auth/login", {
      strUserName: credentials.strUserName,
      strPassword: credentials.strPassword
    });

    const merchant = merchant_login_response.data.merchant_info;
    const token = merchant_login_response.data.token;

    // Set token for subsequent requests
    Cookies.set('auth_token', token);

    // Fetch detailed merchant information
    const merchant_info_response = await axiosInstance.get<Merchant>(`/api/restaurants/${merchant.merchant_id}`);

    if (merchant && merchant_info_response.data) {
      Cookies.set('merchant_auth_token', token, { expires: 7 });
      Cookies.set('merchant_role_type', 'Merchant', { expires: 7 });
      Cookies.set('current_merchant_id', merchant.merchant_id, { expires: 7 });

      return {
        success: true,
        data: {
          merchant: merchant_info_response.data,
          token: token,
        },
      };
    }

    throw new ApiError('登录失败');
  } catch (error: any) {
    throw new ApiError(error.response?.data?.message || '账户或密码错误');
  }
};

export const merchantRegister = async (data: MerchantRegisterData): Promise<MerchantRegisterResponse> => {
  try {
    if (!data.strMerchantName || !data.strPassword || !data.strPhone) {
      throw new ApiError('请填写所有必填字段');
    }

    Cookies.remove('auth_token');
    Cookies.remove('merchant_auth_token');
    Cookies.remove('merchant_role_type');
    Cookies.remove('current_merchant_id');
    Cookies.remove('user_auth_token');
    Cookies.remove('current_user_id');
    const response = await axiosInstance.post<MerchantRegisterResponse>("/api/merchants/register", {
      strMerchantName: data.strMerchantName,
      strPassword: data.strPassword,
      strPhone: data.strPhone
    });

    return response.data;
  } catch (error: any) {
    throw new ApiError(error.response?.data?.message || '注册失败');
  }
};

export const merchantLogout = () => {
  Cookies.remove('merchant_auth_token');
  Cookies.remove('merchant_role_type');
  Cookies.remove('current_merchant_id');
  Cookies.remove('auth_token');
};

export const checkMerchantAuth = async (): Promise<MerchantAuthResponse | null> => {
  const token = Cookies.get('merchant_auth_token');
  if (!token) return null;

  try {
    const currentMerchantId = Cookies.get('current_merchant_id');
    if (!currentMerchantId) {
      merchantLogout();
      return null;
    }

    // Verify token and get merchant info
    const merchant_info_response = await axiosInstance.get<Merchant>(`/api/restaurants/${currentMerchantId}`);

    return {
      success: true,
      data: {
        merchant: merchant_info_response.data,
        token
      }
    };
  } catch (error) {
    merchantLogout();
    return null;
  }
};

// Unified API functions for both user and merchant sides
export const getMerchantById = async (merchantId: string): Promise<Merchant> => {
  try {
    const response = await axiosInstance.get<Merchant>(`/api/restaurants/${merchantId}`);
    return response.data;
  } catch (error: any) {
    throw new ApiError(error.response?.data?.message || '商家不存在');
  }
};

export const getMenuByMerchantId = async (merchantId: string): Promise<MenuItem[]> => {
  try {
    const response = await axiosInstance.get<Merchant>(`/api/restaurants/${merchantId}`);
    return response.data.cuisine;
  } catch (error: any) {
    throw new ApiError(error.response?.data?.message || '获取菜单失败');
  }
};

export const getAllMerchants = async (): Promise<Merchant[]> => {
  try {
    const response = await axiosInstance.get<Merchant[]>("/api/restaurants");
    return response.data;
  } catch (error: any) {
    throw new ApiError(error.response?.data?.message || '获取商家列表失败');
  }
};

// Merchant management functions with real-time updates
export const updateMerchantProfile = async (merchantId: string, updates: Partial<Merchant>): Promise<Merchant> => {
  try {
    const response = await axiosInstance.put<Merchant>(`/api/merchants/${merchantId}/update`, updates);

    // Emit event for real-time updates
    emit('merchantUpdated', { merchantId, updates, merchant: response.data });

    return response.data;
  } catch (error: any) {
    throw new ApiError(error.response?.data?.message || '更新商家信息失��');
  }
};

// Menu management functions
export const fetchMerchantMenu = async (merchantId: string): Promise<MenuItem[]> => {
  return getMenuByMerchantId(merchantId);
};

export const addMenuItem = async (merchantId: string, item: Omit<MenuItem, 'iMenuItemId'>): Promise<void> => {
  try {
    const _item : Partial<EditMenuItem> = {
      strMenuItemName: item.strMenuItemName,
      strMenuItemDescription: item.strMenuItemDescription,
      dMenuItemPrice: item.dMenuItemPrice,
      strImage_url: item.strImage_url,
      striMenuItemCategory: item.striMenuItemCategory,
    }
    await axiosInstance.post<MenuItem>(`/api/merchants/menu/${merchantId}`, _item);

    // 刷新页面以重新获取最新数据
    window.location.reload();
  } catch (error: any) {
    throw new ApiError(error.response?.data?.message || '添加菜品失败');
  }
};

export const updateMenuItem = async (merchantId: string, itemId: string, item: Partial<MenuItem>): Promise<void> => {
  try {
    const _item : Partial<EditMenuItem> = {
      strMenuItemName: item.strMenuItemName,
      strMenuItemDescription: item.strMenuItemDescription,
      dMenuItemPrice: item.dMenuItemPrice,
      strImage_url: item.strImage_url,
      striMenuItemCategory: item.striMenuItemCategory,
    }
    await axiosInstance.put(`/api/merchants/menu/${merchantId}/${itemId}`, _item);

    // 刷新页面以重新获取最新数据
    window.location.reload();
  } catch (error: any) {
    throw new ApiError(error.response?.data?.message || '更新菜品失败');
  }
};

export const deleteMenuItem = async (merchantId: string, itemId: string): Promise<void> => {
  try {
    await axiosInstance.delete(`/api/merchants/menu/${merchantId}/${itemId}`);

    // Emit event for real-time updates
    emit('menuItemDeleted', { merchantId, itemId });
  } catch (error: any) {
    throw new ApiError(error.response?.data?.message || '删除菜品失败');
  }
};


interface ordersResponse {
  arrOrders: Order[];
}

// 获取商家所有订单
export const fetchMerchantOrders = async (merchantId: string | number): Promise<Order[]> => {
  try {
    const response = await axiosInstance.get(`/api/orders/${merchantId}`);
    console.log(response.data.arrOrders);
    return response.data.arrOrders;
  } catch (error: any) {
    throw new ApiError(error.response?.data?.message || '获取商家订单失败');
  }
};

// 获取用户所有订单
export const fetchUserOrders = async (userId: string | number): Promise<Order[]> => {
  try {
    const response = await axiosInstance.get(`/api/orders/user/${userId}`);
    return response.data;
  } catch (error: any) {
    throw new ApiError(error.response?.data?.message || '获取用户订单失败');
  }
};

// 更新订单状态
export const updateOrderStatus = async (orderId: string, merchantId: string, status: Order['strStatus']): Promise<Order> => {
  try {
    const response = await axiosInstance.patch(`/api/merchants/orders/${merchantId}/${orderId}`, { status });

    // Emit event for real-time updates
    emit('orderStatusUpdated', { orderId, status, order: response.data });

    return response.data;
  } catch (error: any) {
    throw new ApiError(error.response?.data?.message || '更新订单状态失败');
  }
};

// 创建新订单
export const createOrder = async (orderData: OrderRequest): Promise<Order> => {
  try {
    const response = await axiosInstance.post('/api/orders', orderData);

    // Emit event for real-time updates
    emit('orderAdded', response.data);

    return response.data;
  } catch (error: any) {
    throw new ApiError(error.response?.data?.message || '创建订单失败');
  }
};

// 获取单个订单详情
export const getOrderById = async (orderId: string): Promise<Order> => {
  try {
    const response = await axiosInstance.get(`/api/orders/${orderId}`);
    return response.data;
  } catch (error: any) {
    throw new ApiError(error.response?.data?.message || '获取订单详情失败');
  }
};

// 取消订单
export const cancelOrder = async (orderId: string): Promise<Order> => {
  try {
    const response = await axiosInstance.put(`/api/orders/${orderId}/cancel`);

    // Emit event for real-time updates
    emit('orderStatusUpdated', { orderId, status: 'cancelled', order: response.data });

    return response.data;
  } catch (error: any) {
    throw new ApiError(error.response?.data?.message || '取消订单失败');
  }
};

// Export event system for components to use
export { on, off, emit };