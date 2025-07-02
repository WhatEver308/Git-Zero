import React, {useEffect, useState} from 'react';
import {
    Avatar,
    Button,
    Card,
    Col,
    Layout,
    Menu,
    Popconfirm,
    Result,
    Row,
    Space,
    Spin,
    Statistic,
    Table,
    Tag,
    Typography
} from 'antd';
import {DollarSign, Edit, LogOut, Package, Plus, Settings, ShoppingCart, Store, Trash2, TrendingUp} from 'lucide-react';
import {useLocation, useNavigate} from 'react-router-dom';
import styled from 'styled-components';
import {useMerchantAuth} from '../../contexts/MerchantAuthContext';
import {MenuItem} from '../../types';
import {customStyles} from '../../styles/theme';
import AddMenuItemModal from '../../components/merchant/AddMenuItemModal';
import EditMenuItemModal from '../../components/merchant/EditMenuItemModal';
import MerchantInfoCard from '../../components/merchant/MerchantInfoCard';
import OrdersManagement from './OrdersManagement';
import Base64Image from '../../components/Base64Image';
import {useAddMenuItem, useDeleteMenuItem, useMenu, useUpdateMenuItem} from '../../hooks/useMenu';

const {Title, Text} = Typography;
const {Sider, Content} = Layout;

const PageContainer = styled.div`
    min-height: 100vh;
    background-color: #f0f2f5;
`;

const StyledLayout = styled(Layout)`
    min-height: 100vh;
`;

const StyledSider = styled(Sider)`
    background: white;
    box-shadow: 2px 0 8px rgba(0, 0, 0, 0.1);
`;

const HeaderCard = styled(Card)`
    margin-bottom: ${customStyles.spacing.lg};
    background: linear-gradient(135deg, #1890ff 0%, #722ed1 100%);
    color: white;

    .ant-card-body {
        padding: ${customStyles.spacing.lg};
    }

    .ant-statistic-title {
        color: rgba(255, 255, 255, 0.85) !important;
    }

    .ant-statistic-content {
        color: white !important;
    }
`;

const ContentContainer = styled.div`
    padding: ${customStyles.spacing.lg};

    @media (max-width: 768px) {
        padding: ${customStyles.spacing.md};
    }
`;

const ActionButton = styled(Button)`
    display: flex;
    align-items: center;
    gap: 4px;
`;

const StyledTable = styled(Table)`
    .ant-table-thead > tr > th {
        background-color: #fafafa;
        font-weight: 600;
    }
`;

const StyledMenu = styled(Menu)`
    border-right: none;

    .ant-menu-item {
        display: flex;
        align-items: center;
        gap: 8px;
        margin: 4px 0;
        border-radius: 6px;
    }

    .ant-menu-item-selected {
        background-color: #e6f7ff;
        color: ${customStyles.colors.primary};
    }
`;

const LogoSection = styled.div`
    padding: ${customStyles.spacing.lg};
    text-align: center;
    border-bottom: 1px solid #f0f0f0;
    margin-bottom: ${customStyles.spacing.md};
`;

const LoadingContainer = styled.div`
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 400px;
`;

