import React, { createContext, useContext, useReducer } from 'react';
import { User, Address, UserSettings } from '../types';

interface UserState {
  user: User | null;
  isAuthenticated: boolean;
}

interface UserContextType {
  state: UserState;
  addresses: Address[];
  updateProfile: (updates: Partial<User>) => void;
  addAddress: (address: Omit<Address, 'iAddressId'>) => void;
  updateAddress: (id: number, updates: Partial<Address>) => void;
  deleteAddress: (id: number) => void;
  setDefaultAddress: (id: number) => void;
  updateSettings: (settings: Partial<UserSettings>) => void;
  logout: () => void;
}

const __UserContext = createContext<UserContextType | undefined>(undefined);

type UserAction =
  | { type: 'UPDATE_PROFILE'; payload: Partial<User> }
  | { type: 'ADD_ADDRESS'; payload: Address }
  | { type: 'UPDATE_ADDRESS'; payload: { id: number; updates: Partial<Address> } }
  | { type: 'DELETE_ADDRESS'; payload: number }
  | { type: 'SET_DEFAULT_ADDRESS'; payload: number }
  | { type: 'UPDATE_SETTINGS'; payload: Partial<UserSettings> }
  | { type: 'LOGOUT' };

const userReducer = (state: UserState, action: UserAction): UserState => {
  if (!state.user) return state;

  switch (action.type) {
    case 'UPDATE_PROFILE':
      return {
        ...state,
        user: { ...state.user, ...action.payload }
      };

    case 'ADD_ADDRESS':
      if ((state.user as any).addresses.length >= 10) {
        console.error('Maximum address limit reached');
        return state;
      }
      return {
        ...state,
        user: {
          ...state.user,
          addresses: [...(state.user as any).addresses, action.payload]
        }
      };

    case 'UPDATE_ADDRESS':
      return {
        ...state,
        user: {
          ...state.user,
          addresses: (state.user as any).addresses.map((addr: Address) =>
            addr.iAddressId === action.payload.id
              ? { ...addr, ...action.payload.updates }
              : addr
          )
        }
      };

    case 'DELETE_ADDRESS':
      return {
        ...state,
        user: {
          ...state.user,
          addresses: (state.user as any).addresses.filter((addr: Address) => addr.iAddressId !== action.payload)
        }
      };

    case 'SET_DEFAULT_ADDRESS':
      return {
        ...state,
        user: {
          ...state.user,
          addresses: (state.user as any).addresses.map((addr: Address) => ({
            ...addr,
            bDefault: addr.iAddressId === action.payload
          }))
        }
      };

    case 'UPDATE_SETTINGS':
      return {
        ...state,
        user: {
          ...state.user,
          settings: {
            ...(state.user as any).settings,
            ...action.payload
          }
        }
      };

    case 'LOGOUT':
      return {
        user: null,
        isAuthenticated: false
      };

    default:
      return state;
  }
};

export const UserProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [state, dispatch] = useReducer(userReducer, {
    user: mockUser,
    isAuthenticated: true
  });

  const updateProfile = (updates: Partial<User>) => {
    dispatch({ type: 'UPDATE_PROFILE', payload: updates });
  };

  const addAddress = (address: Omit<Address, 'iAddressId'>) => {
    const newAddress: Address = {
      ...address,
      iAddressId: Date.now()
    };
    dispatch({ type: 'ADD_ADDRESS', payload: newAddress });
  };

  const updateAddress = (id: number, updates: Partial<Address>) => {
    dispatch({ type: 'UPDATE_ADDRESS', payload: { id, updates } });
  };

  const deleteAddress = (id: number) => {
    dispatch({ type: 'DELETE_ADDRESS', payload: id });
  };

  const setDefaultAddress = (id: number) => {
    dispatch({ type: 'SET_DEFAULT_ADDRESS', payload: id });
  };

  const updateSettings = (settings: Partial<UserSettings>) => {
    dispatch({ type: 'UPDATE_SETTINGS', payload: settings });
  };

  const logout = () => {
    dispatch({ type: 'LOGOUT' });
  };

  return (
    <__UserContext.Provider
      value={{
        state,
        updateProfile,
        addAddress,
        updateAddress,
        deleteAddress,
        setDefaultAddress,
        updateSettings,
        logout
      }}
    >
      {children}
    </__UserContext.Provider>
  );
};

export const useUser = () => {
  const context = useContext(__UserContext);
  if (context === undefined) {
    throw new Error('useUser must be used within a UserProvider');
  }
  return context;
};