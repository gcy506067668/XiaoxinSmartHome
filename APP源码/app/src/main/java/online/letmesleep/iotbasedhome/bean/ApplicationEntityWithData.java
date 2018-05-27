package online.letmesleep.iotbasedhome.bean;



import com.github.mikephil.charting.data.Entry;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Letmesleep on 2018/4/15.
 */

public class ApplicationEntityWithData extends DataSupport{
    int id;
    String applicationId;              //控制命令码
    String applicationName;            //设备名称
    int applicationType;               //设备类别
    String status;                     //设备状态
    List<Entry> lineData;
    String currentValue;
    String limithigh;
    String limitlow;

    public String getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(String currentValue) {
        this.currentValue = currentValue;
    }

    public String getLimithigh() {
        return limithigh;
    }

    public void setLimithigh(String limithigh) {
        this.limithigh = limithigh;
    }

    public String getLimitlow() {
        return limitlow;
    }

    public void setLimitlow(String limitlow) {
        this.limitlow = limitlow;
    }

    public ApplicationEntityWithData(ApplicationEntity applicationEntity, List<Entry> lineData){
        this.id = applicationEntity.getId();
        this.applicationId = applicationEntity.getApplicationId();
        this.applicationName = applicationEntity.getApplicationName();
        this.applicationType = applicationEntity.getApplicationType();
        this.lineData = lineData;
    }

    public ApplicationEntityWithData(){

    }

    public List<Entry> getLineData() {
        if(lineData==null)
            return new ArrayList<Entry>();
        return lineData;
    }

    public void setLineData(List<Entry> lineData) {
        this.lineData = lineData;
    }

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

    public class Point{
        String time;
        double value;
    }
}
