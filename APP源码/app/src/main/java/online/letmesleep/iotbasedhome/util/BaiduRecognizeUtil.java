package online.letmesleep.iotbasedhome.util;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;

import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Letmesleep on 2018/4/14.
 */

public class BaiduRecognizeUtil {
    public static final String ASR = "asr";
    public static final String WAKE_UP = "wp";
    private EventManager asr;
    private String type;
    private EventListener listener;
    public BaiduRecognizeUtil(Context context,EventListener listener,String type){
        asr = EventManagerFactory.create(context, type);
        asr.registerListener(listener);
        this.listener = listener;
        this.type = type;

    }

    public void start(){

        if(type.equals(ASR)){
            startASR();
        }
        if(type.equals(WAKE_UP)){
            startWP();
        }
    }

    /***
     * wake up start
     */
    private void startWP(){
        Map<String, Object> params = new TreeMap<String, Object>();

        params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, false);
        params.put(SpeechConstant.WP_WORDS_FILE, "assets:///WakeUp.bin");
        // "assets:///WakeUp.bin" 表示WakeUp.bin文件定义在assets目录下

        String json = null; // 这里可以替换成你需要测试的json
        json = new JSONObject(params).toString();
        asr.send(SpeechConstant.WAKEUP_START, json, null, 0, 0);
    }

    /***
     * asr start
     */
    private void startASR(){
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        String event = null;
        event = SpeechConstant.ASR_START; // 替换成测试的event

        params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, false);
        // params.put(SpeechConstant.NLU, "enable");
        // params.put(SpeechConstant.VAD_ENDPOINT_TIMEOUT, 0); // 长语音
        // params.put(SpeechConstant.IN_FILE, "res:///com/baidu/android/voicedemo/16k_test.pcm");
        // params.put(SpeechConstant.VAD, SpeechConstant.VAD_DNN);
        // params.put(SpeechConstant.PROP ,20000);
        // params.put(SpeechConstant.PID, 1537); // 中文输入法模型，有逗号
        // 请先使用如‘在线识别’界面测试和生成识别参数。 params同ActivityRecog类中myRecognizer.start(params);


        String json = null; // 可以替换成自己的json
        json = new JSONObject(params).toString(); // 这里可以替换成你需要测试的json
        asr.send(event, json, null, 0, 0);

    }
    public void stop(){
        if(type.equals(ASR))
            asr.send(SpeechConstant.ASR_STOP, null, null, 0, 0);
        else{
            asr.send(SpeechConstant.WAKEUP_STOP, null, null, 0, 0);
            release();
        }
    }

    public void release(){
        if(asr==null)
            return;
        asr.unregisterListener(listener);
        asr=null;
    }

}
