package org.umlpractice.backend_fooddeliverysystem.pojo.DTO;


/**
 * APIWhitelistItemDTO 类说明
 *
 * @author 刘陈文君
 * @date 2025 /5/29 14:57
 */
public class APIWhitelistItemDTO {
    private Integer iId;

    private String strPath;

    private String strDescription;

    @Override
    public String toString() {
        return "APIWhitelistItemDTO{" +
                "iId=" + iId +
                ", strPath='" + strPath + '\'' +
                ", strDescription='" + strDescription + '\'' +
                '}';
    }

    public Integer getiId() {
        return iId;
    }

    public void setiId(Integer iId) {
        this.iId = iId;
    }

    public String getStrPath() {
        return strPath;
    }

    public void setStrPath(String strPath) {
        this.strPath = strPath;
    }

    public String getStrDescription() {
        return strDescription;
    }

    public void setStrDescription(String strDescription) {
        this.strDescription = strDescription;
    }
}
