import React, {createContext, useContext, useEffect, useState} from 'react';
import {useLocation, useNavigate} from 'react-router-dom';
import {message} from 'antd';
import {User, Address, UserSettings, Order} from '../types';
import {
    api_users_login as userLogin,
    api_users_register as userRegister,
    api_users_updateProfile,
    api_users_getAddresses,
    api_users_addAddress,
    api_users_updateAddress,
    api_users_deleteAddress,
    api_users_setDefaultAddress,
    api_users_getOrderList,
} from '../services/api_users.ts';
import Cookies from "js-cookie";

interface UserContextType {
    user: User | null;
    addresses: Address[];
    orders: Order[];
    userSettings: UserSettings | null;
    token: string | null;
    loading: boolean;
    user_login: (email: string, password: string) => Promise<void>;
    user_register: (username: string, email: string, phone: string, password: string, gender: 'Male' | 'Female' | 'Unspecified') => Promise<void>;
    user_logout: () => void;
    updateProfile: (updates: Partial<User>) => Promise<void>;
    addAddress: (address: Omit<Address, 'iAddressId'>) => Promise<void>;
    updateAddress: (id: number, updates: Partial<Address>) => Promise<void>;
    deleteAddress: (id: number) => Promise<void>;
    setDefaultAddress: (id: number) => Promise<void>;
    refreshAddresses: (userId: number) => Promise<void>; // 修正参数类型
    refreshOrders: () => Promise<void>;
}

const UserContext = createContext<UserContextType | undefined>(undefined);

const PUBLIC_ROUTES = ['/login', '/register'];

