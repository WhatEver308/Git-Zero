import React, { createContext, useContext, useReducer, useEffect } from 'react';
import { CartItem } from '../types';

interface CartState {
  items: CartItem[];
  merchantId: number | null;
  merchantName: string | null;
}

interface CartContextType {
  state: CartState;
  addItem: (item: CartItem, merchantId: number, merchantName: string) => void;
  removeItem: (itemId: number) => void;
  updateQuantity: (itemId: number, change: number) => void;
  clearCart: () => void;
}

const CartContext = createContext<CartContextType | undefined>(undefined);

// Get initial state from localStorage
const getInitialState = (): CartState => {
  const savedState = localStorage.getItem('cart');
  return savedState ? JSON.parse(savedState) : {
    items: [],
    merchantId: null,
    merchantName: null
  };
};

type CartAction =
  | { type: 'ADD_ITEM'; payload: { item: CartItem; merchantId: number; merchantName: string } }
  | { type: 'REMOVE_ITEM'; payload: number }
  | { type: 'UPDATE_QUANTITY'; payload: { itemId: number; change: number } }
  | { type: 'CLEAR_CART' }
  | { type: 'SET_CART'; payload: CartState };

const cartReducer = (state: CartState, action: CartAction): CartState => {
  let newState: CartState;

  switch (action.type) {
    case 'ADD_ITEM': {
      // If trying to add items from a different merchant, clear the cart first
      if (state.merchantId && state.merchantId !== action.payload.merchantId) {
        newState = {
          items: [action.payload.item],
          merchantId: action.payload.merchantId,
          merchantName: action.payload.merchantName
        };
      } else {
        const existingItem = state.items.find(item => item.iMenuItemId === action.payload.item.iMenuItemId);

        if (existingItem) {
          newState = {
            ...state,
            items: state.items.map(item =>
              item.iMenuItemId === action.payload.item.iMenuItemId
                ? { ...item, quantity: item.quantity + action.payload.item.quantity }
                : item
            )
          };
        } else {
          newState = {
            items: [...state.items, action.payload.item],
            merchantId: action.payload.merchantId,
            merchantName: action.payload.merchantName
          };
        }
      }
      break;
    }

    case 'REMOVE_ITEM': {
      const newItems = state.items.filter(item => item.iMenuItemId !== action.payload);
      newState = {
        ...state,
        items: newItems,
        merchantId: newItems.length > 0 ? state.merchantId : null,
        merchantName: newItems.length > 0 ? state.merchantName : null
      };
      break;
    }

    case 'UPDATE_QUANTITY': {
      const newItems = state.items.map(item =>
        item.iMenuItemId === action.payload.itemId
          ? { ...item, quantity: Math.max(1, item.quantity + action.payload.change) }
          : item
      );
      newState = {
        ...state,
        items: newItems
      };
      break;
    }

    case 'CLEAR_CART':
      newState = {
        items: [],
        merchantId: null,
        merchantName: null
      };
      break;

    case 'SET_CART':
      newState = action.payload;
      break;

    default:
      return state;
  }

  // Save to localStorage after each change
  localStorage.setItem('cart', JSON.stringify(newState));
  return newState;
};

export const CartProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [state, dispatch] = useReducer(cartReducer, getInitialState());

  const addItem = (item: CartItem, merchantId: number, merchantName: string) => {
    dispatch({ type: 'ADD_ITEM', payload: { item, merchantId, merchantName } });
  };

  const removeItem = (itemId: number) => {
    dispatch({ type: 'REMOVE_ITEM', payload: itemId });
  };

  const updateQuantity = (itemId: number, change: number) => {
    dispatch({ type: 'UPDATE_QUANTITY', payload: { itemId, change } });
  };

  const clearCart = () => {
    dispatch({ type: 'CLEAR_CART' });
  };

  return (
    <CartContext.Provider value={{ state, addItem, removeItem, updateQuantity, clearCart }}>
      {children}
    </CartContext.Provider>
  );
};

export const useCart = () => {
  const context = useContext(CartContext);
  if (context === undefined) {
    throw new Error('useCart must be used within a CartProvider');
  }
  return context;
};