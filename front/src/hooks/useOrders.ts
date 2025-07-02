import { useQuery, useQueryClient } from '@tanstack/react-query';
import { useEffect } from 'react';
import { Order } from '../types';
import { fetchMerchantOrders, fetchUserOrders, on, off } from '../services/api_merchants';

interface UseOrdersOptions {
  merchantId?: number;
  userId?: number;
}

export const useOrders = ({ merchantId, userId }: UseOrdersOptions) => {
  const queryClient = useQueryClient();
  
  // Determine query key and fetch function based on parameters
  const queryKey = merchantId ? ['orders', 'merchant', merchantId] : ['orders', 'user', userId];
  const queryFn = () => {
    if (merchantId) {
      console.log('merchantId', merchantId);
      return fetchMerchantOrders(merchantId);
    } else if (userId) {
      return fetchUserOrders(userId);
    } else {
      throw new Error('Either merchantId or userId must be provided');
    }
  };

  // Set up real-time event listeners for order updates
  useEffect(() => {
    const handleOrderStatusUpdated = (data: { orderId: string; status: Order['strStatus']; order: Order }) => {

      // Invalidate all order queries to ensure fresh data from source
      queryClient.invalidateQueries({ queryKey: ['orders'] });
    };

    const handleOrderAdded = (order: Order) => {
      // Check if this order is relevant to current hook instance
      const isRelevant = (merchantId && order.iMerchantId === merchantId) ||
                        (userId && order.iUserId === userId);

      // Invalidate all order queries to ensure fresh data from source
      queryClient.invalidateQueries({ queryKey: ['orders'] });
    };

    on('orderStatusUpdated', handleOrderStatusUpdated);
    on('orderAdded', handleOrderAdded);

    return () => {
      off('orderStatusUpdated', handleOrderStatusUpdated);
      off('orderAdded', handleOrderAdded);
    };
  }, [queryClient, merchantId, userId]);

  const result = useQuery<Order[], Error>({
    queryKey,
    queryFn,
    staleTime: 0, // Always consider data stale to ensure fresh fetches
    gcTime: 5 * 60 * 1000, // 5 minutes
    enabled: !!(merchantId || userId), // Only run query if we have an ID
    refetchOnWindowFocus: true, // Refetch when window regains focus
  });


  return result;
};

export default useOrders;