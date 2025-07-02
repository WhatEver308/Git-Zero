import React, { useState } from 'react';
import { 
  Typography, 
  Tabs, 
  Card, 
  Button, 
  Avatar, 
  Form, 
  Input, 
  Space, 
  Divider, 
  List, 
  Tag, 
  Radio,
  Row,
  Col,
  Dropdown,
  Menu,
  Modal,
  Popconfirm,
  Select,
  DatePicker,
  Switch,
  Upload,
  Empty,
  Checkbox,
  message
} from 'antd';
import { 
  User, 
  MapPin, 
  CreditCard, 
  Heart, 
  Clock, 
  Package, 
  Settings,
  LogOut,
  Star,
  MoreVertical,
  Edit,
  Trash2,
  Plus,
  Copy,
  Camera,
  Lock,
  Bell,
  Eye,
  Shield
} from 'lucide-react';
import { Link, useNavigate } from 'react-router-dom';
import styled from 'styled-components';
import dayjs from 'dayjs';
import { customStyles } from '../styles/theme';
import { useFavorite } from '../contexts/FavoriteContext';
import { useOrder } from '../contexts/OrderContext';
import { useUser } from '../contexts/UserContext.tsx';
import BackButton from '../components/BackButton';

const { Title, Text, Paragraph } = Typography;
const { TabPane } = Tabs;
const { Option } = Select;

const PageContainer = styled.div`
  max-width: 1200px;
  margin: 0 auto;
  padding: ${customStyles.spacing.lg};
  
  @media (max-width: 768px) {
    padding: ${customStyles.spacing.md};
  }
`;

const ProfileHeader = styled.div`
  background: white;
  padding: ${customStyles.spacing.lg};
  border-radius: ${customStyles.borderRadius.lg};
  box-shadow: ${customStyles.shadows.small};
  margin-bottom: ${customStyles.spacing.lg};
`;

const AvatarUpload = styled.div`
  position: relative;
  display: inline-block;
  margin-bottom: ${customStyles.spacing.md};

  .upload-button {
    position: absolute;
    bottom: 0;
    right: 0;
    background: ${customStyles.colors.primary};
    color: white;
    border-radius: 50%;
    padding: 8px;
    cursor: pointer;
    box-shadow: ${customStyles.shadows.small};
  }
`;

const FormCard = styled(Card)`
  margin-bottom: ${customStyles.spacing.md};
`;

const AddressCard = styled(Card)<{ $isDefault?: boolean }>`
  margin-bottom: ${customStyles.spacing.md};
  border: 1px solid ${props => props.$isDefault ? customStyles.colors.primary : customStyles.colors.border};
  transition: all 0.3s ease;

  &:hover {
    box-shadow: ${customStyles.shadows.medium};
  }
`;

const AddressActions = styled.div`
  position: absolute;
  top: ${customStyles.spacing.md};
  right: ${customStyles.spacing.md};
`;

const SettingSection = styled.div`
  margin-bottom: ${customStyles.spacing.lg};
`;

const SettingItem = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: ${customStyles.spacing.md} 0;
  border-bottom: 1px solid ${customStyles.colors.border};

  &:last-child {
    border-bottom: none;
  }
`;

const FavoriteCard = styled(Card)`
  transition: all 0.3s;
  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  }
`;

const OrderCard = styled(Card)`
  margin-bottom: 16px;
