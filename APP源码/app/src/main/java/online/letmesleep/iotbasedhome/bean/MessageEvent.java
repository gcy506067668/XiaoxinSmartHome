package online.letmesleep.iotbasedhome.bean;

/**
 * Created by Letmesleep on 2018/4/28.
 */

public class MessageEvent {

    public static final int LIGHT_CHANGE = 1;
    public static final int TEMPERATURE_UPDATA = 2;
    public static final int HUMIDITY_UPDATA = 3;
    public static final int TEMPERATURE_LIMIT_ALERT = 4;
    public static final int HUMIDITY_LIMIT_ALERT = 5;
    public static final int SHOW_APPLICATION_FRAGMENT = 6;


    public int what;
    public String message;
    public Object obj;
    public float value;
}
