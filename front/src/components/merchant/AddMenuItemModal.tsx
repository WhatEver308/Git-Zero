import React from 'react';
import { Modal, Form, Input, InputNumber, Select, Upload, Button, message } from 'antd';
import { Plus, Upload as UploadIcon } from 'lucide-react';
import { MenuItem } from '../../types';
import { uploadImage } from '../../services/api_images';

const { Option } = Select;
const { TextArea } = Input;

interface AddMenuItemModalProps {
  visible: boolean;
  onCancel: () => void;
  onSubmit: (item: Omit<MenuItem, 'id'>) => Promise<void>;
}

const categories = [
  '推荐',
  '粤菜',
  '湘菜',
  '粥',
  '炖汤',
  '小炒',
  '海鲜',
  '点心',
  '甜品',
  '饮料'
];

const AddMenuItemModal: React.FC<AddMenuItemModalProps> = ({
  visible,
  onCancel,
  onSubmit
}) => {
  const [form] = Form.useForm();
  const [loading, setLoading] = React.useState(false);

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields();
      setLoading(true);
      
      const newItem: Omit<MenuItem, 'id'> = {
        name: values.name,
        description: values.description,
        price: values.price,
        category: values.category,
        image: values.image || 'https://images.pexels.com/photos/2611917/pexels-photo-2611917.jpeg',
        stock: values.stock || 0,
        isAvailable: true,
        isSpicy: values.isSpicy || false,
        isVegetarian: values.isVegetarian || false,
        isPopular: false
      };

      await onSubmit(newItem);
      form.resetFields();
      onCancel();
      message.success('菜品添加成功');
    } catch (error) {
      console.error('添加菜品失败:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleCancel = () => {
    form.resetFields();
    onCancel();
  };

  // 将文件转换为 base64 编码
  const convertToBase64 = (file: File): Promise<string> => {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => {
        const result = reader.result as string;
        // 移除 data:image/xxx;base64, 前缀
        const base64Data = result.split(',')[1];
        resolve(base64Data);
      };
      reader.onerror = error => reject(error);
    });
  };

  // 获取文件扩展名
  const getFileExtension = (file: File): string => {
    const fileName = file.name;
    return fileName.substring(fileName.lastIndexOf('.'));
  };

  const uploadProps = {
    name: 'file',
    showUploadList: false,
    beforeUpload: async (file: File) => {
      const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png';
      if (!isJpgOrPng) {
        message.error('只能上传 JPG/PNG 格式的图片!');
        return false;
      }
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isLt2M) {
        message.error('图片大小不能超过 2MB!');
        return false;
      }

      try {
        message.loading('正在上传图片...', 0);

        // 转换为 base64
        const base64Data = await convertToBase64(file);
        const fileExtension = getFileExtension(file);

        // 调用上传 API
        const response = await uploadImage({
          strImageSuffix: fileExtension,
          strImageBase64Data: base64Data
        });

        // 设置表单字段值
        form.setFieldsValue({ image: response.image_url });
        message.destroy();
        message.success(`${file.name} 上传成功`);
      } catch (error) {
        message.destroy();
        message.error(`${file.name} 上传失败`);
        console.error('图片上传失败:', error);
      }

      return false; // 阻止默认上传行为
    },
  };

  return (
    <Modal
      title="添加菜品"
      open={visible}
      onOk={handleSubmit}
      onCancel={handleCancel}
      confirmLoading={loading}
      width={600}
      okText="添加"
      cancelText="取消"
    >
      <Form
        form={form}
        layout="vertical"
        initialValues={{
          isAvailable: true,
          stock: 10
        }}
      >
        <Form.Item
          name="name"
          label="菜品名称"
          rules={[{ required: true, message: '请输入菜品名称' }]}
        >
          <Input placeholder="请输入菜品名称" />
        </Form.Item>

        <Form.Item
          name="category"
          label="菜品分类"
          rules={[{ required: true, message: '请选择菜品分类' }]}
        >
          <Select placeholder="请选择菜品分类">
            {categories.map(category => (
              <Option key={category} value={category}>
                {category}
              </Option>
            ))}
          </Select>
        </Form.Item>

        <Form.Item
          name="price"
          label="价格"
          rules={[{ required: true, message: '请输入价格' }]}
        >
          <InputNumber
            min={0}
            precision={2}
            style={{ width: '100%' }}
            placeholder="请输入价格"
            addonAfter="元"
          />
        </Form.Item>

        <Form.Item
          name="stock"
          label="库存"
          rules={[{ required: true, message: '请输入库存数量' }]}
        >
          <InputNumber
            min={0}
            style={{ width: '100%' }}
            placeholder="请���入库存数量"
          />
        </Form.Item>

        <Form.Item
          name="description"
          label="菜品描述"
          rules={[{ required: true, message: '请输入菜品描述' }]}
        >
          <TextArea
            rows={3}
            placeholder="请输入菜品描述"
          />
        </Form.Item>

        <Form.Item
          name="image"
          label="菜品图片"
        >
          <Upload {...uploadProps}>
            <Button icon={<UploadIcon size={16} />}>
              点击上传图片
            </Button>
          </Upload>
        </Form.Item>

        <Form.Item name="isSpicy" valuePropName="checked">
          <input type="checkbox" id="isSpicy" />
          <label htmlFor="isSpicy" style={{ marginLeft: '8px' }}>辣味菜品</label>
        </Form.Item>

        <Form.Item name="isVegetarian" valuePropName="checked">
          <input type="checkbox" id="isVegetarian" />
          <label htmlFor="isVegetarian" style={{ marginLeft: '8px' }}>素食菜品</label>
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default AddMenuItemModal;

