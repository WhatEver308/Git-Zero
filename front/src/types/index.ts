export interface User {
  iUserId: number;
  strUserName: string;
  strEmail: string;
  strPhone: string;
  strUserCategory: string;
  strUserGender: 'Male' | 'Female' | 'Unspecified';
}

// export interface Merchant {
//   id: string;
//   name: string;
//   logo: string;
//   coverImage: string;
//   cuisine: string[];
//   rating: number;
//   deliveryTime: number;
//   deliveryFee: number;
//   minOrder: number;
//   distance: number;
//   promotions: Promotion[];
//   isNew?: boolean;
//   isFavorite?: boolean;
//   averagePrice: number;
//   // Merchant-specific fields
//   email?: string;
//   phone?: string;
//   address?: string;
//   description?: string;
//   isActive?: boolean;
// }
export interface Merchant {
  iMerchantId: number;
  strMerchantName: string;
  strLogo_url: string;
  strCoverImage_url: string;
  strMerchantCategory: string;
  cuisine: MenuItem[];
  dRating: number;
  tDeliveryTime: number;
  dDeliveryFee: number;
  dMinOder: number;
  dAveragePrice: number;
}

export interface Address {
  iAddressId: number | null;
  strRecipientName: string;
  strPhone: string;
  strProvince: string;
  strCity: string;
  strDistrict: string;
  strAddress: string;
  bDefault: boolean;
}

export interface UserSettings {
  notifications: {
    email: boolean;
    sms: boolean;
    promotions: boolean;
    orderUpdates: boolean;
  };
  privacy: {
    showProfile: boolean;
    showOrders: boolean;
    showReviews: boolean;
  };
  security: {
    twoFactorEnabled: boolean;
    lastPasswordChange: string;
  };
}

export interface Promotion {
  id: string;
  type: 'discount' | 'gift' | 'freeDelivery';
  description: string;
}

// export interface MenuItem {
//   id: string;
//   name: string;
//   description: string;
//   price: number;
//   image: string;
//   category: string;
//   isSpicy?: boolean;
//   isPopular?: boolean;
//   isVegetarian?: boolean;
// }
export interface MenuItem {
  iMenuItemId: number;
  strMenuItemName: string;
  strMenuItemDescription: string;
  dMenuItemPrice: number;
  strImage_url: string;
  striMenuItemCategory: string;
}

export interface CartItem extends MenuItem {
  quantity: number;
  notes?: string;
}

export interface OrderItemList {
  MenuItem: MenuItem;
  iQuantity: number;
}

export interface OrderRequest {
  address: Address;
  iMerchantId: number;
  iUserId: number;
  orderItemList: OrderItemList[];
}

export interface OrderItem {
  iMenuItemId: number;
  strMenuItemName: string;
  iQuantity: number;
  dPrice: number;
  strNotes?: string;
}

export interface Order {
  iOrderId: number;
  iMerchantId: number;
  iUserId: number;
  OrderItemList: OrderItemList[];
  dTotalPrice: number;
  dDeliveryFee: number;
  strStatus: 'Pending' | 'Confirmed' | 'Preparing' | 'Delivering' | 'Delivered' | 'Cancelled';
  strCreatedAt: string;
  objAddress: Address;
}

export interface MerchantFullData {
  iMerchantId: string;
  strMerchantName: string;
  strLogo_url: string;
  strCoverImage_url: string;
  strMerchantCategory: string;// 枚举类型，可扩展

  cuisine: CuisineItem[];           // 新增：菜品数组
  dstrRating: number;               // 新增：评分
  tDeliveryTime: number;            // 新增：送达时间
  dDeliveryFee: number;             // 新增：配送费
  dMinOder: number;                 // 新增：起送价
  dAveragePrice: number;            // 新增：人均价格

}
export interface CuisineItem  {
  iMenuItemId: string;
  strMenuItemName: string;
  strMenuItemDescription: string;
  dMenuItemPrice: number;
  strImage_url: string;
  striMenuItemCategory: string;
  quantity: number;
}
export interface Restaurant {
  iMerchantId: string;
  strMerchantName: string;
  strMerchantCategory: string;
  strLogo_url: string;
  strCoverImage_url: string;
}
export interface vecRestauantsArray {
  vecRestauantsArray: Restaurant[];
}