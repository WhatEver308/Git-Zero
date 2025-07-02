import React, { useState, useEffect } from 'react';
import { 
  Typography, 
  Rate, 
  Tabs, 
  Button, 
  Space, 
  Divider, 
  Tag,
  Anchor,
  Collapse,
  Result,
  Affix,
  message
} from 'antd';
import { 
  CheckCircle, 
  Clock, 
  ChefHat, 
  Truck, 
  Home, 
  MapPin, 
  Phone,
  Navigation,
  AlertCircle,
  Share2,
  Heart
} from 'lucide-react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import styled from 'styled-components';
import MenuItemCard from '../components/MenuItemCard';
import LoadingSkeleton from '../components/LoadingSkeleton';
import { Merchant, MenuItem } from '../types';
import { customStyles } from '../styles/theme';
import { useFavorite } from '../contexts/FavoriteContext';
import { getMerchantById } from '../services/api_merchants';
import BackButton from '../components/BackButton';

const { Title, Text, Paragraph } = Typography;
const { TabPane } = Tabs;
const { Link: AnchorLink } = Anchor;
const { Panel } = Collapse;

const PageContainer = styled.div`
  max-width: 1200px;
  margin: 0 auto;
  padding: ${customStyles.spacing.lg};
  
  @media (max-width: 768px) {
    padding: ${customStyles.spacing.md};
  }
`;

const HeroContainer = styled.div`
  position: relative;
  height: 280px;
  overflow: hidden;
  border-radius: 8px;
  
  @media (max-width: 768px) {
    height: 200px;
  }
`;

const HeroImage = styled.img`
  width: 100%;
  height: 100%;
  object-fit: cover;
`;

const RestaurantInfoContainer = styled.div`
  max-width: 1200px;
  margin: 0 auto;
  padding: ${customStyles.spacing.lg};
  background: white;
  border-radius: 8px;
  margin-top: -60px;
  position: relative;
  box-shadow: ${customStyles.shadows.small};
  
  @media (max-width: 768px) {
    margin-top: -40px;
    padding: ${customStyles.spacing.md};
  }
`;

const RestaurantLogo = styled.div`
  position: absolute;
  top: -40px;
  left: ${customStyles.spacing.lg};
  width: 80px;
  height: 80px;
  border-radius: 50%;
  background: white;
  padding: 4px;
  overflow: hidden;
  box-shadow: ${customStyles.shadows.small};
  
  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    border-radius: 50%;
  }
  
  @media (max-width: 768px) {
    width: 60px;
    height: 60px;
    top: -30px;
    left: ${customStyles.spacing.md};
  }
`;

const ContentContainer = styled.div`
  display: flex;
  gap: ${customStyles.spacing.lg};
  margin-top: ${customStyles.spacing.lg};
  
  @media (max-width: 992px) {
    flex-direction: column;
  }
`;

const MainContent = styled.div`
  flex: 3;
`;

const SidebarContent = styled.div`
  flex: 1;
  
  @media (max-width: 992px) {
    order: -1;
  }
`;

const StyledCollapse = styled(Collapse)`
  background: white;
  border-radius: ${customStyles.borderRadius.lg};
  margin-bottom: ${customStyles.spacing.md};

  .ant-collapse-header {
    padding: ${customStyles.spacing.md} !important;
  }

  .ant-collapse-content-box {
    padding: ${customStyles.spacing.md} !important;
  }

  .ant-collapse-expand-icon {
    margin-right: ${customStyles.spacing.md} !important;
  }
`;

const CategoryHeader = styled.div`
  padding: ${customStyles.spacing.md} 0;
  background: white;
  margin-bottom: ${customStyles.spacing.md};
`;

const CategoryNavigation = styled.div`
  position: sticky;
  top: 0;
  z-index: 100;
  background: white;
  padding: ${customStyles.spacing.sm} 0;
  margin-bottom: ${customStyles.spacing.md};
  border-bottom: 1px solid ${customStyles.colors.border};
  
  @media (max-width: 768px) {
    overflow-x: auto;
    white-space: nowrap;
    -webkit-overflow-scrolling: touch;
    
    &::-webkit-scrollbar {
      display: none;
    }
  }
`;

