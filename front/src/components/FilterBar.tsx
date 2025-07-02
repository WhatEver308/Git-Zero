import React, {useState} from 'react';
import { Radio, Space, Select, Button, Tag, Drawer } from 'antd';
import { SortAsc, X, Filter } from 'lucide-react';
import styled from 'styled-components';
import { customStyles } from '../styles/theme';
import {MerchantFullData} from "../types";
import { getRestaurants } from '../services/api_resteranuts';
const { Option } = Select;

interface FilterBarProps {
  onFilterChange: (key: string, value: string | null) => void;
  onSortChange: (value: string) => void;
  activeFilters: Record<string, string>;
  clearFilters: () => void;
}

const FilterContainer = styled.div`
  background-color: white;
  padding: ${customStyles.spacing.md};
  border-radius: 8px;
  box-shadow: ${customStyles.shadows.small};
  margin-bottom: ${customStyles.spacing.md};
  display: flex;
  justify-content: space-between;
  align-items: center;
`;

const FilterButton = styled(Button)`
  display: flex;
  align-items: center;
  gap: 8px;
`;

const FilterSection = styled.div`
  margin-bottom: ${customStyles.spacing.md};
`;

const SortSection = styled.div`
  display: flex;
  align-items: center;
  gap: ${customStyles.spacing.md};
`;

const FilterLabel = styled.div`
  font-weight: 500;
  margin-bottom: ${customStyles.spacing.sm};
  color: ${customStyles.colors.textPrimary};
`;

const ActiveFilters = styled.div`
  display: flex;
  flex-wrap: wrap;
  gap: ${customStyles.spacing.sm};
`;

const DrawerContent = styled.div`
  padding: ${customStyles.spacing.md} 0;
`;

const cuisineOptions = [
  { value: 'all', label: '全部' },
  { value: '中餐', label: '中餐' },
  { value: '湘菜', label: '湘菜' },
  { value: '粥', label: '粥' },
  { value: '炖汤', label: '炖汤' },
  { value: '小炒', label: '小炒' },
  { value: '海鲜', label: '海鲜' },
  { value: '点心', label: '点心' },
  { value: '甜品', label: '甜品' },
  { value: '饮料', label: '饮料' },
];

const FilterBar: React.FC<FilterBarProps> = ({
  onFilterChange,
  onSortChange,
  activeFilters,
  clearFilters
}) => {
  const [drawerVisible, setDrawerVisible] = React.useState(false);
  const [sortOrder, setSortOrder] = React.useState('名稱排序');
  const [loading, setLoading] = React.useState(false);
  const activeFilterCount = Object.values(activeFilters).filter(value => value && value !== 'all').length;
  const [filteredMerchants, setFilteredMerchants] = useState<MerchantFullData[]>([]);
  const getFilterLabel = (key: string, value: string) => {
    switch (key) {
      case 'cuisine':
        return cuisineOptions.find(opt => opt.value === value)?.label || value;
      case 'averagePrice':
        switch (value) {
          case '0-50': return '¥0-50';
          case '51-100': return '¥51-100';
          case '101-200': return '¥101-200';
          case '201+': return '¥201以上';
          default: return value;
        }
      case 'rating':
        return `${value}分以上`;
      case 'distance':
        switch (value) {
          case '0-1': return '1公里内';
          case '1-3': return '1-3公里';
          case '3-5': return '3-5公里';
          case '5+': return '5公里以上';
          default: return value;
        }
      default:
        return value;
    }
  };
  // 由父组件传入的 onSortChange 负责刷新页面和数据
  return (
    <FilterContainer>
      <SortSection>
        <SortAsc size={16} />
        <Select
          value={sortOrder}
          style={{ width: 150 }}
          onChange={value => {
            setSortOrder(value);
            onSortChange(value); // 通知父组件刷新数据
          }}
        >
          <Option value="popularity">推荐排序</Option>
          <Option value="rating">评分最高</Option>
          <Option value="name">名稱排序</Option>
        </Select>
      </SortSection>
    </FilterContainer>
  );
};

export default FilterBar;