import axiosInstance from "../utils/axiosInstance";

export interface ImageUploadRequest {
  strImageSuffix: string;
  strImageBase64Data: string;
}

export interface ImageUploadResponse {
  image_url: string;
}

export interface ImageBase64Response {
  strImageSuffix: string;
  strImageBase64Data: string;
}

export const uploadImage = async (imageData: ImageUploadRequest): Promise<ImageUploadResponse> => {
  const response = await axiosInstance.post<ImageUploadResponse>('/api/resource/images', imageData);
  return response.data;
};

export const getImageBase64 = async (imageUrl: string): Promise<ImageBase64Response> => {
  const response = await axiosInstance.get<ImageBase64Response>(`/api/resource/images/${encodeURIComponent(imageUrl)}`);
  return response.data;
};
