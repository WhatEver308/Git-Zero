import React, { useState } from 'react';
import { Card, Typography, Button, InputNumber, Modal, Form, Input, message } from 'antd';
import { Plus, Minus } from 'lucide-react';
import styled from 'styled-components';
import { MenuItem } from '../types';
import { useCart } from '../contexts/CartContext';
import { customStyles } from '../styles/theme';
import Base64Image from './Base64Image';

const { Text, Title } = Typography;
const { TextArea } = Input;

interface MenuItemCardProps {
  item: MenuItem;
  merchantId: string;
  merchantName: string;
}

const StyledCard = styled(Card)`
  display: flex;
  margin-bottom: ${customStyles.spacing.md};
  border-radius: 8px;
  overflow: hidden;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
  border: 1px solid #f0f0f0;
  
  &:hover {
    box-shadow: ${customStyles.shadows.small};
  }
  
  .ant-card-body {
    padding: 0;
    display: flex;
    width: 100%;
  }
`;

const ItemImage = styled.div`
  width: 120px;
  height: 120px;
  overflow: hidden;
  flex-shrink: 0;
  
  @media (max-width: 576px) {
    width: 80px;
    height: 80px;
  }
`;

const ItemContent = styled.div`
  flex: 1;
  padding: ${customStyles.spacing.md};
  display: flex;
  flex-direction: column;
  justify-content: space-between;
`;

const ItemDescription = styled(Text)`
  color: ${customStyles.colors.textSecondary};
  font-size: 14px;
  line-height: 1.4;
  margin: ${customStyles.spacing.xs} 0;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
`;

const ItemFooter = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: auto;
`;

const PriceText = styled(Text)`
  color: ${customStyles.colors.primary};
  font-size: 18px;
  font-weight: bold;
`;

const QuantityControl = styled.div`
  display: flex;
  align-items: center;
  gap: 8px;
`;

const QuantityButton = styled(Button)`
  min-width: 32px;
  height: 32px;
  padding: 0;
  display: flex;
  align-items: center;
  justify-content: center;
`;

const QuantityText = styled(Text)`
  margin: 0 ${customStyles.spacing.sm};
  min-width: 20px;
  text-align: center;
`;

const MenuItemCard: React.FC<MenuItemCardProps> = ({ item, merchantId, merchantName }) => {
  const [quantity, setQuantity] = useState(1);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [form] = Form.useForm();
  const { addItem } = useCart();

  const handleAddToCart = () => {
    if (quantity > 0) {
      setIsModalVisible(true);
    }
  };

  const handleModalOk = () => {
    form.validateFields().then(values => {
      // 转换 MenuItem 格式以适配购物车
      const cartItem = {
        ...item,
        id: item.iMenuItemId.toString(),
        name: item.strMenuItemName,
        description: item.strMenuItemDescription,
        price: item.dMenuItemPrice,
        image: item.strImage_url,
        category: item.striMenuItemCategory,
        quantity,
        notes: values.notes
      };
      addItem(
        cartItem,
        parseInt(merchantId),
        merchantName
      );
      setIsModalVisible(false);
      setQuantity(1);
      form.resetFields();
      message.success(`已添加${quantity}份${item.strMenuItemName}到购物车`);
    });
  };

  const handleModalCancel = () => {
    setIsModalVisible(false);
    form.resetFields();
    setQuantity(1);
  };

  return (
    <>
      <StyledCard bordered={false}>
        <ItemImage>
          <Base64Image
            imageUrl={item.strImage_url}
            alt={item.strMenuItemName}
            fallbackSrc="https://images.pexels.com/photos/2611917/pexels-photo-2611917.jpeg"
            style={{ width: '100%', height: '100%' }}
          />
        </ItemImage>

        <ItemContent>
          <div>
            <Title level={5} style={{ margin: 0 }}>
              {item.strMenuItemName || '菜品名称未知'}
            </Title>
            <ItemDescription>
              {item.strMenuItemDescription || '暂无描述'}
            </ItemDescription>
          </div>

          <ItemFooter>
            <PriceText>¥{item.dMenuItemPrice?.toFixed(2) || '0.00'}</PriceText>

            <QuantityControl>
              <QuantityButton
                icon={<Minus size={16} />}
                onClick={() => setQuantity(Math.max(1, quantity - 1))}
                disabled={quantity <= 1}
              />
              <QuantityText>{quantity}</QuantityText>
              <QuantityButton
                icon={<Plus size={16} />}
                onClick={() => setQuantity(quantity + 1)}
              />
              <Button
                type="primary"
                onClick={handleAddToCart}
                style={{ marginLeft: 8 }}
              >
                添加
              </Button>
            </QuantityControl>
          </ItemFooter>
        </ItemContent>
      </StyledCard>

      <Modal
        title={item.strMenuItemName}
        open={isModalVisible}
        onOk={handleModalOk}
        onCancel={handleModalCancel}
        okText="加入购物车"
        cancelText="取消"
        width={500}
      >
        <div style={{ marginBottom: customStyles.spacing.md }}>
          <div style={{ width: '100%', height: 200, marginBottom: customStyles.spacing.md, overflow: 'hidden', borderRadius: '8px' }}>
            <Base64Image
              imageUrl={item.strImage_url}
              alt={item.strMenuItemName}
              fallbackSrc="https://images.pexels.com/photos/2611917/pexels-photo-2611917.jpeg"
              style={{ width: '100%', height: '100%' }}
            />
          </div>

          <Text>{item.strMenuItemDescription || '暂无描述'}</Text>
          <PriceText style={{ display: 'block', marginTop: 8 }}>
            ¥{item.dMenuItemPrice?.toFixed(2) || '0.00'}
          </PriceText>
        </div>

        <Form form={form} layout="vertical">
          <Form.Item label="数量">
            <QuantityControl>
              <QuantityButton
                icon={<Minus size={16} />}
                onClick={() => setQuantity(Math.max(1, quantity - 1))}
                disabled={quantity <= 1}
              />
              <InputNumber
                min={1}
                value={quantity}
                onChange={(value) => setQuantity(value || 1)}
                style={{ width: 60, textAlign: 'center' }}
              />
              <QuantityButton
                icon={<Plus size={16} />}
                onClick={() => setQuantity(quantity + 1)}
              />
            </QuantityControl>
          </Form.Item>

          <Form.Item label="备注" name="notes">
            <TextArea
              rows={3}
              placeholder="口味偏好、忌口等"
              maxLength={100}
            />
          </Form.Item>
        </Form>
      </Modal>
    </>
  );
};

export default MenuItemCard;