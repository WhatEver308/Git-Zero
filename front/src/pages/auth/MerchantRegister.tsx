import React from 'react';
import { Form, Input, Button, Typography } from 'antd';
import { User, Lock, Phone } from 'lucide-react';
import styled from 'styled-components';
import { useMerchantAuth } from '../../contexts/MerchantAuthContext';
import { customStyles } from '../../styles/theme';
import { useNavigate } from 'react-router-dom';

const { Text } = Typography;

const StyledForm = styled(Form)`
  .ant-form-item {
    margin-bottom: ${customStyles.spacing.md};
  }
`;

const MerchantRegister: React.FC = () => {
  const { merchant_register } = useMerchantAuth();
  const [form] = Form.useForm();
  const [loading, setLoading] = React.useState(false);
  const navigate = useNavigate();

  const handleSubmit = async (values: {
    strMerchantName: string;
    strPassword: string;
    strPhone: string;
  }) => {
    setLoading(true);
    try {
      await merchant_register(values.strMerchantName, values.strPassword, values.strPhone);
      // 注册成功后导航到登录页面
      navigate('/login?tab=merchant');
    } catch (error) {
      // Error is handled in MerchantAuthContext
    } finally {
      setLoading(false);
    }
  };

  return (
    <StyledForm
      form={form}
      layout="vertical"
      onFinish={handleSubmit}
    >
      <Form.Item
        name="strMerchantName"
        rules={[{ required: true, message: '请输入商家名称' }]}
      >
        <Input 
          prefix={<User size={16} />} 
          placeholder="商家名称"
          size="large"
        />
      </Form.Item>

      <Form.Item
        name="strPhone"
        rules={[
          { required: true, message: '请输入手机号' },
          { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码' }
        ]}
      >
        <Input 
          prefix={<Phone size={16} />} 
          placeholder="手机号"
          size="large"
        />
      </Form.Item>

      <Form.Item
        name="strPassword"
        rules={[
          { required: true, message: '请输入密码' },
          { min: 6, message: '密码长度至少为6位' }
        ]}
      >
        <Input.Password 
          prefix={<Lock size={16} />} 
          placeholder="密码" 
          size="large"
        />
      </Form.Item>

      <Form.Item
        name="confirmPassword"
        dependencies={['strPassword']}
        rules={[
          { required: true, message: '请确认密码' },
          ({ getFieldValue }) => ({
            validator(_, value) {
              if (!value || getFieldValue('strPassword') === value) {
                return Promise.resolve();
              }
              return Promise.reject(new Error('两次输入的密码不一致'));
            },
          }),
        ]}
      >
        <Input.Password 
          prefix={<Lock size={16} />} 
          placeholder="确认密码" 
          size="large"
        />
      </Form.Item>

      <Form.Item>
        <Button 
          type="primary" 
          htmlType="submit" 
          block 
          size="large"
          loading={loading}
        >
          注册
        </Button>
      </Form.Item>

      <div style={{ textAlign: 'center' }}>
        <Text type="secondary" style={{ fontSize: '12px' }}>
          注册即表示您同意我们的商家服务条款和隐私政策
        </Text>
      </div>
    </StyledForm>
  );
};

export default MerchantRegister;