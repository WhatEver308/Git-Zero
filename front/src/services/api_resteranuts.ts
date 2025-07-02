import Cookies from 'js-cookie';
import { vecRestauantsArray } from '../types';
import axiosInstance from "../utils/axiosInstance"

// http://127.0.0.1:4523/m1/6381204-6077758-default/api/restaurants

export const getRestaurants = async (params?: Record<string, any>): Promise<vecRestauantsArray> => {
  try {
    console.log("api call" + JSON.stringify(params));
    const response = await axiosInstance.get('/api/restaurants', { params });
    console.log(response);
    return response.data;
  } catch (error) {
    console.error('获取餐厅列表失败:', error);
    throw new Error('获取餐厅列表失败');
  }
};
