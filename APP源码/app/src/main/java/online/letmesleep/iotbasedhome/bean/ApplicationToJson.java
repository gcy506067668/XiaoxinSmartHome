package online.letmesleep.iotbasedhome.bean;

/**
 * Created by Letmesleep on 2018/5/8.
 */

public class ApplicationToJson {
    String applicationId;              //控制命令码
    String applicationName;            //设备名称
    int applicationType;               //设备类别

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
