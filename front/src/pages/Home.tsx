import React, { useEffect, useState } from 'react';
import { Typography, Row, Col, Carousel, Card, Button, Tabs, Space } from 'antd';
import { useNavigate } from 'react-router-dom';
import { ArrowRight, ChevronRight, MapPin, ThumbsUp, Clock, Tag as TagIcon } from 'lucide-react';
import styled from 'styled-components';
import SearchBar from '../components/SearchBar';
import MerchantCard from '../components/MerchantCard';
import { Merchant,vecRestauantsArray } from '../types';
import { customStyles } from '../styles/theme';
import { getRestaurants } from '../services/api_resteranuts';


const { Title, Text } = Typography;
const { TabPane } = Tabs;

const PageContainer = styled.div`
  max-width: 1200px;
  margin: 0 auto;
  padding: ${customStyles.spacing.lg};
  
  @media (max-width: 768px) {
    padding: ${customStyles.spacing.md};
  }
`;

const HeroBanner = styled.div`
  position: relative;
  width: 100%;
  height: 400px;
  margin-bottom: ${customStyles.spacing.xl};
  border-radius: 12px;
  overflow: hidden;
  box-shadow: ${customStyles.shadows.medium};
  
  @media (max-width: 768px) {
    height: 300px;
  }
`;

const HeroContent = styled.div`
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: ${customStyles.spacing.xl};
  background: linear-gradient(
    to right,
    rgba(0, 0, 0, 0.7) 0%,
    rgba(0, 0, 0, 0.4) 50%,
    rgba(0, 0, 0, 0) 100%
  );
  
  @media (max-width: 768px) {
    padding: ${customStyles.spacing.lg};
  }
`;

const HeroImage = styled.img`
  width: 100%;
  height: 100%;
  object-fit: cover;
`;

const SearchContainer = styled.div`
  margin: -80px auto 40px;
  max-width: 800px;
  z-index: 10;
  position: relative;
  
  @media (max-width: 768px) {
    margin-top: -40px;
  }
`;

const SectionTitle = styled(Title)`
  margin-bottom: ${customStyles.spacing.lg} !important;
  display: flex;
  justify-content: space-between;
  align-items: center;
`;

const ViewAllLink = styled(Button)`
  display: flex;
  align-items: center;
  
  svg {
    margin-left: 4px;
  }
`;

const CategoryCard = styled(Card)`
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  
  &:hover {
    transform: translateY(-4px);
    box-shadow: ${customStyles.shadows.medium};
  }
`;

const CategoryImage = styled.div`
  height: 140px;
  overflow: hidden;
  
  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    transition: transform 0.3s ease;
  }
  
  &:hover img {
    transform: scale(1.05);
  }
`;

const PromotionCard = styled(Card)`
  height: 200px;
  border-radius: 8px;
  overflow: hidden;
  background: linear-gradient(135deg, #00C73C 0%, #008c29 100%);
  color: white;
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: ${customStyles.spacing.lg};
  cursor: pointer;
  transition: transform 0.3s ease;
  
  &:hover {
    transform: translateY(-4px);
  }
`;

const ViewAllButton = styled(Button)`
  background-color: ${customStyles.colors.primary};
  border-color: ${customStyles.colors.primary};
  color: white;
  font-weight: 500;
  
  &:hover, &:focus {
    background-color: ${customStyles.colors.primary}cc !important;
    border-color: ${customStyles.colors.primary}cc !important;
    color: white !important;
  }
`;


// Mock data
// const mockMerchants: Merchant[] = [
//   {
//     iMerchantId: '1',
//     strMerchantName: '金龙餐厅',
//     strLogo_url: 'https://images.pexels.com/photos/941861/pexels-photo-941861.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2',
//     strCoverImage_url: 'https://images.pexels.com/photos/1640772/pexels-photo-1640772.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2',
//     strMerchantCategory: '中餐',
//     isNew: true
//   },
//   {
//     iMerchantId: '2',
//     strMerchantName: '意面天堂',
//     strLogo_url: 'https://images.pexels.com/photos/1438672/pexels-photo-1438672.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2',
//     strCoverImage_url: 'https://images.pexels.com/photos/1527603/pexels-photo-1527603.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2',
//     strMerchantCategory: '意大利菜',
//     isNew: true
//   },
//   {
//     iMerchantId: '3',
//     strMerchantName: '寿司速递',
//     strLogo_url: 'https://images.pexels.com/photos/359993/pexels-photo-359993.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2',
//     strCoverImage_url: 'https://images.pexels.com/photos/858508/pexels-photo-858508.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2',
//     strMerchantCategory: '日本料理',
//     isNew: true
//   },
//   {
//     iMerchantId: '4',
//     strMerchantName: '墨西哥风情',
//     strLogo_url: 'https://images.pexels.com/photos/2087748/pexels-photo-2087748.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2',
//     strCoverImage_url: 'https://images.pexels.com/photos/4958641/pexels-photo-4958641.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2',
//     strMerchantCategory: '墨西哥菜',
//     isNew: true
//   }
// ];

