import React, { useState } from 'react';
import { Card, Descriptions, Avatar, Button, Modal, Form, Input, InputNumber, message } from 'antd';
import { Merchant } from '../../types';
import { updateMerchantProfile } from '../../services/api_merchants';

interface Props {
  merchant: Merchant;
}

const MerchantInfoCard: React.FC<Props> = ({ merchant }) => {
  const [modalVisible, setModalVisible] = useState(false);
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);

  const handleEdit = () => {
    form.setFieldsValue({
      strMerchantName: merchant.strMerchantName,
      strMerchantCategory: merchant.strMerchantCategory,
      strLogo_url: merchant.strLogo_url,
      strCoverImage_url: merchant.strCoverImage_url,
      dRating: merchant.dRating,
      tDeliveryTime: merchant.tDeliveryTime,
      dDeliveryFee: merchant.dDeliveryFee,
      dMinOder: merchant.dMinOder,
      dAveragePrice: merchant.dAveragePrice,
    });
    setModalVisible(true);
  };

  const handleOk = async () => {
    try {
      setLoading(true);
      const values = await form.validateFields();
      await updateMerchantProfile(merchant.iMerchantId.toString(), values);
      message.success('店铺信息已更新');
      setModalVisible(false);
    } catch (e) {
      // 错误已在api层处理
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <Card
        title="店铺信息"
        style={{ maxWidth: 600, margin: '0 auto' }}
        extra={<Button onClick={handleEdit}>修改</Button>}
      >
        <div style={{ display: 'flex', alignItems: 'center', marginBottom: 24 }}>
          <Avatar src={merchant.strLogo_url} size={64} style={{ marginRight: 24 }} />
          <div>
            <div style={{ fontSize: 20, fontWeight: 600 }}>{merchant.strMerchantName}</div>
            <div style={{ color: '#888' }}>{merchant.strMerchantCategory}</div>
          </div>
        </div>
        <Descriptions column={1} bordered size="small">
          <Descriptions.Item label="店铺ID">{merchant.iMerchantId}</Descriptions.Item>
          <Descriptions.Item label="封面">
            <Avatar src={merchant.strCoverImage_url} shape="square" size={80} />
          </Descriptions.Item>
          <Descriptions.Item label="评分">{merchant.dRating} 分</Descriptions.Item>
          <Descriptions.Item label="预计送达时间">{merchant.tDeliveryTime} 分钟</Descriptions.Item>
          <Descriptions.Item label="配送费">¥{merchant.dDeliveryFee}</Descriptions.Item>
          <Descriptions.Item label="起送价">¥{merchant.dMinOder}</Descriptions.Item>
          <Descriptions.Item label="人均消费">¥{merchant.dAveragePrice}</Descriptions.Item>
        </Descriptions>
      </Card>
      <Modal
        title="修改店铺信息"
        open={modalVisible}
        onOk={handleOk}
        onCancel={() => setModalVisible(false)}
        confirmLoading={loading}
        destroyOnClose
      >
        <Form form={form} layout="vertical">
          <Form.Item name="strMerchantName" label="店铺名称" rules={[{ required: true, message: '请输入店铺名称' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="strMerchantCategory" label="店铺分类" rules={[{ required: true, message: '请输入分类' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="strLogo_url" label="Logo链接" rules={[{ required: true, message: '请输入Logo链接' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="strCoverImage_url" label="封面链接" rules={[{ required: true, message: '请输入封面链接' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="dRating" label="评分" rules={[{ required: true, message: '请输入评分' }]}>
            <InputNumber min={0} max={5} step={0.1} style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item name="tDeliveryTime" label="预计送达时间(分钟)" rules={[{ required: true, message: '请输入送达时间' }]}>
            <InputNumber min={0} style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item name="dDeliveryFee" label="配送费(元)" rules={[{ required: true, message: '请输入配送费' }]}>
            <InputNumber min={0} step={0.01} style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item name="dMinOder" label="起送价(元)" rules={[{ required: true, message: '请输入起送价' }]}>
            <InputNumber min={0} step={0.01} style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item name="dAveragePrice" label="人均消费(元)" rules={[{ required: true, message: '请输入人均消费' }]}>
            <InputNumber min={0} step={0.01} style={{ width: '100%' }} />
          </Form.Item>
        </Form>
      </Modal>
    </>
  );
};

export default MerchantInfoCard;
