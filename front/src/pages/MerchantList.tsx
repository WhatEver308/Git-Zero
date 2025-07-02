import React, { useState, useEffect } from 'react';
import { Typography, Row, Col, Pagination, Empty, Spin, Select, Radio, Space } from 'antd';
import styled from 'styled-components';
import { useLocation } from 'react-router-dom';
import SearchBar from '../components/SearchBar';
import FilterBar from '../components/FilterBar';
import MerchantCard from '../components/MerchantCard';
import LoadingSkeleton from '../components/LoadingSkeleton';
import { MerchantFullData } from '../types';
import { customStyles } from '../styles/theme';
import { MapPin } from 'lucide-react';
import { getRestaurants } from '../services/api_resteranuts';

// import axiosInstance from '../utils/axiosInstance';
// export const getMerchantDetail = async (id: string): Promise<IncompleteMerchant> => {
//   const res = await axiosInstance.get(`/api/restaurants/${id}`);
//   return res.data;
// };

const { Title, Text } = Typography;
const { Option } = Select;

// 定义和Home页一致的菜系选项
const categoryOptions = [
  { value: 'all', label: '全部' },
  { value: '粤菜', label: '粤菜' },
  { value: '湘菜', label: '湘菜' },
  { value: '粥', label: '粥' },
  { value: '炖汤', label: '炖汤' },
  { value: '小炒', label: '小炒' },
  { value: '海鲜', label: '海鲜' },
  { value: '点心', label: '点心' },
  { value: '甜品', label: '甜品' },
  { value: '饮料', label: '饮料' },
];

const PageContainer = styled.div`
  max-width: 1200px;
  margin: 0 auto;
  padding: ${customStyles.spacing.lg};
  
  @media (max-width: 768px) {
    padding: ${customStyles.spacing.md};
  }
`;

const HeaderSection = styled.div`
  margin-bottom: ${customStyles.spacing.lg};
`;

const LocationSelector = styled.div`
  display: flex;
  align-items: center;
  margin-bottom: ${customStyles.spacing.md};
  
  svg {
    color: ${customStyles.colors.primary};
    margin-right: ${customStyles.spacing.xs};
  }
`;

const ResultsHeader = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: ${customStyles.spacing.lg} 0;
  
  @media (max-width: 768px) {
    flex-direction: column;
    align-items: flex-start;
    gap: ${customStyles.spacing.sm};
  }
`;

const PaginationContainer = styled.div`
  margin-top: ${customStyles.spacing.xl};
  display: flex;
  justify-content: center;