export const UserProvider: React.FC<{ children: React.ReactNode }> = ({children}) => {
    const [user, setUser] = useState<User | null>(null);
    const [addresses, setAddresses] = useState<Address[]>([]);
    const [orders, setOrders] = useState<Order[]>([]);
    const [userSettings, setUserSettings] = useState<UserSettings | null>(null);
    const [token, setToken] = useState<string | null>(null);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();
    const location = useLocation();

    // 获取用户地址列表
    const refreshAddresses = async (userId: number) => {
        try {
            const targetUserId = userId || user?.iUserId;
            if (targetUserId) {
                const addressList = await api_users_getAddresses(targetUserId);
                setAddresses(addressList);
                const orders = await api_users_getOrderList(targetUserId);
                setOrders(orders);
            }
        } catch (error) {
            console.error('Failed to fetch addresses:', error);
        }
    };

    useEffect(() => {
        const initAuth = async () => {
            try {
                // 获取持久化的token
                const auth_token = Cookies.get('auth_token');
                console.log('check_token', auth_token);

                if (!auth_token) {
                    // 无token，直接重新登录
                    console.log('No auth token found');
                    if (!PUBLIC_ROUTES.includes(location.pathname)) {
                        navigate('/login', {replace: true});
                    }
                } else {
                    // 有token
                    setToken(auth_token);

                    // 修复: 检查 cookie 是否存在且不为空
                    const userInfoCookie = Cookies.get('user_info');
                    if (userInfoCookie) {
                        try {
                            const userInfo = JSON.parse(userInfoCookie) as User;
                            setUser(userInfo);
                            refreshAddresses(userInfo.iUserId);
                        } catch (parseError) {
                            console.error('Failed to parse user info:', parseError);
                            // 如果解析失败，清除无效的 cookie
                            Cookies.remove('user_info');
                            Cookies.remove('auth_token');
                            if (!PUBLIC_ROUTES.includes(location.pathname)) {
                                navigate('/login', {replace: true});
                            }
                        }
                    }
                }
            } catch (error) {
                console.error('Auth initialization error:', error);
                if (!PUBLIC_ROUTES.includes(location.pathname)) {
                    navigate('/login', {replace: true});
                }
            } finally {
                setLoading(false);
            }
        };

        initAuth();
    }, [location.pathname, navigate]);

    const user_login = async (name: string, password: string) => {
        try {
            const response = await userLogin({name, password});

            // Update state first
            setUser(response.data.user_info);
            setToken(response.data.token);
            Cookies.set('auth_token', response.data.token);
            Cookies.set('user_info', JSON.stringify(response.data.user_info));

            message.success('登录成功');

            // 获取用户相关数据，传入用户ID
            try {
                await refreshAddresses(response.data.user_info.iUserId);
            } catch (error) {
                console.error('Failed to refresh addresses after login:', error);
            }

            // Navigate after state is updated
            setTimeout(() => {
                navigate('/', {replace: true});
                console.log("successfully jump to main page");
            }, 100);
        } catch (error) {
            message.error(error instanceof Error ? error.message : '登录失败');
            throw error;
        }
    };

    const user_register = async (username: string, email: string, phone: string, password: string, gender: 'Male' | 'Female' | 'Unspecified') => {
        try {
            const response = await userRegister({
                strUserName: username,
                strEmail: email,
                strPhone: phone,
                strPassword: password,
                strUserGender: gender
            });

            message.success('注册成功');

            // 注册成功后不需要设置用户状态，直接导航到登录页面
            // 根据 API 返回值示例，只返回 user_id，不包含完整用户信息和 token
        } catch (error) {
            message.error(error instanceof Error ? error.message : '注册失败');
            throw error;
        }
    };

    const user_logout = () => {
        console.log('user logout');
        setUser(null);
        setToken(null);
        Cookies.remove('auth_token');
        Cookies.remove('user_info');
        setAddresses([]);
        setUserSettings(null);
        message.success('已退出登录');
        navigate('/login', {replace: true});
    };

    // 更新用户资料
    const updateProfile = async (updates: Partial<User>) => {
        try {
            const updatedUser = await api_users_updateProfile(updates);
            setUser(updatedUser);
            message.success('个人信息更新成功');
        } catch (error) {
            message.error(error instanceof Error ? error.message : '更新个人信息失败');
            throw error;
        }
    };

    // 添加地址
    const addAddress = async (address: Omit<Address, 'iAddressId'>) => {
        try {
            await api_users_addAddress(user?.iUserId as number, address);
            if (user?.iUserId) {
                await refreshAddresses(user.iUserId); // 安全的条件检查
            }
            message.success('地址添加成功');
        } catch (error) {
            message.error(error instanceof Error ? error.message : '添加地址失败');
            throw error;
        }
    };

    // 更新地址
    const updateAddress = async (id: number, updates: Partial<Address>) => {
        try {
            await api_users_updateAddress(user?.iUserId as number, id, updates);
            if (user?.iUserId) {
                await refreshAddresses(user.iUserId); // 安全的条件检查
            }
            message.success('地址更新成功');
        } catch (error) {
            message.error(error instanceof Error ? error.message : '更新地址失败');
            throw error;
        }
    };

    // 删除地址
    const deleteAddress = async (id: number) => {
        try {
            await api_users_deleteAddress(user?.iUserId as number, id);
            if (user?.iUserId) {
                await refreshAddresses(user.iUserId); // 安全的条件检查
            }
            message.success('地址删除成功');
        } catch (error) {
            message.error(error instanceof Error ? error.message : '删除地址失败');
            throw error;
        }
    };

    // 设置默认地址
    const setDefaultAddress = async (id: number) => {
        try {
            await api_users_setDefaultAddress(user?.iUserId as number, id);
            if (user?.iUserId) {
                await refreshAddresses(user.iUserId); // 安全的条件检查
            }
            message.success('默认地址设置成功');
        } catch (error) {
            message.error(error instanceof Error ? error.message : '设置默认地址失败');
            throw error;
        }
    };

    const refreshOrders = async () : Promise<void> => {
        try {
            setOrders(await api_users_getOrderList(user?.iUserId as number)) ;
        } catch (error) {
            message.error(error instanceof Error ? error.message : '获取订单失败');
            throw error;
        }
    }

    return (
        <UserContext.Provider value={{
            user,
            addresses,
            orders,
            userSettings,
            token,
            loading,
            user_login,
            user_register,
            user_logout,
            updateProfile,
            addAddress,
            updateAddress,
            deleteAddress,
            setDefaultAddress,
            refreshAddresses,
            refreshOrders
            // refreshSettings
        }}>
            {loading ? null : children}
        </UserContext.Provider>
    );
};

export const useUser = () => {
    const context = useContext(UserContext);
    if (context === undefined) {
        throw new Error('useUser must be used within a UserProvider');
    }
    return context;
};