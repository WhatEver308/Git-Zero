package org.umlpractice.backend_fooddeliverysystem.pojo;
import jakarta.persistence.*;

/**
 * APIWhitelistItem 类说明
 * allows authentication-free access to correspond path
 *
 * @author 刘陈文君
 * @date 2025 /5/28 23:54
 */
@Table(name="tb_api_whitelist")
@Entity
public class APIWhitelistItem {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Integer iId;

    @Column(name="path")
    private String strPath;

    @Column(name="decription")
    private String strDescription;

    @Override
    public String toString() {
        return "APIWhitelistItem{" +
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
