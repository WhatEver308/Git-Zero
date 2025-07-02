import React, { useState } from 'react';
import { 
  Typography, 
  Row, 
  Col, 
  Steps, 
  Button, 
  Form, 
  Input, 
  Select, 
  Radio, 
  Card, 
  Divider, 
  Space,
  List,
  Alert,
  Collapse,
  Modal,
  Tag
} from 'antd';
import { 
  ChevronLeft, 
  MapPin, 
  CreditCard, 
  Clock, 
  CheckCircle, 
  Truck, 
  DollarSign,
  AlertCircle,
  ShoppingCart
} from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import styled from 'styled-components';
import { CartItem, Address, Order } from '../types';
import { customStyles } from '../styles/theme';
import { useCart } from '../contexts/CartContext';
import { useUser } from '../contexts/UserContext';
import { createOrder } from '../services/api_orders';
import BackButton from '../components/BackButton';

const { Title, Text, Paragraph } = Typography;
const { Option } = Select;
const { Panel } = Collapse;

const PageContainer = styled.div`
  max-width: 1200px;
  margin: 0 auto;
  padding: ${customStyles.spacing.lg};
  
  @media (max-width: 768px) {
    padding: ${customStyles.spacing.md};
  }
`;

const CheckoutHeader = styled.div`
  display: flex;
  align-items: center;
  margin-bottom: ${customStyles.spacing.lg};
`;

const StepsContainer = styled.div`
  margin-bottom: ${customStyles.spacing.lg};
`;

const ContentContainer = styled.div`
  display: flex;
  gap: ${customStyles.spacing.lg};
  
  @media (max-width: 992px) {
    flex-direction: column;
  }
`;

const MainContent = styled.div`
  flex: 3;
`;

const SidebarContent = styled.div`
  flex: 1;
`;

const StyledCard = styled(Card)`
  margin-bottom: ${customStyles.spacing.md};
  border-radius: 8px;
  box-shadow: ${customStyles.shadows.small};
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
`;

const DeliveryTimeCard = styled(Card)`
  background-color: #f6ffed;
  border-color: ${customStyles.colors.primary};
  margin-bottom: ${customStyles.spacing.md};
`;

const PaymentOption = styled(Radio.Button)<{ $selected?: boolean }>`
  height: 80px;
  width: 100%;
  text-align: left;
  padding: 16px;
  margin-bottom: 8px;
  border-radius: 8px !important;
  display: flex;
  align-items: center;
  
  &.ant-radio-button-wrapper-checked {
    border-color: ${customStyles.colors.primary} !important;
    background-color: #f6ffed;
  }
  
  &::before {
    display: none !important;
  }
  
  svg {
    margin-right: 12px;
    color: ${props => props.$selected ? customStyles.colors.primary : customStyles.colors.textSecondary};
  }
`;

const AddressOption = styled(Radio.Button)<{ $selected?: boolean }>`
  height: auto;
  width: 100%;
  text-align: left;
  padding: 16px;
  margin-bottom: 8px;
  border-radius: 8px !important;
  display: block;
  
  &.ant-radio-button-wrapper-checked {
    border-color: ${customStyles.colors.primary} !important;
    background-color: #f6ffed;
  }
  
  &::before {
    display: none !important;
  }
`;

const AddressLabel = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 4px;
  
  span {
    font-weight: bold;
  }
`;

const OrderButton = styled(Button)`
  width: 100%;
  height: 48px;
  font-size: 16px;
