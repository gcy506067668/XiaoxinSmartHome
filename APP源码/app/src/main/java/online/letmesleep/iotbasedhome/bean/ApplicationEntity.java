package online.letmesleep.iotbasedhome.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by Letmesleep on 2018/4/15.
 */

public class ApplicationEntity extends DataSupport{
    int id;
    String applicationId;              //控制命令码
    String applicationName;            //设备名称
    int applicationType;               //设备类别
    String status;                     //设备状态


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public int getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(int applicationType) {
        this.applicationType = applicationType;
    }
}