const categories = [
  { id: 1, name: '粤菜', image: 'https://images.pexels.com/photos/15355037/pexels-photo-15355037.jpeg' },
  { id: 2, name: '湘菜', image: 'https://images.pexels.com/photos/32789244/pexels-photo-32789244.jpeg' },
  { id: 3, name: '粥', image: 'https://images.pexels.com/photos/5652188/pexels-photo-5652188.jpeg' },
  { id: 4, name: '炖汤', image: 'https://images.pexels.com/photos/32795461/pexels-photo-32795461.jpeg' },
  { id: 5, name: '小炒', image: 'https://images.pexels.com/photos/31774666/pexels-photo-31774666.jpeg' },
  { id: 6, name: '海鲜', image: 'https://images.pexels.com/photos/566345/pexels-photo-566345.jpeg' },
  { id: 7, name: '点心', image: 'https://images.pexels.com/photos/2313695/pexels-photo-2313695.jpeg' },
  { id: 8, name: '甜品', image: 'https://images.pexels.com/photos/18869626/pexels-photo-18869626.jpeg' },
  {id : 9, name: '饮料', image:'https://images.pexels.com/photos/8879617/pexels-photo-8879617.jpeg' },
];

const Home: React.FC = () => {
  const navigate = useNavigate();
  const [activeTab, setActiveTab] = useState('nearby');
  const [merchants,setMerchants] = useState<Merchant[]>([]);
  //const [loading, setLoading] = useState<boolean>(true);
useEffect(() => {
  console.log('Fetching restaurants...1');
  //setLoading(true);
  getRestaurants({sort_by: 'name'}) // 传递排序参数
    .then(res => {
      console.log('Fetched restaurants:', res.vecRestauantsArray);
      setMerchants(res.vecRestauantsArray);
    })
    .catch(() => setMerchants([]))
  //.finally(() => setLoading(false));
}, []);
  
  const handleSearch = (keyword: string, filters: any) => {
    navigate('/merchants', { 
      state: { 
        keyword,
        filters
      }
    });
  };
  
  const handleCategoryClick = (category: string) => {
    navigate('/merchants', {
      state: {
        category // 直接传递category字段，和MerchantList.tsx一致
      }
    });
  };
  
  return (
    <div>
      <HeroBanner>
        <HeroImage src="https://images.pexels.com/photos/958545/pexels-photo-958545.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2" alt="美食外卖" />
        <HeroContent>
          <Title level={1} style={{ color: 'white', marginBottom: '16px' }}>
            美味佳肴，送到家门
          </Title>
          <Text style={{ color: 'white', fontSize: '18px', maxWidth: '600px' }}>
            从当地最好的餐厅订购，轻松实现随时随地的外卖服务。
          </Text>
        </HeroContent>
      </HeroBanner>
      
      <PageContainer>
        <SearchContainer>
          <SearchBar onSearch={handleSearch} />
        </SearchContainer>
        
        <Row gutter={[24, 24]}>
          <Col span={24}>
            <SectionTitle level={3}>
              美食分类
              <ViewAllLink type="link" onClick={() => navigate('/merchants')}>
                查看全部 <ChevronRight size={16} />
              </ViewAllLink>
            </SectionTitle>
            
            <Row gutter={[16, 16]}>
              {categories.map(category => (
                <Col xs={12} sm={8} md={6} lg={3} key={category.id}>
                  <CategoryCard
                    hoverable
                    cover={
                      <CategoryImage>
                        <img src={category.image} alt={category.name} />
                      </CategoryImage>
                    }
                    onClick={() => handleCategoryClick(category.name)}
                  >
                    <Card.Meta title={category.name} style={{ textAlign: 'center' }} />
                  </CategoryCard>
                </Col>
              ))}
            </Row>
          </Col>
          
          <Col span={24} style={{ marginTop: '32px' }}>
            <Tabs activeKey={activeTab} onChange={setActiveTab}>
              <TabPane tab="餐廳" key="nearby" />
            </Tabs>
            
            <Row gutter={[16, 16]}>
              {merchants.map(merchant => (
                <Col xs={24} sm={12} md={8} lg={6} key={merchant.iMerchantId}>
                  <MerchantCard merchant={merchant} />
                </Col>
              ))}
            </Row>
            
            <div style={{ textAlign: 'center', margin: '32px 0' }}>
              <ViewAllButton 
                type="primary" 
                size="large"
                onClick={() => navigate('/merchants')}
                icon={<ArrowRight size={16} />}
              >
                查看全部餐厅
              </ViewAllButton>
            </div>
          </Col>

        </Row>
      </PageContainer>
    </div>
  );
};

export default Home;
