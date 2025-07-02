import React from 'react';
import { Table, Tag, Button, Space, Typography } from 'antd';
import { ColumnsType } from 'antd/es/table';
import { Eye, CheckCircle, XCircle, Clock, Truck } from 'lucide-react';
import styled from 'styled-components';
import { Order, Merchant } from '../types';
import { customStyles } from '../styles/theme';

const { Text } = Typography;

interface OrderTableProps {
  orders: Order[];
  loading?: boolean;
  onViewDetails?: (order: Order) => void;
  onUpdateStatus?: (orderId: string, status: Order['strStatus']) => void;
  showMerchantActions?: boolean;
  showUserActions?: boolean;
}

const StyledTable = styled(Table)`
  .ant-table-thead > tr > th {
    background-color: #fafafa;
    font-weight: 600;
  }
  
  .ant-table-tbody > tr:hover > td {
    background-color: #f5f5f5;
  }
`;

const StatusTag = styled(Tag)<{ $status: Order['strStatus'] }>`
  font-weight: 500;
`;

const ActionButton = styled(Button)`
  display: flex;
  align-items: center;
  gap: 4px;
`;

const AddressText = styled(Text)`
  max-width: 200px;
  display: block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
`;

const getStatusColor = (status: Order['strStatus']) => {
  switch (status) {
    case 'Pending':
      return 'orange';
    case 'Confirmed':
      return 'blue';
    case 'Preparing':
      return 'cyan';
    case 'Delivering':
      return 'purple';
    case 'Delivered':
      return 'green';
    case 'Cancelled':
      return 'red';
    default:
      return 'default';
  }
};

const getStatusText = (status: Order['strStatus']) => {
  switch (status) {
    case 'Pending':
      return '待确认';
    case 'Confirmed':
      return '已确认';
    case 'Preparing':
      return '制作中';
    case 'Delivering':
      return '配送中';
    case 'Delivered':
      return '已送达';
    case 'Cancelled':
      return '已取消';
    default:
      return status;
  }
};

const OrderTable: React.FC<OrderTableProps> = ({
  orders,
  loading = false,
  onViewDetails,
  onUpdateStatus,
  showMerchantActions = false,
  showUserActions = false,
}) => {
  const defaultColumns: ColumnsType<Order> = [
    {
      title: '订单ID',
      dataIndex: 'iOrderId',
      key: 'iOrderId',
      width: 120,
      render: (id: number) => (
        <Text strong>#{id}</Text>
      ),
    },
    {
      title: '收货人',
      key: 'recipient',
      width: 120,
      render: (_, record) => (
        <div>
          <Text strong>{record.objAddress.strRecipientName}</Text>
          <br />
          <Text type="secondary" style={{ fontSize: '12px' }}>
            {record.objAddress.strPhone}
          </Text>
        </div>
      ),
    },
    {
      title: '配送地址',
      key: 'address',
      width: 200,
      render: (_, record) => (
        <AddressText>
          {record.objAddress.strProvince} {record.objAddress.strCity} {record.objAddress.strDistrict} {record.objAddress.strAddress}
        </AddressText>
      ),
    },
    {
      title: '订单商品',
      key: 'items',
      width: 200,
      render: (_, record) => (
        <div>
          {record.orderItemList.map((item, index) => (
            <div key={index} style={{ marginBottom: '4px' }}>
              <Text>
                {item.menuItem?.strMenuItemName || '未知商品'} × {item.iQuantity}
              </Text>
            </div>
          ))}
        </div>
      ),
    },
    {
      title: '总金额',
      dataIndex: 'dTotalPrice',
      key: 'dTotalPrice',
      width: 100,
      render: (price: number) => (
        <Text strong style={{ color: customStyles.colors.primary }}>
          ¥{(price || 0).toFixed(2)}
        </Text>
      ),
    },
    {
      title: '订单状态',
      dataIndex: 'strStatus',
      key: 'strStatus',
      width: 120,
      render: (status: Order['strStatus']) => (
        <StatusTag $status={status} color={getStatusColor(status)}>
          {getStatusText(status)}
        </StatusTag>
      ),
    },
    {
      title: '下单时间',
      dataIndex: 'strCreatedAt',
      key: 'strCreatedAt',
      width: 150,
      render: (time: string) => (
        <div>
          <div>{new Date(time).toLocaleDateString()}</div>
          <Text type="secondary" style={{ fontSize: '12px' }}>
            {new Date(time).toLocaleTimeString()}
          </Text>
        </div>
      ),
    },
    {
      title: '操作',
      key: 'actions',
      fixed: 'right',
      width: showMerchantActions ? 200 : 120,
      render: (_, record) => (
        <Space direction="vertical" size="small">
          <ActionButton
            type="text"
            icon={<Eye size={14} />}
            onClick={() => onViewDetails?.(record)}
            size="small"
          >
            查看详情
          </ActionButton>

          {showMerchantActions && (
            <Space>
              {record.strStatus === 'Pending' && (
                <ActionButton
                  type="text"
                  icon={<CheckCircle size={14} />}
                  onClick={() => onUpdateStatus?.(record.iOrderId.toString(), 'Confirmed')}
                  size="small"
                  style={{ color: '#52c41a' }}
                >
                  确认
                </ActionButton>
              )}

              {record.strStatus === 'Confirmed' && (
                <ActionButton
                  type="text"
                  icon={<Clock size={14} />}
                  onClick={() => onUpdateStatus?.(record.iOrderId.toString(), 'Preparing')}
                  size="small"
                  style={{ color: '#1890ff' }}
                >
                  制作中
                </ActionButton>
              )}

              {record.strStatus === 'Preparing' && (
                <ActionButton
                  type="text"
                  icon={<Truck size={14} />}
                  onClick={() => onUpdateStatus?.(record.iOrderId.toString(), 'Delivering')}
                  size="small"
                  style={{ color: '#722ed1' }}
                >
                  配送
                </ActionButton>
              )}

              {record.strStatus === 'Delivering' && (
                <ActionButton
                  type="text"
                  icon={<CheckCircle size={14} />}
                  onClick={() => onUpdateStatus?.(record.iOrderId.toString(), 'Delivered')}
                  size="small"
                  style={{ color: '#52c41a' }}
                >
                  完成
                </ActionButton>
              )}

              {(record.strStatus === 'Pending' || record.strStatus === 'Confirmed') && (
                <ActionButton
                  type="text"
                  icon={<XCircle size={14} />}
                  onClick={() => onUpdateStatus?.(record.iOrderId.toString(), 'Cancelled')}
                  size="small"
                  danger
                >
                  取消
                </ActionButton>
              )}
            </Space>
          )}

          {showUserActions && record.strStatus === 'Pending' && (
            <ActionButton
              type="text"
              icon={<XCircle size={14} />}
              onClick={() => onUpdateStatus?.(record.iOrderId.toString(), 'Cancelled')}
              size="small"
              danger
            >
              取消订单
            </ActionButton>
          )}
        </Space>
      ),
    },
  ];

  return (
    <StyledTable
      columns={defaultColumns as any}
      dataSource={orders}
      rowKey="iOrderId"
      loading={loading}
      pagination={{
        pageSize: 10,
        showSizeChanger: true,
        showQuickJumper: true,
        showTotal: (total, range) =>
          `第 ${range[0]}-${range[1]} 条 共 ${total} 条记录`,
      }}
      scroll={{ x: 1200 }}
      size="middle"
    />
  );
};

export default OrderTable;
