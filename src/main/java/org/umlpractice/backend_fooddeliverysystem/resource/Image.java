package org.umlpractice.backend_fooddeliverysystem.resource;


import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Value;

/**
 * Image 类说明
 *
 * @author 刘陈文君
 * @date 2025 /6/29 14:09
 */
@Table(name="tb_Image_base64")
@Entity
public class Image
{
    @Column(name="image_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    private Integer iImageId;

    @Column(name="image_directory")
    @Value("${app.image.url}")
    private String strImageDirectory;

    @Column(name="image_suffix")
    private String strImageSuffix;

    @Column(name="base64_data")
    private String strBase64Data;

    @Override
    public String toString() {
        return "Image{" +
                "iImageId=" + iImageId +
                ", strImageDirectory='" + strImageDirectory + '\'' +
                ", strImageSuffix='" + strImageSuffix + '\'' +
                ", strBase64Data='" + strBase64Data + '\'' +
                '}';
    }

    /**
     * Gets image id.
     *
     * @return the image id
     */
    public Integer getiImageId() {
        return iImageId;
    }

    /**
     * Sets image id.
     *
     * @param iImageId the image id
     */
    public void setiImageId(Integer iImageId) {
        this.iImageId = iImageId;
    }

    /**
     * Gets str image directory.
     *
     * @return the str image directory
     */
    public String getStrImageDirectory() {
        return strImageDirectory;
    }

    /**
     * Sets str image directory.
     *
     * @param strImageDirectory the str image directory
     */
    public void setStrImageDirectory(String strImageDirectory) {
        this.strImageDirectory = strImageDirectory;
    }

    /**
     * Gets str image suffix.
     *
     * @return the str image suffix
     */
    public String getStrImageSuffix() {
        return strImageSuffix;
    }

    /**
     * Sets str image suffix.
     *
     * @param strImageSuffix the str image suffix
     */
    public void setStrImageSuffix(String strImageSuffix) {
        this.strImageSuffix = strImageSuffix;
    }

    /**
     * Gets str base 64 data.
     *
     * @return the str base 64 data
     */
    public String getStrBase64Data() {
        return strBase64Data;
    }

    /**
     * Sets str base 64 data.
     *
     * @param strBase64Data the str base 64 data
     */
    public void setStrBase64Data(String strBase64Data) {
        this.strBase64Data = strBase64Data;
    }
}
