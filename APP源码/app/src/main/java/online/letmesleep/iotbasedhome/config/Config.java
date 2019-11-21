package online.letmesleep.iotbasedhome.config;

import org.litepal.crud.DataSupport;

import java.util.List;

import online.letmesleep.iotbasedhome.MyApplication;
import online.letmesleep.iotbasedhome.bean.ApplicationEntity;
import online.letmesleep.iotbasedhome.ifly.IFLYSpeaker;

/**
 * Created by Letmesleep on 2018/4/15.
 */

public class Config {
    // basic url
    public static final String LIGHT_BASIC_URL = "http://localhost:80";
    public static final String LOGIN_URL = "http://localhost:80";
    public static final String REGISTER_URL = "http://localhost:80";
    public static final String TEMP_HUMIDITY_URL = "http://localhost:80";
    public static final String TULING_API_URL = "http://localhost:80";
    public static final String LIGHT_STATUE_URL = "http://localhost:80";

    //api config
    // public static final String BAIDU_WAKE_UP_APP_ID = "**********";
    // public static final String BAIDU_WAKE_UP_API_KEY = "**********";
    // public static final String BAIDU_WAKE_UP_SECRET_KEY = "**********";
    public static final String IFLY_APPID = "**********";
    public static final String TULING_APPKEY = "**********";
    public static final String TRANSLATE_APP_ID = "**********";
    public static final String TRANSLATE_SECURITY_KEY = "**********";

    //preference tag
    public static final String PREFENCE_TEMP_LOW= "temperature_low_limit";
    public static final String PREFENCE_TEMP_HIGH= "temperature_high_limit";
    public static final String PREFENCE_HUMIDITY_HIGH = "humidity_high_limit";
    public static final String PREFENCE_HUMIDITY_LOW = "humidity_low_limit";


    //application type
    public static final int APPLICATION_TYPE_LED = 0;
    public static final int APPLICATION_TYPE_RED_LED = 1;
    public static final int APPLICATION_TYPE_GREEN_LED = 2;
    public static final int APPLICATION_TYPE_BLUE_LED = 3;
    public static final int APPLICATION_TYPE_TEMPERATURE = 4;
    public static final int APPLICATION_TYPE_HUMIDITY = 5;

    //application statue
    public static final String LIGHT_STATUS_ON = "light_on";
    public static final String LIGHT_STATUS_OFF = "light_off";


    public static String getApplicationType(int type){
        switch (type){
            case APPLICATION_TYPE_LED :
                return "白色LED灯";
            case APPLICATION_TYPE_RED_LED :
                return "红色LED灯";
            case APPLICATION_TYPE_GREEN_LED :
                return "绿色LED灯";
            case APPLICATION_TYPE_BLUE_LED :
                return "蓝色LED灯";
            case APPLICATION_TYPE_TEMPERATURE :
                return "温度传感器";
            case APPLICATION_TYPE_HUMIDITY :
                return "湿度传感器";
        }
        return "未知类型设备";
    }


    //language type
    public static final int HENAN_SPEAKER = 0;    //河南话
    public static final int DONGBEI_SPEAKER = 1;    //东北话
    public static final int XIAOXIN_SPEAKER = 2;    //小新发音
    public static final int NORMAL_SPEAKER = 3;     //正常女生
    public static final int SICHUAN_SPEAKER = 4;     //四川方言
    public static final int YUYUE_SPEAKER = 5;     //粤语
    public static final int LITTLE_GIRL_SPEAKER = 6;     //小女孩
    public static final int TAIWAN_SPEAKER = 7;     //台湾
    public static int HIGH_TEMPERATURE = 0;
    public static int LOW_TEMPERATURE = 100;
    public static int HIGH_HUMIDITY_STANDARD = 100;
    public static int LOW_HUMIDITY_STANDARD = 0;


    public static List<ApplicationEntity> getAllApplication(){
        if(MyApplication.applications==null
                ||MyApplication.applications.size()!= DataSupport.count(ApplicationEntity.class)){
            MyApplication.applications = DataSupport.findAll(ApplicationEntity.class);
        }
        return MyApplication.applications;
    }
}
