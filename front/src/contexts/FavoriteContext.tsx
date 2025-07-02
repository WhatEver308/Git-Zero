import React, { createContext, useContext, useReducer, useEffect } from 'react';
import { Merchant } from '../types';

interface FavoriteState {
  merchants: string[]; // 存储商家ID
}

interface FavoriteContextType {
  state: FavoriteState;
  toggleFavorite: (merchantId: string) => void;
  isFavorite: (merchantId: string) => boolean;
  getFavoriteMerchants: () => Merchant[];
}

const FavoriteContext = createContext<FavoriteContextType | undefined>(undefined);

// 从 localStorage 获取初始状态
const getInitialState = (): FavoriteState => {
  const savedState = localStorage.getItem('favorites');
  return savedState ? JSON.parse(savedState) : { merchants: [] };
};

type FavoriteAction =
  | { type: 'TOGGLE_FAVORITE'; payload: string }
  | { type: 'SET_FAVORITES'; payload: string[] };

const favoriteReducer = (state: FavoriteState, action: FavoriteAction): FavoriteState => {
  switch (action.type) {
    case 'TOGGLE_FAVORITE': {
      const isFavorite = (state.merchants || []).includes(action.payload);
      const newMerchants = isFavorite
        ? (state.merchants || []).filter(id => id !== action.payload)
        : [...(state.merchants || []), action.payload];
      
      // 保存到 localStorage
      localStorage.setItem('favorites', JSON.stringify({ merchants: newMerchants }));
      
      return {
        ...state,
        merchants: newMerchants
      };
    }
    case 'SET_FAVORITES': {
      return {
        ...state,
        merchants: action.payload
      };
    }
    default:
      return state;
  }
};

// 模拟商家数据，实际应该从 API 获取
const mockMerchants: Record<string, Merchant> = {
  '1': {
    iMerchantId: 1,
    strMerchantName: '金龙餐厅',
    strLogo_url: 'https://images.pexels.com/photos/941861/pexels-photo-941861.jpeg',
    strCoverImage_url: 'https://images.pexels.com/photos/1640772/pexels-photo-1640772.jpeg',
    strMerchantCategory: '中餐',
    cuisine: [],
    dRating: 4.8,
    tDeliveryTime: 25,
    dDeliveryFee: 2.99,
    dMinOder: 15,
    dAveragePrice: 35
  },
  '2': {
    iMerchantId: 2,
    strMerchantName: '意面天堂',
    strLogo_url: 'https://images.pexels.com/photos/1438672/pexels-photo-1438672.jpeg',
    strCoverImage_url: 'https://images.pexels.com/photos/1527603/pexels-photo-1527603.jpeg',
    strMerchantCategory: '意大利菜',
    cuisine: [],
    dRating: 4.5,
    tDeliveryTime: 30,
    dDeliveryFee: 1.99,
    dMinOder: 20,
    dAveragePrice: 40
  },
  '3': {
    iMerchantId: 3,
    strMerchantName: '寿司速递',
    strLogo_url: 'https://images.pexels.com/photos/359993/pexels-photo-359993.jpeg',
    strCoverImage_url: 'https://images.pexels.com/photos/858508/pexels-photo-858508.jpeg',
    strMerchantCategory: '日本料理',
    cuisine: [],
    dRating: 4.7,
    tDeliveryTime: 20,
    dDeliveryFee: 3.99,
    dMinOder: 25,
    dAveragePrice: 50
  },
  '4': {
    iMerchantId: 4,
    strMerchantName: '墨西哥风情',
    strLogo_url: 'https://images.pexels.com/photos/2087748/pexels-photo-2087748.jpeg',
    strCoverImage_url: 'https://images.pexels.com/photos/4958641/pexels-photo-4958641.jpeg',
    strMerchantCategory: '墨西哥菜',
    cuisine: [],
    dRating: 4.3,
    tDeliveryTime: 35,
    dDeliveryFee: 0,
    dMinOder: 15,
    dAveragePrice: 30
  }
};

export const FavoriteProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [state, dispatch] = useReducer(favoriteReducer, getInitialState());

  const toggleFavorite = (merchantId: string) => {
    dispatch({ type: 'TOGGLE_FAVORITE', payload: merchantId });
  };

  const isFavorite = (merchantId: string) => {
    return (state.merchants || []).includes(merchantId);
  };

  const getFavoriteMerchants = () => {
    return (state.merchants || []).map(id => mockMerchants[id]).filter(Boolean);
  };

  return (
    <FavoriteContext.Provider value={{ state, toggleFavorite, isFavorite, getFavoriteMerchants }}>
      {children}
    </FavoriteContext.Provider>
  );
};

export const useFavorite = () => {
  const context = useContext(FavoriteContext);
  if (context === undefined) {
    throw new Error('useFavorite must be used within a FavoriteProvider');
  }
  return context;
};