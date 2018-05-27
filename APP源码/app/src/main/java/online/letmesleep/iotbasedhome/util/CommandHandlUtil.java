package online.letmesleep.iotbasedhome.util;

import android.util.Log;

import com.iflytek.thirdparty.P;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import online.letmesleep.iotbasedhome.MainActivity;
import online.letmesleep.iotbasedhome.MyApplication;
import online.letmesleep.iotbasedhome.bean.ApplicationEntity;
import online.letmesleep.iotbasedhome.config.Config;
import online.letmesleep.iotbasedhome.ifly.IFLYSpeaker;

import static android.os.Build.VERSION_CODES.M;

/**
 * Created by Letmesleep on 2018/4/15.
 */
public class CommandHandlUtil {

    public static final String OPEN_LIGHT_ACTION = "light";
    public static final String TEMPERATURE_ACTION = "temperature";
    public static final String HUMIDITY_ACTION = "humidity";
    public static final String EXIT_SELF = "exit_self";
    public static final String TRANSLATE_LANGUAGE = "translate_language";
    public static final String TEMPERATURE_LIMIT = "temp_limit";
    public static final String ADD_APPLICATION = "add_application";
    public static final String HUMIDITY_LIMIT_ACTION = "HUMIDITY_LIMIT_ACTION";



    /****
     *          命令解析器
     * @param command
     */
    public static String handleMessage(String command) {
        //解析识别文字内容
        command = JSONAnalysisUtil.analysisCommandJSON(command);
        String action = getActionFromCommand(command);
        return action;

    }

    /**
     * 命令分发器
     * @param command
     * @return
     */
    private static String getActionFromCommand(String command){

        /**
         * 灯
         * **/
        if(command.contains("灯")) {
            return handleLight(command);
        }
        /**
         * *
         * *温度
         * */
        if(command.contains("温度")){
            return handleTemperature(command);
        }

        /**
         * 湿度
         * **/
        if(command.contains("湿度")){
            return handleHumidity(command);
        }

        /**
         * 切换发音人
         * **/
        if((command.contains("声音")&&(command.contains("不好听")||command.contains("难听")))
                ||(command.contains("你会说")&&command.contains("吗"))
                ||command.contains("你说话能")
                ||(command.contains("你能说")&&command.contains("吗"))){
            return handleLanguageTranslate(command);
        }

        /**
         * 语音退出APP
         * **/
        if((command.contains("没事")||command.contains("没什么事"))
                &&(command.contains("退下吧")||command.contains("退下了"))
                ||(command.contains("把自己")&&command.contains("关"))){
            return EXIT_SELF;
        }

        /**
         * 翻译
         * **/
        if(command.contains("帮我翻译")
                ||command.contains("翻译一下")
                ||command.contains("翻译成")){
            return TRANSLATE_LANGUAGE;
        }

        if(command.contains("天价设备")
                ||command.contains("添加设备")){
            return ADD_APPLICATION;
        }

        return command;
    }

    /**
     * 处理灯相关命令
     * @param command
     * @return
     */
    private static String handleLight(String command){
        // TODO: 2018/4/29      数据库读取设备名称    更改状态后保存数据到数据库
        String statue = "";
        if ((command.contains("打开") || command.contains("开"))) {
            statue = "on";
        }
        if((command.contains("关闭") || command.contains("关"))){
            statue = "off";
        }

        for (ApplicationEntity entity:Config.getAllApplication()) {
            if(entity.getApplicationType()<4){
                if(command.contains(entity.getApplicationName())){
                    ApplicationControlUtil.sendLightControlCommand(entity.getApplicationId()+statue);
                    entity.setStatus("light_"+statue);
                    entity.save();
                }
            }
        }
        return OPEN_LIGHT_ACTION;
    }