`;



const Checkout: React.FC = () => {
  const navigate = useNavigate();
  const { state: cartState, clearCart } = useCart();
  const { addresses, addAddress, user } = useUser();
  const [currentStep, setCurrentStep] = useState(0);
  const [form] = Form.useForm();
  const [paymentMethod, setPaymentMethod] = useState('card');
  const [selectedAddress, setSelectedAddress] = useState<number | string | null>(
    addresses.find(addr => addr.bDefault)?.iAddressId || addresses[0]?.iAddressId || null
  );
  const [showNewAddressForm, setShowNewAddressForm] = useState(false);
  const [orderProcessing, setOrderProcessing] = useState(false);
  const [showConfirmModal, setShowConfirmModal] = useState(false);
  
  // Calculate totals
  const subtotal = cartState.items.reduce((total, item) => total + (item.dMenuItemPrice * item.quantity), 0);
  const deliveryFee = 2.99;
  const tax = subtotal * 0.08;
  const total = subtotal + deliveryFee + tax;

  // Redirect if cart is empty
  React.useEffect(() => {
    if (cartState.items.length === 0) {
      navigate('/cart');
    }
  }, [cartState.items, navigate]);

  // Update selected address when addresses change
  React.useEffect(() => {
    if (!selectedAddress && addresses.length > 0) {
      const defaultAddress = addresses.find(addr => addr.bDefault);
      setSelectedAddress(defaultAddress?.iAddressId || addresses[0].iAddressId);
    }
  }, [addresses, selectedAddress]);

  const handleStepChange = (step: number) => {
    form.validateFields()
      .then(() => {
        setCurrentStep(step);
      })
      .catch(error => {
        console.log('验证失败:', error);
      });
  };
  
  const handleAddressChange = (e: any) => {
    setSelectedAddress(e.target.value);
    if (e.target.value === 'new') {
      setShowNewAddressForm(true);
    } else {
      setShowNewAddressForm(false);
    }
  };

  const handleAddNewAddress = async () => {
    try {
      const values = await form.validateFields([
        'strRecipientName',
        'strPhone',
        'strProvince',
        'strCity',
        'strDistrict',
        'strAddress',
        'bDefault'
      ]);

      const newAddress: Omit<Address, 'iAddressId'> = {
        strRecipientName: values.strRecipientName,
        strPhone: values.strPhone,
        strProvince: values.strProvince,
        strCity: values.strCity,
        strDistrict: values.strDistrict,
        strAddress: values.strAddress,
        bDefault: values.bDefault || false
      };

      await addAddress(newAddress);
      setShowNewAddressForm(false);
      form.resetFields(['strRecipientName', 'strPhone', 'strProvince', 'strCity', 'strDistrict', 'strAddress', 'bDefault']);
    } catch (error) {
      console.error('添加地址失败:', error);
    }
  };

  const handlePlaceOrder = () => {
    setShowConfirmModal(true);
  };
  
  const confirmOrder = async () => {
    setShowConfirmModal(false);
    setOrderProcessing(true);
    
    try {
      // Get selected address
      const selectedAddressData = addresses.find(addr => addr.iAddressId === selectedAddress);

      if (!selectedAddressData) {
        throw new Error('请选择配送地址');
      }

      // Prepare order data according to API specification
      const orderData = {
        iUserId: user?.iUserId || 0,
        iMerchantId: cartState.merchantId!,
        address: selectedAddressData,
        orderItemList: cartState.items.map(item => ({
          iMenuItemId: item.iMenuItemId,
          iQuantity: item.quantity
        }))
      };

      // Call the API
      const response = await createOrder(orderData);

      // Clear cart after successful order
      clearCart();

      // Navigate to order confirmation with order ID
      navigate('/order-confirmation', {
        state: {
          orderId: response.order_id,
          orderData: orderData,
          total: total
        }
      });
    } catch (error) {
      console.error('订单创建失败:', error);
      // Show error message but keep modal open for retry
      setShowConfirmModal(true);
    } finally {
      setOrderProcessing(false);
    }
  };
  
  const renderDeliveryStep = () => {
    return (
      <StyledCard>
        <Title level={4}>配送地址</Title>
        
        <Form layout="vertical">
          <Form.Item>
            <Radio.Group 
              value={selectedAddress} 
              onChange={handleAddressChange}
              style={{ width: '100%' }}
            >
              {addresses.map(address => (
                <AddressOption
                  key={address.iAddressId}
                  value={address.iAddressId}
                  $selected={selectedAddress === address.iAddressId}
                >
                  <AddressLabel>
                    <span>{address.strRecipientName}</span>
                    {address.bDefault && <Tag color="green">默认</Tag>}
                  </AddressLabel>
                  <div>{address.strPhone}</div>
                  <div>{address.strProvince} {address.strCity} {address.strDistrict}</div>
                  <div>{address.strAddress}</div>
                </AddressOption>
              ))}
              
              <AddressOption 
                value="new"
                $selected={selectedAddress === 'new'}
              >
                <div style={{ display: 'flex', alignItems: 'center' }}>
                  <MapPin size={16} style={{ marginRight: '8px' }} />
                  添加新地址
                </div>
              </AddressOption>
            </Radio.Group>
          </Form.Item>
          
          {showNewAddressForm && (
            <div style={{ border: '1px solid #f0f0f0', padding: '16px', borderRadius: '8px' }}>
              <Form.Item label="收件人姓名" name="strRecipientName" rules={[{ required: true }]}>
                <Input placeholder="请输入收件人姓名" />
              </Form.Item>
              
              <Form.Item label="手机号" name="strPhone" rules={[{ required: true }]}>
                <Input placeholder="请输入手机号" />
              </Form.Item>
              
              <Row gutter={16}>
                <Col span={8}>
                  <Form.Item label="省份" name="strProvince" rules={[{ required: true }]}>
                    <Input placeholder="省份" />
                  </Form.Item>
                </Col>
                <Col span={8}>
                  <Form.Item label="城市" name="strCity" rules={[{ required: true }]}>
                    <Input placeholder="城市" />
                  </Form.Item>
                </Col>
                <Col span={8}>
                  <Form.Item label="区/县" name="strDistrict" rules={[{ required: true }]}>
                    <Input placeholder="区/县" />
                  </Form.Item>
                </Col>
              </Row>
              
              <Form.Item label="详细地址" name="strAddress" rules={[{ required: true }]}>
                <Input placeholder="街道、门牌号等详细地址" />
              </Form.Item>

              <Form.Item name="bDefault" valuePropName="checked">
                <Radio>设为默认地址</Radio>
              </Form.Item>

              <div style={{ display: 'flex', gap: '8px', marginTop: '16px' }}>
                <Button type="primary" onClick={handleAddNewAddress}>
                  保存地址
                </Button>
                <Button onClick={() => {
                  setShowNewAddressForm(false);
                  setSelectedAddress(addresses.find(addr => addr.bDefault)?.iAddressId || addresses[0]?.iAddressId || null);
                }}>
                  取消
                </Button>
              </div>
            </div>
          )}
          
          <div style={{ display: 'flex', justifyContent: 'flex-end', marginTop: '16px' }}>
            <Button 
              type="primary" 
              onClick={() => handleStepChange(1)}
              disabled={!selectedAddress || selectedAddress === 'new'}
            >
              继续支付
            </Button>
          </div>
        </Form>
      </StyledCard>
    );
  };
  
  const renderPaymentStep = () => {
    return (
      <StyledCard>
        <Title level={4}>支付方式</Title>
        
        <Form layout="vertical">
          <Form.Item>
            <Radio.Group 
              value={paymentMethod} 
              onChange={(e) => setPaymentMethod(e.target.value)}
              style={{ width: '100%' }}
            >
              <PaymentOption 
                value="card"
                $selected={paymentMethod === 'card'}
              >
                <CreditCard size={24} />
                <div>
                  <div><strong>信用卡/借记卡</strong></div>
                  <Text type="secondary">支持Visa、Mastercard等</Text>
                </div>
              </PaymentOption>
              
              <PaymentOption 
                value="cash"
                $selected={paymentMethod === 'cash'}
              >
                <DollarSign size={24} />
                <div>
                  <div><strong>货到付款</strong></div>
                  <Text type="secondary">收货时现金支付</Text>
                </div>
              </PaymentOption>
            </Radio.Group>
          </Form.Item>
          
          {paymentMethod === 'card' && (
            <>
              <Form.Item label="卡号" name="cardNumber" rules={[{ required: true }]}>
                <Input placeholder="1234 5678 9012 3456" />
              </Form.Item>
              
              <Row gutter={16}>
                <Col span={12}>
                  <Form.Item label="有效期" name="expiryDate" rules={[{ required: true }]}>
                    <Input placeholder="MM/YY" />
                  </Form.Item>
                </Col>
                <Col span={12}>
                  <Form.Item label="安全码" name="cvv" rules={[{ required: true }]}>
                    <Input placeholder="123" />
                  </Form.Item>
                </Col>
              </Row>
              
              <Form.Item label="持卡人姓名" name="nameOnCard" rules={[{ required: true }]}>
                <Input placeholder="张三" />
              </Form.Item>
            </>
          )}
          
          <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: '16px' }}>
            <Button 
              onClick={() => handleStepChange(0)}
            >
              返回配送
            </Button>
            
            <Button 
              type="primary" 
              onClick={() => handleStepChange(2)}
            >
              确认订单
            </Button>
          </div>
        </Form>
      </StyledCard>
    );
  };
  
  const renderReviewStep = () => {
    // 获取选中的地址
    const address = addresses.find(addr => addr.iAddressId === selectedAddress) || addresses[0];

    return (
      <StyledCard>
        <Title level={4}>确认订单</Title>
        
        <Collapse defaultActiveKey={['1', '2', '3']} expandIconPosition="end">
          <Panel 
            header={
              <Space>
                <MapPin size={16} color={customStyles.colors.primary} />
                <Text strong>配送地址</Text>
              </Space>
            } 
            key="1"
          >
            <div>
              <Text strong>{address?.strRecipientName}</Text>
              <div>{address?.strPhone}</div>
              <div>{address?.strProvince} {address?.strCity} {address?.strDistrict}</div>
              <div>{address?.strAddress}</div>
            </div>
          </Panel>
          
          <Panel 
            header={
              <Space>
                <CreditCard size={16} color={customStyles.colors.primary} />
                <Text strong>支付方式</Text>
              </Space>
            } 
            key="2"
          >
            <div>
              {paymentMethod === 'card' ? (
                <div>
                  <Text strong>信用卡/借记卡</Text>
                  <div>卡号尾号 **** 3456</div>
                </div>
              ) : (
                <div>
                  <Text strong>货到付款</Text>
                  <div>请准备好现金</div>
                </div>
              )}
            </div>
          </Panel>
          
          <Panel 
            header={
              <Space>
                <ShoppingCart size={16} color={customStyles.colors.primary} />
                <Text strong>订单明细</Text>
              </Space>
            } 
            key="3"
          >
            <List
              itemLayout="horizontal"
              dataSource={cartState.items}
              renderItem={item => (
                <List.Item>
                  <List.Item.Meta
                    title={`${item.strMenuItemName} x${item.quantity}`}
                    description={item.notes && `备注: ${item.notes}`}
                  />
                  <div>¥{(item.dMenuItemPrice * item.quantity).toFixed(2)}</div>
                </List.Item>
              )}
            />
          </Panel>
        </Collapse>
        
        <DeliveryTimeCard>
          <Space align="start">
            <Clock size={20} color={customStyles.colors.primary} />
            <div>
              <Text strong>预计送达时间</Text>
              <div>下单后30-45分钟送达</div>
            </div>
          </Space>
        </DeliveryTimeCard>
        
        <Alert
          message="下单即表示您同意我们的服务条款和隐私政策"
          type="info"
          showIcon
          icon={<AlertCircle size={16} />}
          style={{ marginBottom: customStyles.spacing.md }}
        />
        
        <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: '16px' }}>
          <Button 
            onClick={() => handleStepChange(1)}
          >
            返回支付
          </Button>
          
          <OrderButton 
            type="primary" 
            size="large"
            loading={orderProcessing}
            onClick={handlePlaceOrder}
            disabled={!address}
          >
            提交订单 (¥{total.toFixed(2)})
          </OrderButton>
        </div>
      </StyledCard>
    );
  };
  
  return (
    <PageContainer>
      <CheckoutHeader>
        <BackButton />
        <Title level={2} style={{ margin: 0 }}>订单结算</Title>
      </CheckoutHeader>
      
      <StepsContainer>
        <Steps
          current={currentStep}
          items={[
            {
              title: '配送',
              icon: <MapPin size={16} />
            },
            {
              title: '支付',
              icon: <CreditCard size={16} />
            },
            {
              title: '确认',
              icon: <CheckCircle size={16} />
            }
          ]}
          onChange={handleStepChange}
        />
      </StepsContainer>
      
      <ContentContainer>
        <MainContent>
          {currentStep === 0 && renderDeliveryStep()}
          {currentStep === 1 && renderPaymentStep()}
          {currentStep === 2 && renderReviewStep()}
        </MainContent>
        
        <SidebarContent>
          <StyledCard title="订单摘要">
            <List
              dataSource={cartState.items}
              renderItem={item => (
                <List.Item>
                  <div>{item.strMenuItemName} x{item.quantity}</div>
                  <div>¥{(item.dMenuItemPrice * item.quantity).toFixed(2)}</div>
                </List.Item>
              )}
            />
            
            <Divider style={{ margin: '12px 0' }} />
            
            <SummaryRow>
              <Text>小计</Text>
              <Text>¥{subtotal.toFixed(2)}</Text>
            </SummaryRow>
            
            <SummaryRow>
              <Text>配送费</Text>
              <Text>¥{deliveryFee.toFixed(2)}</Text>
            </SummaryRow>
            
            <SummaryRow>
              <Text>税费</Text>
              <Text>¥{tax.toFixed(2)}</Text>
            </SummaryRow>
            
            <Divider style={{ margin: '12px 0' }} />
            
            <TotalRow>
              <Text>总计</Text>
              <Text>¥{total.toFixed(2)}</Text>
            </TotalRow>
          </StyledCard>
          
          <StyledCard>
            <Space align="start">
              <Truck size={20} color={customStyles.colors.primary} />
              <div>
                <Text strong>配送信息</Text>
                <div>预计送达时间: 30-45分钟</div>
                <div>商家: {cartState.merchantName}</div>
              </div>
            </Space>
          </StyledCard>
        </SidebarContent>
      </ContentContainer>
      
      <Modal
        title="确认订单"
        open={showConfirmModal}
        onOk={confirmOrder}
        onCancel={() => setShowConfirmModal(false)}
        okText="确认下单"
        cancelText="再看看"
      >
        <Paragraph>
          您即将从{cartState.merchantName}下单，总金额为¥{total.toFixed(2)}。
        </Paragraph>
        <Paragraph>
          您的美食将在30-45分钟内送达指定地址。
        </Paragraph>
        <Paragraph strong>
          是否确认下单？
        </Paragraph>
      </Modal>
    </PageContainer>
  );
};

export default Checkout;