const CategoryButton = styled(Button)<{ $active?: boolean }>`
  margin-right: ${customStyles.spacing.sm};
  border-radius: 16px;
  
  ${props => props.$active && `
    background: ${customStyles.colors.primary};
    color: white;
    border-color: ${customStyles.colors.primary};
    
    &:hover, &:focus {
      background: ${customStyles.colors.primary};
      color: white;
      border-color: ${customStyles.colors.primary};
      opacity: 0.9;
    }
  `}
  
  @media (max-width: 768px) {
    margin-bottom: 0;
  }
`;

const FavoriteButton = styled(Button)<{ $isFavorite: boolean }>`
  svg {
    color: ${props => props.$isFavorite ? '#ff4d4f' : 'inherit'};
    fill: ${props => props.$isFavorite ? '#ff4d4f' : 'none'};
  }
`;

const MerchantDetail: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(true);
  const [activeCollapse, setActiveCollapse] = useState<string[]>(['notice', 'delivery']);
  const [activeCategory, setActiveCategory] = useState<string>('');
  const { isFavorite, toggleFavorite } = useFavorite();

  const [merchant, setMerchant] = useState<Merchant | null>(null);
  const favorite = merchant ? isFavorite(merchant.iMerchantId.toString()) : false;

  // 从商家的菜单中提取分类
  const categories = merchant?.cuisine ?
    [...new Set(merchant.cuisine.map(item => item.striMenuItemCategory))] :
    [];

  useEffect(() => {
    const fetchMerchant = async () => {
      if (id) {
        try {
          setLoading(true);
          const data = await getMerchantById(id);
          setMerchant(data);
        } catch (error) {
          console.error('获取商家信息失败:', error);
          message.error('商家信息加载失败');
        } finally {
          setLoading(false);
        }
      }
    };

    fetchMerchant();
  }, [id]);

  useEffect(() => {
    if (categories.length > 0) {
      setActiveCategory(categories[0]);
    }
  }, [categories]);

  const scrollToCategory = (category: string) => {
    setActiveCategory(category);
    const element = document.getElementById(category);
    if (element) {
      const offset = 80;
      const elementPosition = element.getBoundingClientRect().top;
      const offsetPosition = elementPosition + window.pageYOffset - offset;
      
      window.scrollTo({
        top: offsetPosition,
        behavior: 'smooth'
      });
    }
  };

  useEffect(() => {
    const handleScroll = () => {
      const categoryElements = categories.map(category => ({
        category,
        element: document.getElementById(category)
      }));
      
      const currentCategory = categoryElements.find(({ element }) => {
        if (!element) return false;
        const rect = element.getBoundingClientRect();
        return rect.top <= 100 && rect.bottom > 100;
      });
      
      if (currentCategory) {
        setActiveCategory(currentCategory.category);
      }
    };
    
    window.addEventListener('scroll', handleScroll);
    return () => window.removeEventListener('scroll', handleScroll);
  }, [categories]);

  if (loading) {
    return (
      <PageContainer>
        <LoadingSkeleton type="detail" />
      </PageContainer>
    );
  }

  if (!merchant) {
    return (
      <PageContainer>
        <Result
          status="404"
          title="商家不存在"
          subTitle="抱歉，您访问的商家不存在或已下线"
          extra={
            <Button type="primary" onClick={() => navigate('/')}>
              返回首页
            </Button>
          }
        />
      </PageContainer>
    );
  }

  // 按分类组织菜品
  const menuItemsByCategory = categories.reduce((acc, category) => {
    acc[category] = merchant.cuisine.filter(item => item.striMenuItemCategory === category);
    return acc;
  }, {} as Record<string, MenuItem[]>);

  return (
    <div>
      <PageContainer>
        <BackButton />
        <HeroContainer>
          <HeroImage src={merchant.strCoverImage_url} alt={merchant.strMerchantName} />
        </HeroContainer>

        <RestaurantInfoContainer>
          <RestaurantLogo>
            <img src={merchant.strLogo_url} alt={merchant.strMerchantName} />
          </RestaurantLogo>

          <div style={{ marginTop: '24px' }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
              <Title level={2} style={{ margin: 0 }}>{merchant.strMerchantName}</Title>
              <Tag color={customStyles.colors.primary}>{merchant.strMerchantCategory}</Tag>
            </div>

            <Space align="center" style={{ marginTop: '8px' }}>
              <Rate disabled defaultValue={merchant.dRating || 0} style={{ fontSize: 14 }} />
              <Text strong>{merchant.dRating || 0}</Text>
              <Text type="secondary">(200+ 评价)</Text>
            </Space>

            <Space size={16} style={{ marginTop: '16px' }}>
              <Space>
                <Clock size={16} />
                <Text>{merchant.tDeliveryTime || 30} 分钟</Text>
              </Space>

              <Space>
                <ChefHat size={16} />
                <Text>人均 ¥{merchant.dAveragePrice || 0}</Text>
              </Space>

              {(merchant.dDeliveryFee || 0) === 0 ? (
                <Tag color="volcano">免配送费</Tag>
              ) : (
                <Text>配送费: ¥{(merchant.dDeliveryFee || 0).toFixed(2)}</Text>
              )}
            </Space>

            <Space style={{ marginTop: '16px' }}>
              <Button icon={<Share2 size={16} />}>
                分享
              </Button>
              <FavoriteButton
                icon={<Heart size={16} />}
                $isFavorite={favorite}
                onClick={() => toggleFavorite(merchant.iMerchantId.toString())}
              >
                {favorite ? '已收藏' : '收藏'}
              </FavoriteButton>
            </Space>
          </div>
        </RestaurantInfoContainer>

        <ContentContainer>
          <MainContent>
            <Affix offsetTop={0}>
              <CategoryNavigation>
                {categories.map(category => (
                  <CategoryButton
                    key={category}
                    $active={activeCategory === category}
                    onClick={() => scrollToCategory(category)}
                  >
                    {category}
                  </CategoryButton>
                ))}
              </CategoryNavigation>
            </Affix>

            {categories.map(category => (
              <div key={category} id={category}>
                <CategoryHeader>
                  <Title level={3}>{category}</Title>
                  <Divider style={{ margin: '12px 0' }} />
                </CategoryHeader>
                
                <div>
                  {menuItemsByCategory[category]?.map(item => (
                    <MenuItemCard
                      key={item.iMenuItemId}
                      item={item}
                      merchantId={merchant.iMerchantId.toString()}
                      merchantName={merchant.strMerchantName}
                    />
                  ))}
                </div>
              </div>
            ))}
          </MainContent>
          
          <SidebarContent>
            <StyledCollapse
              activeKey={activeCollapse}
              onChange={(keys) => setActiveCollapse(keys as string[])}
              expandIconPosition="start"
              items={[
                {
                  key: 'notice',
                  label: (
                    <Space>
                      <AlertCircle size={16} />
                      <Text strong>温馨提示</Text>
                    </Space>
                  ),
                  children: (
                    <Text type="secondary">
                      最低起送金额: ¥{merchant.dMinOder || 0}
                    </Text>
                  )
                },
                {
                  key: 'delivery',
                  label: (
                    <Space>
                      <Navigation size={16} />
                      <Text strong>配送信息</Text>
                    </Space>
                  ),
                  children: (
                    <Space direction="vertical">
                      <Text>配送时间: {merchant.tDeliveryTime || 30}分钟</Text>
                      <Text>配送费: ¥{(merchant.dDeliveryFee || 0).toFixed(2)}</Text>
                    </Space>
                  )
                }
              ]}
            />

            <div>
              <Title level={5}>菜品分类</Title>
              {categories.map(category => (
                <AnchorLink 
                  key={category} 
                  href={`#${category}`} 
                  title={category}
                />
              ))}
            </div>
          </SidebarContent>
        </ContentContainer>
      </PageContainer>
    </div>
  );
};

export default MerchantDetail;