    /**
     * 处理温度相关命令
     * @param command
     * @return
     */
    private static String handleTemperature(String command){
        if(command.contains("设置")){
            command=command.trim();
            String resultInt="";
            if(command != null && !"".equals(command)){
                for(int i=0;i<command.length();i++){
                    if(command.charAt(i)>=48 && command.charAt(i)<=57){
                        resultInt+=command.charAt(i);
                    }
                }
            }
            if(resultInt.equals(""))
                return TEMPERATURE_LIMIT;
            if(!resultInt.matches("\\d*"))
                return TEMPERATURE_LIMIT;
            if(command.contains("上限")||command.contains("上线")){
                Config.HIGH_TEMPERATURE = Integer.parseInt(resultInt);
                PreferenceUtil.save(MyApplication.getInstance(),Config.PREFENCE_TEMP_HIGH,Config.HIGH_TEMPERATURE);
            }else{
                Config.LOW_TEMPERATURE = Integer.parseInt(resultInt);
                PreferenceUtil.save(MyApplication.getInstance(),Config.PREFENCE_TEMP_LOW,Config.LOW_TEMPERATURE);
            }
            return TEMPERATURE_LIMIT;
        }
        if(command.contains("上线")
                ||command.contains("上限")
                ||command.contains("下线")
                ||command.contains("下限"))
            return TEMPERATURE_LIMIT;
        // TODO: 2018/4/15

        return TEMPERATURE_ACTION;
    }

    /**
     * 处理湿度相关命令
     * @param command
     * @return
     */
    private static String handleHumidity(String command){
        if(command.contains("设置")){
            command=command.trim();
            String resultInt="";
            if(command != null && !"".equals(command)){
                for(int i=0;i<command.length();i++){
                    if(command.charAt(i)>=48 && command.charAt(i)<=57){
                        resultInt+=command.charAt(i);
                    }
                }
            }
            if(resultInt.equals(""))
                return HUMIDITY_LIMIT_ACTION;
            if(!resultInt.matches("\\d*"))
                return HUMIDITY_LIMIT_ACTION;
            if(command.contains("上限")||command.contains("上线")){
                Config.HIGH_HUMIDITY_STANDARD = Integer.parseInt(resultInt);
                PreferenceUtil.save(MyApplication.getInstance(),Config.PREFENCE_HUMIDITY_HIGH,Config.HIGH_HUMIDITY_STANDARD);
            }else{
                Config.LOW_HUMIDITY_STANDARD = Integer.parseInt(resultInt);
                PreferenceUtil.save(MyApplication.getInstance(),Config.PREFENCE_HUMIDITY_LOW,Config.LOW_HUMIDITY_STANDARD);
            }
            return HUMIDITY_LIMIT_ACTION;
        }
        if(command.contains("上线")
                ||command.contains("上限")
                ||command.contains("下线")
                ||command.contains("下限"))
            return HUMIDITY_LIMIT_ACTION;

        return HUMIDITY_ACTION;
    }

    private static String handleLanguageTranslate(String command){
        if(command.contains("河南")){
            PreferenceUtil.save(MyApplication.getInstance(),"speaker", Config.HENAN_SPEAKER);
            MyApplication.speaker = IFLYSpeaker.Speaker.HeNan;
            return "河南话";
        }
        if(command.contains("四川")){
            PreferenceUtil.save(MyApplication.getInstance(),"speaker", Config.SICHUAN_SPEAKER);
            MyApplication.speaker = IFLYSpeaker.Speaker.SiChuan;
            return "四川话";
        }
        if(command.contains("粤语")){
            PreferenceUtil.save(MyApplication.getInstance(),"speaker", Config.YUYUE_SPEAKER);
            MyApplication.speaker = IFLYSpeaker.Speaker.Yueyu;
            return "粤语";
        }
        if(command.contains("小新")||command.contains("小心")){
            PreferenceUtil.save(MyApplication.getInstance(),"speaker", Config.XIAOXIN_SPEAKER);
            MyApplication.speaker = IFLYSpeaker.Speaker.Xiaoxin;
            return "蜡笔小新";
        }
        if(command.contains("正常")){
            PreferenceUtil.save(MyApplication.getInstance(),"speaker", Config.NORMAL_SPEAKER);
            MyApplication.speaker = IFLYSpeaker.Speaker.EnglishOrChineseGirl;
            return "你能正常点吗";
        }

        if(command.contains("东北")){
            PreferenceUtil.save(MyApplication.getInstance(),"speaker", Config.DONGBEI_SPEAKER);
            MyApplication.speaker = IFLYSpeaker.Speaker.DongBei;
            return "东北人";
        }
        if(command.contains("萝莉")||command.contains("罗莉")){
            PreferenceUtil.save(MyApplication.getInstance(),"speaker", Config.DONGBEI_SPEAKER);
            MyApplication.speaker = IFLYSpeaker.Speaker.LittleGirl;
            return "是这样吗";
        }
        PreferenceUtil.save(MyApplication.getInstance(),"speaker", Config.DONGBEI_SPEAKER);
        MyApplication.speaker = IFLYSpeaker.Speaker.LittleGirl;
        return "现在呢";
    }
}
