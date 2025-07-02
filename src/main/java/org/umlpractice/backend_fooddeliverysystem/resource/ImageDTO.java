package org.umlpractice.backend_fooddeliverysystem.resource;


/**
 * ImageDTO 类说明
 *
 * @author 刘陈文君
 * @date 2025/6/29 14:38
 */
public class ImageDTO {
    private String url;
    private Integer imageId;
    private String base64;
    public ImageDTO(Image image)
    {
        this.url = image.getStrImageDirectory()+String.valueOf(image.getiImageId())+image.getStrImageSuffix();
        this.imageId = image.getiImageId();
        this.base64 = image.getStrBase64Data();
    }

    @Override
    public String toString() {
        return "ImageDTO{" +
                "url='" + url + '\'' +
                ", imageId=" + imageId +
                ", base64='" + base64 + '\'' +
                '}';
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }
}
