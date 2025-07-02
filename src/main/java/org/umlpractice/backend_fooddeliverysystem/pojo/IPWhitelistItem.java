package org.umlpractice.backend_fooddeliverysystem.pojo;


import jakarta.persistence.*;

/**
 * IpWhiteListItem 类说明
 * allows authentication-free access to all paths for corresponding IP address
 *
 * @author 刘陈文君
 * @date 2025 /5/28 23:49
 */
@Table(name="tb_ip_whitelist")
@Entity
public class IPWhitelistItem {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Integer iId;

    @Column(name="ip_address")
    private String strIP;

    @Column(name="description")
    private String strDescription;

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

    @Override
    public String toString() {
        return "IPWhitelistItem{" +
                "iId=" + iId +
                ", strIP='" + strIP + '\'' +
                ", strDescription='" + strDescription + '\'' +
                '}';
    }
}
