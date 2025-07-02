import React, { useState, useEffect } from 'react';
import {
  Typography,
  Card,
  Space,
  Divider,
  Button,
  Result,
  Spin,
  Tag,
  List,
  message
} from 'antd';
import {
  CheckCircle,
  Clock,
  CreditCard,
  MapPin,
  Home,
  RefreshCw
} from 'lucide-react';
import { useLocation, useNavigate } from 'react-router-dom';
import styled from 'styled-components';
import { customStyles } from '../styles/theme';
import { queryPaymentStatus, PaymentQueryResponse } from '../services/api_payment';

const { Title, Text, Paragraph } = Typography;

const PageContainer = styled.div`
  max-width: 800px;
  margin: 0 auto;
  padding: ${customStyles.spacing.lg};
  min-height: 100vh;
  
  @media (max-width: 768px) {
    padding: ${customStyles.spacing.md};
  }
`;

const StyledCard = styled(Card)`
  margin-bottom: ${customStyles.spacing.md};
  border-radius: 8px;
  box-shadow: ${customStyles.shadows.small};
`;

const OrderHeader = styled.div`
  text-align: center;
  margin-bottom: ${customStyles.spacing.lg};
`;

const StatusIcon = styled.div<{ $status: string }>`
  width: 80px;
  height: 80px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 16px;
  background-color: ${props => 
    props.$status === 'Done' 
      ? '#f6ffed' 
      : props.$status === 'Failed'
        ? '#fff2f0'
        : '#fff7e6'};
  
  svg {
    width: 40px;
    height: 40px;
    color: ${props => 
      props.$status === 'Done' 
        ? '#52c41a' 
        : props.$status === 'Failed'
          ? '#ff4d4f'
          : '#faad14'};
  }
`;

const SummaryRow = styled.div`
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
`;

const TotalRow = styled(SummaryRow)`
  margin-top: ${customStyles.spacing.md};
  font-weight: bold;
  font-size: 16px;
  padding-top: 8px;
  border-top: 1px solid ${customStyles.colors.border};
`;

const ActionButtons = styled.div`
  display: flex;
  gap: ${customStyles.spacing.md};
  justify-content: center;
  margin-top: ${customStyles.spacing.lg};
  
  @media (max-width: 576px) {
    flex-direction: column;
  }
`;

const LoadingContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 400px;
`;

interface LocationState {
  orderId: string;
  orderData?: {
    iUserId: number;
    iMerchantId: number;
    address: any;
    orderItemList: any[];
  };
  total?: number;
}

const OrderConfirmation: React.FC = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const [paymentStatus, setPaymentStatus] = useState<PaymentQueryResponse | null>(null);
  const [loading, setLoading] = useState(true);
  const [polling, setPolling] = useState(true);

  const state = location.state as LocationState;

  // 如果没有订单信息，重定向到首页
  useEffect(() => {
    if (!state?.orderId) {
      navigate('/', { replace: true });
    }
  }, [state, navigate]);

  // 轮询支付状态
  useEffect(() => {
    if (!state?.orderId || !state?.orderData?.iMerchantId || !polling) {
      return;
    }

    const pollPaymentStatus = async () => {
      try {
        const response = await queryPaymentStatus(
          state.orderData!.iMerchantId,
          state.orderId
        );
        setPaymentStatus(response);

        // 如果支付完成，停止轮询并显示成功消息
        if (response.strStatus === 'Done') {
          setPolling(false);
          message.success('支付成功！');
        } else if (response.strStatus === 'Failed') {
          setPolling(false);
          message.error('支付失败，请重试');
        }
      } catch (error) {
        console.error('查询支付状态失败:', error);
      } finally {
        setLoading(false);
      }
    };

    // 立即查询一次
    pollPaymentStatus();

    // 如果状态是等待中，每秒轮询一次
    const interval = setInterval(pollPaymentStatus, 1000);

    return () => clearInterval(interval);
  }, [state?.orderId, state?.orderData?.iMerchantId, polling]);

  const handleReturnHome = () => {
    navigate('/');
  };

  const handleViewOrders = () => {
    navigate('/profile', { state: { activeTab: 'orders' } });
  };

  const handleRetryPayment = () => {
    setPolling(true);
    setLoading(true);
  };

  if (!state?.orderId) {
    return null;
  }

  if (loading && !paymentStatus) {
    return (
      <PageContainer>
        <LoadingContainer>
          <Spin size="large" />
          <Text style={{ marginTop: 16 }}>正在处理您的订单...</Text>
        </LoadingContainer>
      </PageContainer>
    );
  }

  const getStatusInfo = () => {
    if (!paymentStatus) {
      return {
        icon: <Clock />,
        title: '处理中',
        description: '正在处理您的订单，请稍候...',
        color: 'orange'
      };
    }

    switch (paymentStatus.strStatus) {
      case 'Done':
        return {
          icon: <CheckCircle />,
          title: '订单已确认',
          description: '您的订单已成功提交并确认付款',
          color: 'green'
        };
      case 'Failed':
        return {
          icon: <CreditCard />,
          title: '支付失败',
          description: '支付过程中出现问题，请重试',
          color: 'red'
        };
      default:
        return {
          icon: <RefreshCw className={polling ? 'animate-spin' : ''} />,
          title: '等待支付',
          description: '正在等待支付确认...',
          color: 'orange'
        };
    }
  };

  const statusInfo = getStatusInfo();

  return (
    <PageContainer>
      <OrderHeader>
        <StatusIcon $status={paymentStatus?.strStatus || 'Waiting'}>
          {statusInfo.icon}
        </StatusIcon>
        <Title level={2}>{statusInfo.title}</Title>
        <Text type="secondary">{statusInfo.description}</Text>
      </OrderHeader>

      {/* 订单信息卡片 */}
      <StyledCard title="订单详情">
        <Space direction="vertical" style={{ width: '100%' }}>
          <div>
            <Text strong>订单号：</Text>
            <Text copyable>{state.orderId}</Text>
          </div>

          {paymentStatus && (
            <>
              <div>
                <Text strong>支付状态：</Text>
                <Tag color={statusInfo.color}>
                  {paymentStatus.strStatus === 'Done' ? '已支付' :
                   paymentStatus.strStatus === 'Failed' ? '支付失败' : '等待支付'}
                </Tag>
              </div>

              <div>
                <Text strong>创建时间：</Text>
                <Text>{paymentStatus.strCreatedTime}</Text>
              </div>

              <div>
                <Text strong>支付金额：</Text>
                <Text strong style={{ color: customStyles.colors.primary }}>
                  ¥{paymentStatus.dAmount.toFixed(2)}
                </Text>
              </div>
            </>
          )}
        </Space>
      </StyledCard>

      {/* 配送地址 */}
      {state.orderData?.address && (
        <StyledCard
          title={
            <Space>
              <MapPin size={16} />
              <span>配送地址</span>
            </Space>
          }
        >
          <div>
            <Text strong>{state.orderData.address.strRecipientName}</Text>
            <Text style={{ display: 'block' }}>{state.orderData.address.strPhone}</Text>
            <Text type="secondary">
              {state.orderData.address.strProvince} {state.orderData.address.strCity} {state.orderData.address.strDistrict}
            </Text>
            <Text style={{ display: 'block' }}>{state.orderData.address.strAddress}</Text>
          </div>
        </StyledCard>
      )}

      {/* 订单明细 */}
      {state.orderData?.orderItemList && (
        <StyledCard title="订单明细">
          <List
            dataSource={state.orderData.orderItemList}
            renderItem={(item: any) => (
              <List.Item>
                <List.Item.Meta
                  title={`${item.MenuItem?.strMenuItemName || '商品'} x${item.iQuantity}`}
                  description={item.MenuItem?.strMenuItemDescription || ''}
                />
                <Text strong>¥{((item.MenuItem?.dMenuItemPrice || 0) * item.iQuantity).toFixed(2)}</Text>
              </List.Item>
            )}
          />

          <Divider />

          {state.total && (
            <TotalRow>
              <Text>总计</Text>
              <Text strong style={{ fontSize: '18px', color: customStyles.colors.primary }}>
                ¥{state.total.toFixed(2)}
              </Text>
            </TotalRow>
          )}
        </StyledCard>
      )}

      {/* 操作按钮 */}
      <ActionButtons>
        {paymentStatus?.strStatus === 'Failed' && (
          <Button
            type="primary"
            onClick={handleRetryPayment}
            loading={loading}
          >
            重新支付
          </Button>
        )}

        <Button onClick={handleViewOrders}>
          查看订单
        </Button>

        <Button type="primary" onClick={handleReturnHome}>
          返回首页
        </Button>
      </ActionButtons>

      {/* 等待支付时的提示 */}
      {polling && paymentStatus?.strStatus === 'Waiting' && (
        <StyledCard style={{ marginTop: customStyles.spacing.lg }}>
          <div style={{ textAlign: 'center' }}>
            <Spin size="small" style={{ marginRight: 8 }} />
            <Text type="secondary">正在等待支付确认，请稍候...</Text>
          </div>
        </StyledCard>
      )}
    </PageContainer>
  );
};

export default OrderConfirmation;
