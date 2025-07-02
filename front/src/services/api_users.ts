import Cookies from 'js-cookie';
import {User, Address, UserSettings, Order} from '../types';
import axiosInstance from "../utils/axiosInstance"
import {MerchantLoginResponse} from "./api_merchants.ts";
const API_BASE_URL = '/api';

export interface UserAuthResponse {
  success: boolean;
  message: string;
  data: {
    user_info: User;
    token: string;
  };
}

export interface UserLoginCredentials {
  name: string;
  password: string;
}

export interface UserRegisterData {
  strUserName: string;
  strEmail: string;
  strPhone: string;
  strPassword: string;
  strUserGender: 'Male' | 'Female' | 'Unspecified';
}

export interface UserRegisterResponse {
  user_id: number;
}

class ApiError extends Error {
  constructor(message: string) {
    super(message);
    this.name = 'ApiError';
  }
}

export interface UserLoginResponse {
  code: number;
  message: string;
  data: {
    user_info: User;
    token: string;
  };
}

// 用户认证相关 API
export const api_users_login = async (credentials: UserLoginCredentials): Promise<UserLoginResponse> => {
  if (!credentials.name || !credentials.password) {
    throw new ApiError('请输入邮箱和密码');
  }

  Cookies.remove('auth_token');
  Cookies.remove('merchant_auth_token');
  Cookies.remove('merchant_role_type');
  Cookies.remove('current_merchant_id');
  Cookies.remove('user_auth_token');
  Cookies.remove('current_user_id');
  const user_login_response = await axiosInstance.post<UserLoginResponse>("/api/users/auth/login", {
    strUserName: credentials.name,
    strPassword: credentials.password,
  });

  if (user_login_response.data){
    console.log(user_login_response.data);
    return user_login_response.data;
  }

  throw new ApiError('名称或密码错误');
};

export const api_users_register = async (data: UserRegisterData): Promise<UserRegisterResponse> => {
  try {
    if (!data.strUserName || !data.strEmail || !data.strPhone || !data.strPassword || !data.strUserGender) {
      throw new ApiError('请填写所有必填字段');
    }

    Cookies.remove('auth_token');
    Cookies.remove('merchant_auth_token');
    Cookies.remove('merchant_role_type');
    Cookies.remove('current_merchant_id');
    Cookies.remove('user_auth_token');
    Cookies.remove('current_user_id');
    const response = await axiosInstance.post<UserRegisterResponse>("/api/users/register", {
      strPassword: data.strPassword,
      strUserName: data.strUserName,
      strPhone: data.strPhone,
      strEmail: data.strEmail,
      strUserGender: data.strUserGender
    });

    return response.data;
  } catch (error: any) {
    throw new ApiError(error.response?.data?.message || '注册失败');
  }
};



export const checkAuth = async (): Promise<UserAuthResponse | null> => {
  const token = Cookies.get('auth_token');
  if (!token) return null;

  try {
    const response = await axiosInstance.get<{user_info: User, token: string}>("/api/users/auth/verify");
    return {
      success: true,
      message: 'Token valid',
      data: response.data
    };
  } catch (error) {
    return null;
  }
};

// 用��资料管理 API
export const api_users_updateProfile = async (updates: Partial<User>): Promise<User> => {
  try {
    const response = await axiosInstance.put<User>("/api/users/profile", updates);
    return response.data;
  } catch (error: any) {
    throw new ApiError(error.response?.data?.message || '更新用户资料失败');
  }
};

// 地址管理 API
interface getAddressesResponse {
  addresses: Address[];
}
export const api_users_getAddresses = async (userId: number): Promise<Address[]> => {
  try {
    const response = await axiosInstance.get<getAddressesResponse>(`/api/users/addresses/${userId}`);
    return response.data.addresses;
  } catch (error: any) {
    throw new ApiError(error.response?.data?.message || '获取地址列表失败');
  }
};

export const api_users_addAddress = async (addressId: number, address: Omit<Address, 'iAddressId'>): Promise<void> => {
  try {
    await axiosInstance.post(`/api/users/addresses/${addressId}`, address);
  } catch (error: any) {
    throw new ApiError(error.response?.data?.message || '添加地址失败');
  }
};

export const api_users_updateAddress = async (userId: number, addressId: number, updates: Partial<Address>): Promise<void> => {
  try {
    await axiosInstance.put(`/api/users/addresses/${userId}/${addressId}`, updates);
  } catch (error: any) {
    throw new ApiError(error.response?.data?.message || '更新地址失败');
  }
};

export const api_users_deleteAddress = async (userId: number, addressId: number): Promise<void> => {
  try {
    await axiosInstance.delete(`/api/users/addresses/${userId}/${addressId}`);
  } catch (error: any) {
    throw new ApiError(error.response?.data?.message || '删除地址失败');
  }
};

export const api_users_setDefaultAddress = async (userId: number, addressId: number): Promise<void> => {
  try {
    await axiosInstance.patch(`/api/users/addresses/${userId}/${addressId}/default`);
  } catch (error: any) {
    throw new ApiError(error.response?.data?.message || '设置默认地址失败');
  }
};

interface getOrdersResponse {
  orders: Order[];
}
export const api_users_getOrderList = async (userId: number): Promise<Order[]> => {
  try {
    const response = await axiosInstance.get<getOrdersResponse>(`/api/users/orders/${userId}`);
    return response.data.orders;
  } catch (error: any) {
    throw new ApiError(error.response?.data?.message || '设置默认地址失败');
  }
}