`;

// Mock data with averagePrice added
// const mockMerchants: MerchantFullData[] = [
//   {
//     iMerchantId: '1',
//     strMerchantName: '金龙餐厅',
//     strLogo_url: 'https://images.pexels.com/photos/941861/pexels-photo-941861.jpeg',
//     strCoverImage_url: 'https://images.pexels.com/photos/1640772/pexels-photo-1640772.jpeg',
//     strMerchantCategory: '中餐',
//     cuisine: [
//       {
//         iMenuItemId: '1',
//         strMenuItemName: '宫保鸡丁',
//         strMenuItemDescription: '经典川菜，香辣可口',
//         dMenuItemPrice: 35,
//         strImage_url: 'https://images.pexels.com/photos/1640772/pexels-photo-1640772.jpeg',
//         striMenuItemCategory: '川菜'
//       },
//       {
//         iMenuItemId: '2',
//         strMenuItemName: '麻婆豆腐',
//         strMenuItemDescription: '麻辣鲜香，豆腐嫩滑',
//         dMenuItemPrice: 28,
//         strImage_url: 'https://images.pexels.com/photos/1640772/pexels-photo-1640772.jpeg',
//         striMenuItemCategory: '川菜'
//       }
//     ],
//     dstrRating: 4.5,
//     tDeliveryTime: 30,
//     dDeliveryFee: 2.99,
//     dMinOder: 20,
//     dAveragePrice: 80,
//     // rating: 4.8,
//     // deliveryTime: 25,
//     // deliveryFee: 2.99,
//     // minOrder: 15,
//     // distance: 1.2,
//     // promotions: [
//     //   { id: 'p1', type: 'discount', description: '新用户下单立减20%' }
//     // ],
//     isNew: true,
//   },
//   // {
//   //   iMerchantId: '2',
//   //   strMerchantName: '意面天堂',
//   //   strCoverImage_url: 'https://images.pexels.com/photos/1438672/pexels-photo-1438672.jpeg',
//   //   strLogo_url: 'https://images.pexels.com/photos/1527603/pexels-photo-1527603.jpeg',
//   //   strMerchantCategory: '意餐',
//   //   // rating: 4.5,
//   //   // deliveryTime: 30,
//   //   // deliveryFee: 1.99,
//   //   // minOrder: 20,
//   //   // distance: 2.4,
//   //   // promotions: [
//   //   //   { id: 'p2', type: 'freeDelivery', description: '订单满30元���配送费' }
//   //   // ],
//   //   // averagePrice: 120
//   // },
//   // {
//   //   iMerchantId: '3',
//   //   strMerchantName: '寿司速递',
//   //   strCoverImage_url: 'https://images.pexels.com/photos/359993/pexels-photo-359993.jpeg',
//   //   strLogo_url: 'https://images.pexels.com/photos/858508/pexels-photo-858508.jpeg',
//   //   strMerchantCategory: '日料',
//   //   // rating: 4.7,
//   //   // deliveryTime: 20,
//   //   // deliveryFee: 3.99,
//   //   // minOrder: 25,
//   //   // distance: 1.8,
//   //   // promotions: [
//   //   //   { id: 'p3', type: 'gift', description: '订单满35元赠送味增汤' }
//   //   // ],
//   //   // averagePrice: 150
//   // },
//   // {
//   //   id: '4',
//   //   name: '墨西哥风情',
//   //   logo: 'https://images.pexels.com/photos/2087748/pexels-photo-2087748.jpeg',
//   //   coverImage: 'https://images.pexels.com/photos/4958641/pexels-photo-4958641.jpeg',
//   //   cuisine: ['墨西哥菜', '拉美菜'],
//   //   rating: 4.3,
//   //   deliveryTime: 35,
//   //   deliveryFee: 0,
//   //   minOrder: 15,
//   //   distance: 3.1,
//   //   promotions: [],
//   //   averagePrice: 80
//   // }
// ];

const MerchantList: React.FC = () => {
  const locationHook = useLocation();
  // 处理Home页跳转带入的category
  const initialCategory = locationHook.state?.category || 'all';
  const [loading, setLoading] = useState(false);
  const [searchKeyword, setSearchKeyword] = useState('');
  const [activeFilters, setActiveFilters] = useState<Record<string, string>>({ category: initialCategory });
  const [sortOrder, setSortOrder] = useState('popularity');
  const [currentPage, setCurrentPage] = useState(1);
  const [location, setLocation] = useState('北京');
  const [filteredMerchants, setFilteredMerchants] = useState<MerchantFullData[]>([]);
  const [totalMerchants, setTotalMerchants] = useState(10); // 默认100��可根据后端返回设置


  useEffect(() => {
    setLoading(true);
    async function fetchMerchants() {
      try {
        let params: any = { sort_by: sortOrder || 'popularity', page: currentPage };
        if (searchKeyword && searchKeyword.trim() !== '') {
          params = { ...params, keyword: searchKeyword };
        }
        if (activeFilters.category && activeFilters.category !== 'all') {
          params = { ...params, category: activeFilters.category };
        }
        const res = await getRestaurants(params);
        const apiMerchants = res.vecRestauantsArray || [];
        setFilteredMerchants(apiMerchants);
        setTotalMerchants(res?.total || 100); // 记录总数
      } catch (e) {
        setFilteredMerchants([]);
        setTotalMerchants(0);
      } finally {
        setLoading(false);
      }
    }

    fetchMerchants();
  }, [activeFilters, sortOrder, searchKeyword, currentPage]);

  const handleSearch = (keyword: string, filters: any) => {
    setLoading(true);
    setCurrentPage(1); // 搜索时重置为第一页
    // 搜索时直接用当前activeFilters.category
    const category = activeFilters.category || 'all';
    setSearchKeyword(keyword);
    setActiveFilters(prev => ({ ...prev, ...filters, category }));
    let params: any = { sort_by: sortOrder, page: 1 };
    if (keyword && keyword.trim() !== '') {
      params = { ...params, keyword };
    }
    if (category && category !== 'all') {
      params = { ...params, category };
    }
    getRestaurants(params).then(res => {
      const apiMerchants = res.vecRestauantsArray || [];
      setFilteredMerchants(apiMerchants);
      setTotalMerchants(res?.total || 100);
      setLoading(false);
    });
  };

  const handleFilterChange = (key: string, value: string | null) => {
    let newFilters;
    if (value === null) {
      newFilters = { ...activeFilters };
      delete newFilters[key];
    } else {
      newFilters = { ...activeFilters, [key]: value };
    }
    setActiveFilters(newFilters);
    setCurrentPage(1);
    setLoading(true);
    // 立即发起请求并渲染
    let params: any = { sort_by: sortOrder, page: 1 };
    if (searchKeyword && searchKeyword.trim() !== '') {
      params = { ...params, keyword: searchKeyword };
    }
    if (newFilters.category && newFilters.category !== 'all') {
      params = { ...params, category: newFilters.category };
    }
    getRestaurants(params).then(res => {
      const apiMerchants = res.vecRestauantsArray || [];
      setFilteredMerchants(apiMerchants);
      setTotalMerchants(res?.total || 100);
      setLoading(false);
    });
  };

  const handleSortChange = (value: string) => {
    setSortOrder(value);
    // 判断有无关键词，返回不同的参数对象
    if (searchKeyword) {
      // 有关键词返回 { sort_by: value, keyword: searchKeyword }
      getRestaurants({ sort_by: value , keyword: searchKeyword }).then(res => {
        const apiMerchants = res.vecRestauantsArray || [];
        setFilteredMerchants(apiMerchants);
      });
    } else {
      // 无关键词，只返回 { sort_by: value }
      getRestaurants({ sort_by: value  }).then(res => {
        const apiMerchants = res.vecRestauantsArray || [];
        setFilteredMerchants(apiMerchants);
      });
    }
  };

  const clearFilters = () => {
    setActiveFilters({});
  };

  const handlePageChange = (page: number) => {
    setCurrentPage(page);
    window.scrollTo({top: 0, behavior: 'smooth'});
  };

  // 排序显示中文映射
  const sortOrderDisplay: Record<string, string> = {
    name: '名稱排序',
    rating: '评分排序',
    popularity: '推薦排序',
  };

  return (
      <PageContainer>
        <HeaderSection>
          <LocationSelector>
            <MapPin/>
            <Text>当前位置: {location}</Text>
          </LocationSelector>
          <SearchBar
              defaultValue={searchKeyword}
              onSearch={handleSearch}
              placeholder="搜索商家、菜品"
          />
        </HeaderSection>

        <div style={{ marginBottom: 24 }}>
          <Radio.Group
              onChange={e => handleFilterChange('category', e.target.value)}
              value={activeFilters.category || 'all'}
          >
            <Space wrap>
              {categoryOptions.map(option => (
                  <Radio.Button key={option.value} value={option.value}>
                    {option.label}
                  </Radio.Button>
              ))}
            </Space>
          </Radio.Group>
        </div>

        <FilterBar
            activeFilters={activeFilters}
            onFilterChange={handleFilterChange}
            onSortChange={handleSortChange}
            sortOrder={sortOrder}
            clearFilters={clearFilters}
        />

        {loading ? (
            <LoadingSkeleton/>
        ) : (
            <>
              {filteredMerchants.length > 0 ? (
                  <div>
                    <ResultsHeader>
                      <Title level={3}>
                        {searchKeyword || (activeFilters.category && activeFilters.category !== 'all') ? (
                            <>
                              {activeFilters.category && activeFilters.category !== 'all' && (
                                  <span>[菜系:{categoryOptions.find(c=>c.value===activeFilters.category)?.label}] </span>
                              )}
                              {searchKeyword && <>关键词「{searchKeyword}」</>}
                              搜索結果（{sortOrderDisplay [sortOrder]}）: <Text strong>{filteredMerchants.length}</Text> 家商家
                            </>
                        ) : (
                            <>排序「{sortOrderDisplay[sortOrder] || sortOrder}」结果: <Text strong>{filteredMerchants.length}</Text> 家商家</>
                        )}
                      </Title>
                    </ResultsHeader>
                    <Row gutter={[16, 16]}>
                      {filteredMerchants.map(merchant => (
                          <Col xs={24} sm={12} md={8} lg={6} key={merchant.iMerchantId}>
                            <MerchantCard merchant={merchant}/>
                          </Col>
                      ))}
                    </Row>
                    <PaginationContainer>
                      <Pagination
                          current={currentPage}
                          onChange={handlePageChange}
                          total={totalMerchants}
                          pageSize={10}
                          showSizeChanger={false}
                          showQuickJumper={false}
                          showLessItems={true}
                          pageSizeOptions={['10']}
                          hideOnSinglePage={true}
                          showTitle={false}
                          // 默认显示5个页码按钮
                          showPrevNextJumpers={false}
                          itemRender={(page, type, originalElement) => originalElement}
                          simple={false}
                      />
                    </PaginationContainer>
                  </div>
              ) : (
                  <Empty description={searchKeyword || (activeFilters.category && activeFilters.category !== 'all')
                      ? `未找到与“${searchKeyword || categoryOptions.find(c=>c.value===activeFilters.category)?.label || ''}”相关的商家`
                      : '未找到相关商家'} />
              )}
            </>
        )}
      </PageContainer>
  );
}
export default MerchantList;
