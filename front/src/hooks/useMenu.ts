import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { 
  getMenuByMerchantId, 
  addMenuItem, 
  updateMenuItem, 
  deleteMenuItem,
  on,
  off
} from '../services/api_merchants';
import { MenuItem } from '../types';
import { message } from 'antd';
import { useEffect } from 'react';

export const useMenu = (merchantId?: string) => {
  const queryClient = useQueryClient();

  // Set up real-time event listeners
  useEffect(() => {
    const handleMenuItemAdded = (data: { merchantId: string; item: MenuItem }) => {
      if (data.merchantId === merchantId) {
        queryClient.setQueryData(['menu', merchantId], (oldData: MenuItem[] | undefined) => {
          return oldData ? [...oldData, data.item] : [data.item];
        });
      }
    };

    const handleMenuItemUpdated = (data: { merchantId: string; itemId: string; item: MenuItem }) => {
      if (data.merchantId === merchantId) {
        queryClient.setQueryData(['menu', merchantId], (oldData: MenuItem[] | undefined) => {
          return oldData ? oldData.map(item => 
            item.iMenuItemId.toString() === data.itemId ? data.item : item
          ) : [];
        });
      }
    };

    const handleMenuItemDeleted = (data: { merchantId: string; itemId: string }) => {
      if (data.merchantId === merchantId) {
        queryClient.setQueryData(['menu', merchantId], (oldData: MenuItem[] | undefined) => {
          return oldData ? oldData.filter(item => item.iMenuItemId.toString() !== data.itemId) : [];
        });
      }
    };

    on('menuItemAdded', handleMenuItemAdded);
    on('menuItemUpdated', handleMenuItemUpdated);
    on('menuItemDeleted', handleMenuItemDeleted);

    return () => {
      off('menuItemAdded', handleMenuItemAdded);
      off('menuItemUpdated', handleMenuItemUpdated);
      off('menuItemDeleted', handleMenuItemDeleted);
    };
  }, [merchantId, queryClient]);

  return useQuery<MenuItem[], Error>({
    queryKey: ['menu', merchantId],
    queryFn: () => {
      if (!merchantId) {
        throw new Error('Merchant ID is required');
      }
      return getMenuByMerchantId(merchantId);
    },
    enabled: !!merchantId,
    staleTime: 2 * 60 * 1000, // 2 minutes
    gcTime: 10 * 60 * 1000, // 10 minutes
  });
};

export const useAddMenuItem = (merchantId?: string) => {
  return useMutation({
    mutationFn: (item: Omit<MenuItem, 'iMenuItemId'>) => {
      if (!merchantId) {
        throw new Error('Merchant ID is required');
      }
      return addMenuItem(merchantId, item);
    },
    onSuccess: () => {
      // 不需要更新缓存，因为 addMenuItem 函数会自动刷新页面
      message.success('菜品添加成功');
    },
    onError: (error: Error) => {
      message.error(error.message || '添加菜品失败');
    },
  });
};

export const useUpdateMenuItem = (merchantId?: string) => {
  return useMutation({
    mutationFn: ({ itemId, item }: { itemId: string; item: Partial<MenuItem> }) => {
      if (!merchantId) {
        throw new Error('Merchant ID is required');
      }
      return updateMenuItem(merchantId, itemId, item);
    },
    onSuccess: () => {
      // 不需要更新缓存，因为 updateMenuItem 函数会自动刷新页面
      message.success('菜品更新成功');
    },
    onError: (error: Error) => {
      message.error(error.message || '更新菜品失败');
    },
  });
};

export const useDeleteMenuItem = (merchantId?: string) => {
  const queryClient = useQueryClient();
  
  return useMutation({
    mutationFn: (itemId: string) => {
      if (!merchantId) {
        throw new Error('Merchant ID is required');
      }
      return deleteMenuItem(merchantId, itemId);
    },
    onSuccess: (_, deletedItemId) => {
      // Update cache immediately
      queryClient.setQueryData(['menu', merchantId], (oldData: MenuItem[] | undefined) => {
        return oldData ? oldData.filter(item => item.iMenuItemId.toString() !== deletedItemId) : [];
      });
      queryClient.invalidateQueries({ queryKey: ['menu'] });
      message.success('菜品删除成功');
    },
    onError: (error: Error) => {
      message.error(error.message || '删除菜品失败');
    },
  });
};

export default useMenu;