`;

const UserProfile: React.FC = () => {
  const navigate = useNavigate();
  const { user_logout, user, addresses, userSettings, updateProfile, addAddress, updateAddress, deleteAddress, setDefaultAddress, refreshOrders, orders } = useUser();
  const [activeTab, setActiveTab] = useState('orders');
  const [addressForm] = Form.useForm();
  const [profileForm] = Form.useForm();
  const [editModalVisible, setEditModalVisible] = useState(false);
  const [currentAddress, setCurrentAddress] = useState<number | null>(null);
  const { getFavoriteMerchants, toggleFavorite } = useFavorite();

  const handleLogout = () => {
    Modal.confirm({
      title: '确认退出登录',
      content: '您确定要退出登录吗？',
      okText: '确定',
      cancelText: '取消',
      onOk: () => {
        user_logout();
      }
    });
  };

  const renderOrdersTab = () => {

    // refreshOrders();

    // 确保addresses是一个数组
    const orderList = Array.isArray(orders) ? orders : [];
    console.log(orders);
    console.log(orderList);
    if (orderList.length === 0) {
      return <Empty description="暂无订单" />;
    }

    return (
      <div>
        <div style={{ marginBottom: '16px', textAlign: 'right' }}>
          <Popconfirm
            title="确定要清空所有订单吗？"
            description="此操作不可撤销"
            onConfirm={() => {}}
            okText="确定"
            cancelText="取消"
          >
            <Button danger icon={<Trash2 size={16} />}>
              清空订单
            </Button>
          </Popconfirm>
        </div>
        {orderList.map(order => (
          <OrderCard key={order.iOrderId}>
            <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '16px' }}>
              <div>
                <Link to={`/merchants/${order.iMerchantId}`}>
                  <Text strong style={{ fontSize: '16px' }}>商家订单</Text>
                </Link>
                <div>
                  <Text type="secondary">{new Date(order.strCreatedAt).toLocaleDateString()}</Text>
                </div>
              </div>
              <Space>
                <Tag color="green">{order.strStatus === 'Confirmed' ? '已确认' :
                                   order.strStatus === 'Delivered' ? '已送达' :
                                   order.strStatus === 'Preparing' ? '准备中' :
                                   order.strStatus === 'Delivering' ? '配送中' :
                                   order.strStatus}</Tag>
                <Text strong>¥{order.dTotalPrice.toFixed(2)}</Text>
              </Space>
            </div>

            <List
                dataSource={order.OrderItemList || []}
                locale={{ emptyText: '该订单暂无商品信息' }}
                renderItem={(item, index) => (
                    <List.Item key={item.MenuItem?.iMenuItemId || `item-${index}`}>
                      <Text>
                        {item.MenuItem?.strMenuItemName || '未知商品'} x{item.iQuantity || 0}
                      </Text>
                    </List.Item>
                )}
            />
            
            <Divider style={{ margin: '12px 0' }} />
            
            <Space split={<Divider type="vertical" />}>
              <Button size="small">查看详情</Button>
              <Button size="small" type="primary">再来一单</Button>
              <Button size="small" icon={<Star size={14} />}>评价</Button>
            </Space>
          </OrderCard>
        ))}
      </div>
    );
  };

  const renderFavoritesTab = () => {
    const favorites = getFavoriteMerchants();

    if (favorites.length === 0) {
      return <Empty description="还没有收藏任何餐厅" />;
    }

    return (
      <Row gutter={[16, 16]}>
        {favorites.map(merchant => (
          <Col xs={24} sm={12} md={8} key={merchant.iMerchantId}>
            <Link to={`/merchants/${merchant.iMerchantId}`}>
              <FavoriteCard
                cover={<img src={merchant.strCoverImage_url} alt={merchant.strMerchantName} style={{ height: 160, objectFit: 'cover' }} />}
                actions={[
                  <Button 
                    type="text" 
                    icon={<Heart size={16} fill="red" color="red" />}
                    onClick={(e) => {
                      e.preventDefault();
                      toggleFavorite(merchant.iMerchantId.toString());
                    }}
                  />,
                  <Button type="primary" size="small">立即点餐</Button>
                ]}
              >
                <Card.Meta
                  avatar={<Avatar src={merchant.strLogo_url} />}
                  title={merchant.strMerchantName}
                  description={
                    <Space direction="vertical" size={4}>
                      <Space>
                        <Star size={14} color="#fadb14" />
                        <Text>{merchant.dRating}</Text>
                      </Space>
                      <Space>
                        <Clock size={14} />
                        <Text>{merchant.tDeliveryTime}分钟</Text>
                      </Space>
                    </Space>
                  }
                />
              </FavoriteCard>
            </Link>
          </Col>
        ))}
      </Row>
    );
  };

  const handleAddressSubmit = async (values: any) => {
    try {
      if (currentAddress) {
        await updateAddress(currentAddress, values);
      } else {
        await addAddress(values);
      }
      setEditModalVisible(false);
      addressForm.resetFields();
    } catch (error) {
      // Error is already handled in the context
    }
  };

  const renderAddressesTab = () => {
    if (!user) return null;

    // 确保addresses是一个数组
    const addressList = Array.isArray(addresses) ? addresses : [];

    return (
      <div>
        <Row gutter={[16, 16]}>
          {addressList.map(address => (
            <Col xs={24} md={12} key={address.iAddressId}>
              <AddressCard $isDefault={address.bDefault}>
                <Space align="start">
                  <MapPin size={20} color={customStyles.colors.primary} />
                  <div>
                    <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '4px' }}>
                      <Text strong>地址</Text>
                      {address.bDefault && <Tag color="green">默认地址</Tag>}
                    </div>
                    <Text>{address.strRecipientName} {address.strPhone}</Text>
                    <Text type="secondary">
                      {address.strProvince} {address.strCity} {address.strDistrict}
                    </Text>
                    <div>{address.strAddress}</div>
                  </div>
                </Space>

                <AddressActions>
                  <Space>
                    <Button 
                      type="text" 
                      icon={<Edit size={16} />}
                      onClick={() => {
                        setCurrentAddress(address.iAddressId);
                        addressForm.setFieldsValue({
                          strRecipientName: address.strRecipientName,
                          strPhone: address.strPhone,
                          strProvince: address.strProvince,
                          strCity: address.strCity,
                          strDistrict: address.strDistrict,
                          strAddress: address.strAddress,
                          bDefault: address.bDefault
                        });
                        setEditModalVisible(true);
                      }}
                    />
                    {!address.bDefault && (
                      <Button
                        type="text"
                        icon={<Star size={16} />}
                        onClick={() => setDefaultAddress(address.iAddressId!)}
                      />
                    )}
                    <Popconfirm
                      title="确定要删除这个地址吗？"
                      onConfirm={() => deleteAddress(address.iAddressId!)}
                      okText="确定"
                      cancelText="取消"
                    >
                      <Button 
                        type="text" 
                        danger 
                        icon={<Trash2 size={16} />} 
                      />
                    </Popconfirm>
                  </Space>
                </AddressActions>
              </AddressCard>
            </Col>
          ))}

          {addressList.length < 10 && (
            <Col xs={24} md={12}>
              <AddressCard
                style={{ 
                  height: '100%', 
                  minHeight: '150px',
                  display: 'flex',
                  justifyContent: 'center',
                  alignItems: 'center',
                  cursor: 'pointer'
                }}
                onClick={() => {
                  setCurrentAddress(null);
                  addressForm.resetFields();
                  setEditModalVisible(true);
                }}
              >
                <Space direction="vertical" align="center">
                  <Plus size={24} />
                  <Text>添加新地址</Text>
                </Space>
              </AddressCard>
            </Col>
          )}
        </Row>

        <Modal
          title={currentAddress ? "编辑地址" : "添加新地址"}
          open={editModalVisible}
          onCancel={() => setEditModalVisible(false)}
          onOk={() => addressForm.submit()}
          width={600}
        >
          <Form
            form={addressForm}
            layout="vertical"
            onFinish={handleAddressSubmit}
          >
            <Row gutter={16}>
              <Col span={12}>
                <Form.Item
                  name="strRecipientName"
                  label="收货人姓名"
                  rules={[{ required: true, message: '请输入收货人姓名' }]}
                >
                  <Input placeholder="请输入收货人姓名" />
                </Form.Item>
              </Col>
              <Col span={12}>
                <Form.Item
                  name="strPhone"
                  label="手机号码"
                  rules={[
                    { required: true, message: '请输入手机号码' },
                    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码' }
                  ]}
                >
                  <Input placeholder="请输入手机号码" />
                </Form.Item>
              </Col>
            </Row>

            <Row gutter={16}>
              <Col span={8}>
                <Form.Item
                  name="strProvince"
                  label="省份"
                  rules={[{ required: true, message: '请输入省份' }]}
                >
                  <Input placeholder="请输入省份，如：湖南省" />
                </Form.Item>
              </Col>
              <Col span={8}>
                <Form.Item
                  name="strCity"
                  label="城市"
                  rules={[{ required: true, message: '请输入城市' }]}
                >
                  <Input placeholder="请输入城市，如：长沙市" />
                </Form.Item>
              </Col>
              <Col span={8}>
                <Form.Item
                  name="strDistrict"
                  label="区县"
                  rules={[{ required: true, message: '请输入区县' }]}
                >
                  <Input placeholder="请输入区县，如：岳麓区" />
                </Form.Item>
              </Col>
            </Row>

            <Form.Item
              name="strAddress"
              label="详细地址"
              rules={[{ required: true, message: '请输入详细地址' }]}
            >
              <Input.TextArea 
                placeholder="请输入详细地址，如街道名称、门牌号等"
                rows={3}
              />
            </Form.Item>

            <Form.Item name="bDefault" valuePropName="checked">
              <Checkbox>设为默认地址</Checkbox>
            </Form.Item>
          </Form>
        </Modal>
      </div>
    );
  };

  const renderSettingsTab = () => {
    if (!user || !userSettings) return null;

    return (
      <div>
        <FormCard title="基本信息">
          <Form
            layout="vertical"
            initialValues={{
              strUserName: user.strUserName,
              strEmail: user.strEmail,
              strPhone: user.strPhone,
              strUserGender: user.strUserGender,
            }}
            onFinish={async (values) => {
              try {
                await updateProfile(values);
              } catch (error) {
                // Error is already handled in the context
              }
            }}
          >
            <Row gutter={16}>
              <Col span={24}>
                <AvatarUpload>
                  <Avatar size={100} icon={<User />} />
                  <div className="upload-button">
                    <Camera size={16} />
                  </div>
                </AvatarUpload>
              </Col>
              <Col span={12}>
                <Form.Item
                  name="strUserName"
                  label="昵称"
                  rules={[{ required: true, message: '请输入昵称' }]}
                >
                  <Input />
                </Form.Item>
              </Col>
              <Col span={12}>
                <Form.Item
                  name="strUserGender"
                  label="性别"
                >
                  <Radio.Group>
                    <Radio value="Male">男</Radio>
                    <Radio value="Female">女</Radio>
                    <Radio value="Unspecified">其他</Radio>
                  </Radio.Group>
                </Form.Item>
              </Col>
              <Col span={12}>
                <Form.Item
                  name="strPhone"
                  label="手机号码"
                  rules={[
                    { required: true, message: '请输入手机号码' },
                    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码' }
                  ]}
                >
                  <Input />
                </Form.Item>
              </Col>
              <Col span={12}>
                <Form.Item
                  name="strEmail"
                  label="邮箱"
                  rules={[
                    { required: true, message: '请输入邮箱' },
                    { type: 'email', message: '请输入正确的邮箱格式' }
                  ]}
                >
                  <Input />
                </Form.Item>
              </Col>
            </Row>
            <Form.Item>
              <Button type="primary" htmlType="submit">
                保存修改
              </Button>
            </Form.Item>
          </Form>
        </FormCard>

        <FormCard
          title={
            <Space>
              <Bell size={16} />
              通知设置
            </Space>
          }
        >
          <SettingSection>
            {Object.entries(userSettings.notifications).map(([key, value]) => (
              <SettingItem key={key}>
                <Text>
                  {key === 'email' ? '邮���通知' :
                   key === 'sms' ? '短信通知' :
                   key === 'promotions' ? '优惠活动通知' :
                   '订单更新通知'}
                </Text>
                <Switch
                  checked={value}
                  onChange={async (checked) => {
                    try {
                      await updateSettings({
                        notifications: { ...userSettings.notifications, [key]: checked }
                      });
                    } catch (error) {
                      // Error is already handled in the context
                    }
                  }}
                />
              </SettingItem>
            ))}
          </SettingSection>
        </FormCard>

        <FormCard
          title={
            <Space>
              <Eye size={16} />
              隐私设置
            </Space>
          }
        >
          <SettingSection>
            {Object.entries(userSettings.privacy).map(([key, value]) => (
              <SettingItem key={key}>
                <Text>
                  {key === 'showProfile' ? '公开个人资料' :
                   key === 'showOrders' ? '公开订单记录' :
                   '公开评价记录'}
                </Text>
                <Switch
                  checked={value}
                  onChange={async (checked) => {
                    try {
                      await updateSettings({
                        privacy: { ...userSettings.privacy, [key]: checked }
                      });
                    } catch (error) {
                      // Error is already handled in the context
                    }
                  }}
                />
              </SettingItem>
            ))}
          </SettingSection>
        </FormCard>

        <FormCard
          title={
            <Space>
              <Shield size={16} />
              安全设置
            </Space>
          }
        >
          <SettingSection>
            <SettingItem>
              <div>
                <Text strong>修改密码</Text>
                <div>
                  <Text type="secondary">上次修改时间: {userSettings.security.lastPasswordChange}</Text>
                </div>
              </div>
              <Button icon={<Lock size={16} />}>
                修改
              </Button>
            </SettingItem>
            <SettingItem>
              <div>
                <Text strong>两步验证</Text>
                <div>
                  <Text type="secondary">使用手机验证码进行双重认证</Text>
                </div>
              </div>
              <Switch
                checked={userSettings.security.twoFactorEnabled}
                onChange={async (checked) => {
                  try {
                    await updateSettings({
                      security: { ...userSettings.security, twoFactorEnabled: checked }
                    });
                  } catch (error) {
                    // Error is already handled in the context
                  }
                }}
              />
            </SettingItem>
          </SettingSection>
        </FormCard>
      </div>
    );
  };

  if (!user) {
    return <Empty description="请先登录" />;
  }

  return (
    <PageContainer>
      <BackButton />
      
      <ProfileHeader>
        <Row gutter={24} align="middle">
          <Col>
            <AvatarUpload>
              <Avatar size={80} icon={<User />} />
              <div className="upload-button">
                <Camera size={16} />
              </div>
            </AvatarUpload>
          </Col>
          <Col flex="1">
            <Title level={4} style={{ margin: 0 }}>{user?.strUserName}</Title>
            <Text type="secondary">{user?.strEmail}</Text>
          </Col>
          <Col>
            <Space>
              <Button icon={<Settings size={16} />}>
                账号设置
              </Button>
              <Button 
                icon={<LogOut size={16} />} 
                danger
                onClick={handleLogout}
              >
                退出登录
              </Button>
            </Space>
          </Col>
        </Row>
      </ProfileHeader>

      <Card>
        <Tabs activeKey={activeTab} onChange={setActiveTab}>
          <TabPane
            tab={<Space><Package size={16} />我的订单</Space>}
            key="orders"
          >
            {renderOrdersTab()}
          </TabPane>
          
          <TabPane
            tab={<Space><Heart size={16} />我的收藏</Space>}
            key="favorites"
          >
            {renderFavoritesTab()}
          </TabPane>
          
          <TabPane
            tab={<Space><MapPin size={16} />收货地址</Space>}
            key="addresses"
          >
            {renderAddressesTab()}
          </TabPane>
          
          <TabPane
            tab={<Space><Settings size={16} />账户设置</Space>}
            key="settings"
          >
            {renderSettingsTab()}
          </TabPane>
        </Tabs>
      </Card>
    </PageContainer>
  );
};

export default UserProfile;

