import React, { useState, useEffect } from 'react';
import { Avatar, Spin } from 'antd';
import { getImageBase64 } from '../services/api_images';

interface Base64ImageProps {
  imageUrl: string;
  alt?: string;
  size?: number;
  shape?: 'circle' | 'square';
  fallbackSrc?: string;
  style?: React.CSSProperties;
}

const Base64Image: React.FC<Base64ImageProps> = ({
  imageUrl,
  alt = '',
  size = 60,
  shape = 'square',
  fallbackSrc,
  style
}) => {
  const [base64Src, setBase64Src] = useState<string>('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(false);

  useEffect(() => {
    const loadBase64Image = async () => {
      if (!imageUrl) {
        setLoading(false);
        setError(true);
        return;
      }

      try {
        setLoading(true);
        setError(false);

        const response = await getImageBase64(imageUrl);
        const base64DataUrl = `data:image${response.strImageSuffix};base64,${response.strImageBase64Data}`;
        setBase64Src(base64DataUrl);
      } catch (err) {
        console.error('Failed to load base64 image:', err);
        setError(true);
        if (fallbackSrc) {
          setBase64Src(fallbackSrc);
        }
      } finally {
        setLoading(false);
      }
    };

    loadBase64Image();
  }, [imageUrl, fallbackSrc]);

  if (loading) {
    return (
      <div style={{
        width: style?.width || size,
        height: style?.height || size,
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        border: '1px solid #f0f0f0',
        borderRadius: shape === 'circle' ? '50%' : '6px',
        ...style
      }}>
        <Spin size="small" />
      </div>
    );
  }

  if (error && !fallbackSrc) {
    return (
      <Avatar
        size={size}
        shape={shape}
        style={{ backgroundColor: '#f5f5f5', color: '#999', ...style }}
      >
        ?
      </Avatar>
    );
  }

  // 对于大尺寸图片，使用 img 标签而不是 Avatar
  if ((style?.width && style?.height) || size > 100) {
    return (
      <img
        src={base64Src}
        alt={alt}
        style={{
          width: style?.width || size,
          height: style?.height || size,
          objectFit: 'cover',
          borderRadius: shape === 'circle' ? '50%' : '6px',
          ...style
        }}
      />
    );
  }

  return (
    <Avatar
      src={base64Src}
      alt={alt}
      size={size}
      shape={shape}
      style={style}
    />
  );
};

export default Base64Image;
