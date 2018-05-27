package online.letmesleep.iotbasedhome;

import android.app.Application;


import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;


import org.litepal.LitePal;
import org.litepal.crud.DataSupport;


import java.util.List;

import online.letmesleep.iotbasedhome.bean.ApplicationEntity;
import online.letmesleep.iotbasedhome.config.Config;
import online.letmesleep.iotbasedhome.ifly.IFLYSpeaker;
import online.letmesleep.iotbasedhome.util.PreferenceUtil;
import online.letmesleep.iotbasedhome.util.SoundPoolUtil;
import online.letmesleep.iotbasedhome.util.com.baidu.translate.demo.TransApi;

/**
 * Created by Letmesleep on 2018/4/12.
 */

public class MyApplication extends Application {

    private static Application application;
    public static IFLYSpeaker.Speaker speaker ;
    public static TransApi api;
    public static List<ApplicationEntity> applications ;
    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        LitePal.initialize(this);
        SoundPoolUtil.initSoundPool(this);

        SpeechUtility.createUtility(this, SpeechConstant.APPID+"="+ Config.IFLY_APPID);
        api = new TransApi(Config.TRANSLATE_APP_ID, Config.TRANSLATE_SECURITY_KEY);

        initSetting();

    }

    private void initSetting() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                applications = DataSupport.findAll(ApplicationEntity.class);
            }
        }).start();

        switch (PreferenceUtil.load(this,"speaker",1)){
            case Config.NORMAL_SPEAKER:
                speaker = IFLYSpeaker.Speaker.EnglishOrChineseGirl;
                break;
            case Config.XIAOXIN_SPEAKER:
                speaker = IFLYSpeaker.Speaker.Xiaoxin;
                break;
            case Config.HENAN_SPEAKER:
                speaker = IFLYSpeaker.Speaker.HeNan;
                break;
            case Config.SICHUAN_SPEAKER:
                speaker = IFLYSpeaker.Speaker.SiChuan;
                break;
            case Config.YUYUE_SPEAKER:
                speaker = IFLYSpeaker.Speaker.Yueyu;
                break;
            case Config.LITTLE_GIRL_SPEAKER:
                speaker = IFLYSpeaker.Speaker.LittleGirl;
                break;
            case Config.TAIWAN_SPEAKER:
                speaker = IFLYSpeaker.Speaker.TaiWan;
                break;
            case Config.DONGBEI_SPEAKER:
                speaker = IFLYSpeaker.Speaker.DongBei;
                break;
        }
        if(PreferenceUtil.load(this,"speaker",1)==1){
            speaker = IFLYSpeaker.Speaker.EnglishOrChineseGirl;
        }

        Config.HIGH_TEMPERATURE = PreferenceUtil.load(this,Config.PREFENCE_TEMP_HIGH,50);
        Config.LOW_TEMPERATURE = PreferenceUtil.load(this,Config.PREFENCE_TEMP_LOW,1);
        Config.HIGH_HUMIDITY_STANDARD = PreferenceUtil.load(this,Config.PREFENCE_HUMIDITY_HIGH,50);
        Config.LOW_HUMIDITY_STANDARD = PreferenceUtil.load(this,Config.PREFENCE_HUMIDITY_LOW,0);


    }

    public static Application getInstance(){
        return application;
    }


}