const MerchantDashboard: React.FC = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const {merchant, merchant_logout} = useMerchantAuth();
    const [addModalVisible, setAddModalVisible] = useState(false);
    const [editModalVisible, setEditModalVisible] = useState(false);
    const [editingItem, setEditingItem] = useState<MenuItem | null>(null);
    const [collapsed, setCollapsed] = useState(false);
    const [currentPage, setCurrentPage] = useState('menu');

    // Use the new hooks
    const {data: menuItems = [], isLoading, error, refetch} = useMenu(merchant?.iMerchantId.toString());
    const addMenuItemMutation = useAddMenuItem(merchant?.iMerchantId.toString());
    const updateMenuItemMutation = useUpdateMenuItem(merchant?.iMerchantId.toString());
    const deleteMenuItemMutation = useDeleteMenuItem(merchant?.iMerchantId.toString());

    useEffect(() => {
        if (!merchant) {
            navigate('/login?tab=merchant');
            return;
        }

        // Get current page from URL
        const path = location.pathname;
        if (path.includes('center')) {
            setCurrentPage('center');
        } else if (path.includes('orders')) {
            setCurrentPage('order-management');
        } else if (path.includes('menu')) {
            setCurrentPage('menu');
        } else if (path.includes('settings')) {
            setCurrentPage('settings');
            console.log(path);
        }
    }, [merchant, navigate, location]);

    const handleAddItem = async (item: Omit<MenuItem, 'iMenuItemId'>) => {
        await addMenuItemMutation.mutateAsync(item);
        setAddModalVisible(false);
    };

    const handleEditItem = async (id: string, item: Partial<MenuItem>) => {
        await updateMenuItemMutation.mutateAsync({itemId: id, item});
        setEditModalVisible(false);
        setEditingItem(null);
    };

    const handleDeleteItem = async (id: string) => {
        await deleteMenuItemMutation.mutateAsync(id);
    };

    const handleAvailabilityChange = async (id: string, isAvailable: boolean) => {
        // 注意：新的MenuItem接口中没有isAvailable字段，这里需要根据实际情况调整
        await updateMenuItemMutation.mutateAsync({
            itemId: id,
            item: {...item} // 需要根据���际的MenuItem字段调整
        });
    };

    const handleLogout = () => {
        merchant_logout();
    };

    const handleMenuClick = (key: string) => {
        setCurrentPage(key);
        if (key === 'settings') {
            navigate('/merchant/settings');
        } else if (key === 'order-management') {
            navigate('/merchant/orders');
        } else {
            navigate('/merchant/dashboard');
        }
    };

    const menuConfig = [
        {
            key: 'menu',
            icon: <Package size={16}/>,
            label: '菜品管理',
        },
        {
            key: 'order-management',
            icon: <ShoppingCart size={16}/>,
            label: '订单管理',
        },

        {
            key: 'settings',
            icon: <Settings size={16}/>,
            label: '店铺设置',
        },
    ];

    const columns = [
        {
            title: '菜品图片',
            dataIndex: 'strImage_url',
            key: 'strImage_url',
            width: 80,
            render: (image: string, record: MenuItem) => (
                <Base64Image
                    imageUrl={image}
                    alt={record.strMenuItemName}
                    size={60}
                    shape="square"
                    fallbackSrc="https://images.pexels.com/photos/2611917/pexels-photo-2611917.jpeg"
                />
            ),
        },
        {
            title: '菜品名称',
            dataIndex: 'strMenuItemName',
            key: 'strMenuItemName',
            render: (name: string) => (
                <div>
                    <Text strong>{name}</Text>
                </div>
            ),
        },
        {
            title: '分类',
            dataIndex: 'striMenuItemCategory',
            key: 'striMenuItemCategory',
            render: (category: string) => <Tag color="blue">{category}</Tag>,
        },
        {
            title: '价格',
            dataIndex: 'dMenuItemPrice',
            key: 'dMenuItemPrice',
            render: (price: number) => <Text strong>¥{price.toFixed(2)}</Text>,
        },
        {
            title: '描述',
            dataIndex: 'strMenuItemDescription',
            key: 'strMenuItemDescription',
            render: (description: string) => (
                <Text ellipsis={{tooltip: true}} style={{maxWidth: 200}}>
                    {description}
                </Text>
            ),
        },
        {
            title: '操作',
            key: 'actions',
            render: (_: any, record: MenuItem) => (
                <Space>
                    <ActionButton
                        type="text"
                        icon={<Edit size={16}/>}
                        onClick={() => {
                            setEditingItem(record);
                            setEditModalVisible(true);
                        }}
                    >
                        编辑
                    </ActionButton>
                    <Popconfirm
                        title="确定要删除这个菜品吗？"
                        description="删除后无法恢复"
                        onConfirm={() => handleDeleteItem(record.iMenuItemId.toString())}
                        okText="确定"
                        cancelText="取消"
                    >
                        <ActionButton
                            type="text"
                            danger
                            icon={<Trash2 size={16}/>}
                            loading={deleteMenuItemMutation.isPending}
                        >
                            删除
                        </ActionButton>
                    </Popconfirm>
                </Space>
            ),
        },
    ];

    if (!merchant) {
        return null;
    }

    const totalItems = menuItems.length;
    const totalValue = menuItems.reduce((sum, item) => sum + item.dMenuItemPrice, 0);

    const renderContent = () => {
        // if (currentPage === 'center') {
        //   return <MerchantCenter />;
        // }

        if (currentPage == "order-management") {
            return (
                <div style={{textAlign: 'center', padding: '100px 0'}}>
                    <OrdersManagement/>
                </div>
            );
        }

        if (currentPage === 'analytics') {
            return (
                <div style={{textAlign: 'center', padding: '100px 0'}}>
                    <Title level={3}>数据分析</Title>
                    <Text type="secondary">功能开发中...</Text>
                </div>
            );
        }

        if (currentPage === 'settings') {
            return (
                <div style={{textAlign: 'center', padding: '100px 0'}}>
                    <MerchantInfoCard merchant={merchant}/>
                </div>
            );
        }

        // Default menu management page
        return (
            <>
                <HeaderCard>
                    <Row gutter={24} align="middle">
                        <Col>
                            <Avatar
                                size={80}
                                src={merchant.strLogo_url}
                                icon={<Store/>}
                            />
                        </Col>
                        <Col flex="1">
                            <Title level={2} style={{color: 'white', margin: 0}}>
                                {merchant.strMerchantName}
                            </Title>
                            <Text style={{color: 'rgba(255, 255, 255, 0.85)'}}>
                                欢迎来到商家管理后台
                            </Text>
                        </Col>
                        <Col>
                            <ActionButton
                                icon={<LogOut size={16}/>}
                                onClick={handleLogout}
                                style={{
                                    background: 'rgba(255, 255, 255, 0.2)',
                                    color: 'white',
                                    border: '1px solid rgba(255, 255, 255, 0.3)'
                                }}
                            >
                                退出登录
                            </ActionButton>
                        </Col>
                    </Row>

                    <Row gutter={24} style={{marginTop: customStyles.spacing.lg}}>
                        <Col xs={24} sm={8}>
                            <Statistic
                                title="菜品总数"
                                value={totalItems}
                                prefix={<Package size={20}/>}
                            />
                        </Col>
                        <Col xs={24} sm={8}>
                            <Statistic
                                title="商家评分"
                                value={merchant.dRating}
                                precision={1}
                                prefix={<TrendingUp size={20}/>}
                                suffix="分"
                            />
                        </Col>
                        <Col xs={24} sm={8}>
                            <Statistic
                                title="菜品总价值"
                                value={totalValue}
                                precision={2}
                                prefix={<DollarSign size={20}/>}
                                suffix="元"
                            />
                        </Col>
                    </Row>
                </HeaderCard>

                <Card>
                    <div style={{
                        display: 'flex',
                        justifyContent: 'space-between',
                        alignItems: 'center',
                        marginBottom: customStyles.spacing.lg
                    }}>
                        <Title level={3} style={{margin: 0}}>菜品管理</Title>
                        <ActionButton
                            type="primary"
                            icon={<Plus size={16}/>}
                            onClick={() => setAddModalVisible(true)}
                            loading={addMenuItemMutation.isPending}
                        >
                            新增菜品
                        </ActionButton>
                    </div>

                    {isLoading ? (
                        <LoadingContainer>
                            <Spin size="large"/>
                        </LoadingContainer>
                    ) : error ? (
                        <Result
                            status="error"
                            title="菜品加载失败"
                            subTitle="请稍后重试"
                            extra={
                                <Button type="primary" onClick={() => refetch()}>
                                    重新加载
                                </Button>
                            }
                        />
                    ) : (
                        <StyledTable
                            columns={columns as any}
                            dataSource={menuItems}
                            rowKey="iMenuItemId"
                            pagination={{
                                pageSize: 10,
                                showSizeChanger: true,
                                showQuickJumper: true,
                                showTotal: (total, range) =>
                                    `第 ${range[0]}-${range[1]} 条，共 ${total} 条记录`,
                            }}
                            scroll={{x: 800}}
                        />
                    )}
                </Card>

                <AddMenuItemModal
                    visible={addModalVisible}
                    onCancel={() => setAddModalVisible(false)}
                    onSubmit={handleAddItem}
                />

                <EditMenuItemModal
                    visible={editModalVisible}
                    item={editingItem}
                    onCancel={() => {
                        setEditModalVisible(false);
                        setEditingItem(null);
                    }}
                    onSubmit={handleEditItem}
                />
            </>
        );
    };

    return (
        <PageContainer>
            <StyledLayout>
                <StyledSider
                    collapsible
                    collapsed={collapsed}
                    onCollapse={setCollapsed}
                    width={250}
                    collapsedWidth={80}
                >
                    <LogoSection>
                        <Avatar
                            size={collapsed ? 40 : 60}
                            src={merchant.strLogo_url}
                            icon={<Store/>}
                        />
                        {!collapsed && (
                            <div style={{marginTop: 8}}>
                                <Text strong style={{color: customStyles.colors.textPrimary}}>
                                    {merchant.strMerchantName}
                                </Text>
                            </div>
                        )}
                    </LogoSection>

                    <StyledMenu
                        mode="inline"
                        selectedKeys={[currentPage]}
                        onClick={({key}) => handleMenuClick(key)}
                        items={menuConfig}
                    />
                </StyledSider>

                <Content>
                    <ContentContainer>
                        {renderContent()}
                    </ContentContainer>
                </Content>
            </StyledLayout>
        </PageContainer>
    );
};

export default MerchantDashboard;
