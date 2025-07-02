package org.umlpractice.backend_fooddeliverysystem.pojo.DTO;


/**
 * IPWhitelistItemDTO 类说明
 *
 * @author 刘陈文君
 * @date 2025 /5/29 14:58
 */
public class IPWhitelistItemDTO {
    private Integer iId;

    private String strIP;

    private String strDescription;

    @Override
    public String toString() {
        return "IPWhitelistItemDTO{" +
                "iId=" + iId +
                ", strIP='" + strIP + '\'' +
                ", strDescription='" + strDescription + '\'' +
                '}';
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public Integer getiId() {
        return iId;
    }

    /**
     * Sets id.
     *
     * @param iId the id
     */
    public void setiId(Integer iId) {
        this.iId = iId;
    }

    /**
     * Gets str ip.
     *
     * @return the str ip
     */
    public String getStrIP() {
        return strIP;
    }

    /**
     * Sets str ip.
     *
     * @param strIP the str ip
     */
    public void setStrIP(String strIP) {
        this.strIP = strIP;
    }

    /**
     * Gets str description.
     *
     * @return the str description
     */
    public String getStrDescription() {
        return strDescription;
    }

    /**
     * Sets str description.
     *
     * @param strDescription the str description
     */
    public void setStrDescription(String strDescription) {
        this.strDescription = strDescription;
    }
}
