package online.letmesleep.iotbasedhome.util;

import org.greenrobot.eventbus.EventBus;

import online.letmesleep.iotbasedhome.MyApplication;
import online.letmesleep.iotbasedhome.bean.MessageEvent;
import online.letmesleep.iotbasedhome.config.Config;

import static online.letmesleep.iotbasedhome.config.Config.LIGHT_BASIC_URL;

/**
 * Created by Letmesleep on 2018/4/15.
 */

public class ApplicationControlUtil {



    public static void redOn(){
        MOKHttpUtil.getInstance().HttpGet(LIGHT_BASIC_URL +"ron");
    }
    public static void redOff(){
        MOKHttpUtil.getInstance().HttpGet(LIGHT_BASIC_URL +"roff");
    }

    public static void blueOn(){
        MOKHttpUtil.getInstance().HttpGet(LIGHT_BASIC_URL +"bon");
    }
    public static void blueOff(){
        MOKHttpUtil.getInstance().HttpGet(LIGHT_BASIC_URL +"boff");
    }

    public static void greenOn(){
        MOKHttpUtil.getInstance().HttpGet(LIGHT_BASIC_URL +"gon");
    }
    public static void greenOff(){
        MOKHttpUtil.getInstance().HttpGet(LIGHT_BASIC_URL +"goff");
    }

    public static void lightOn(){
        MOKHttpUtil.getInstance().HttpGet(LIGHT_BASIC_URL +"lighton");
    }
    public static void lightOff(){ MOKHttpUtil.getInstance().HttpGet(LIGHT_BASIC_URL +"lightoff");}


    public static void sendLightControlCommand(String command){
        MOKHttpUtil.getInstance().HttpGet(LIGHT_BASIC_URL + command);
    }

    public interface onTemperatureHumidityCallback{
        void onTemperatureSuccess(float temperature);
        void onHumiditySuccess(float humidity);
        void onFaild(String err);
    }

    public static void getTempAndHumidity(final onTemperatureHumidityCallback callback){
        new Thread(new Runnable() {
            @Override
            public void run() {
                MOKHttpUtil.getInstance().HttpGet(Config.TEMP_HUMIDITY_URL, new MOKHttpUtil.Callback() {
                    @Override
                    public void onSuccess(String resp) {
                        if(callback==null)
                            return;
                        String strs[] = resp.split("_");
                        if(strs.length!=2)
                            callback.onFaild("解析失败:"+resp);
                        if(strs[0].matches("\\d*(\\.*\\d*)")&&strs[1].matches("\\d*(\\.*\\d*)")){
                            callback.onTemperatureSuccess(Float.parseFloat(strs[0]));
                            callback.onHumiditySuccess(Float.parseFloat(strs[1]));
                        }else{
                            callback.onFaild("数据错误:"+resp);
                        }

                    }

                    @Override
                    public void onFaild(String err) {
                        if(callback!=null)
                            callback.onFaild(err.toString());
                    }
                });

            }
        }).start();
    }
}
