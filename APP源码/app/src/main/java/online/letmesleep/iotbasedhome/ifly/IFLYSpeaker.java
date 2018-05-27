package online.letmesleep.iotbasedhome.ifly;

import android.content.Context;
import android.os.Bundle;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

/**
 * Created by letmesleep on 2016/12/8.
 * QQ:506067668
 */

public class IFLYSpeaker {


    /***
     *          语音转文字
     * @param context       //上下文
     * @param speakText     //源文字
     * @param speaker       //发音者
     */
    public static void Speak(Context context, String speakText, Speaker speaker){
        SpeechSynthesizer r = SpeechSynthesizer.createSynthesizer(context,null);
        r.setParameter(SpeechConstant.VOICE_NAME,speaker.getName());
        r.setParameter(SpeechConstant.SPEED,speaker.getVoiceSpeed());
        r.setParameter(SpeechConstant.VOLUME,speaker.getVolume());
        r.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        //r.setParameter(SpeechConstant.TTS_AUDIO_PATH,"/sdcard");
        r.startSpeaking(speakText,null);
    }

    public static void Speak(Context context, String speakText, Speaker speaker,SynthesizerListener listener){
        SpeechSynthesizer r = SpeechSynthesizer.createSynthesizer(context,null);
        r.setParameter(SpeechConstant.VOICE_NAME,speaker.getName());
        r.setParameter(SpeechConstant.SPEED,speaker.getVoiceSpeed());
        r.setParameter(SpeechConstant.VOLUME,speaker.getVolume());
        r.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        //r.setParameter(SpeechConstant.TTS_AUDIO_PATH,"/sdcard");
        r.startSpeaking(speakText,listener);
    }



    /***
     * 发音者
     */
    public enum Speaker{
        EnglishOrChineseBoy("xiaoyu")       //普通话或英语青年男生
        ,EnglishOrChineseGirl("xiaoqi")     //普通话或英语青年女生
        ,PureEnglishGirl("catherine")       //英文女生
        ,PureEnglishBoy("henry")            //英文男生
        ,Russian("Allabent")                //俄语
        ,Spanish("Gabriela")                //西班牙语
        ,Hindi("Abba")                      //印度语
        ,France("Mariane")                  //法语
        ,Vietnam("xiaoyun")                 //越南语
        ,Yueyu("xiaomei")                   //粤语
        ,Xiaoxin("xiaoxin")                 //蜡笔小新
        ,TaiWan("xiaolin")                  //台湾普通话
        ,LittleGirl("nannan")               //儿童女生
        ,SiChuan("xiaorong")                //四川方言
        ,DongBei("xiaoqian")                //东北话
        ,HUNAN("xiaoqiang")                  //湖南话
        ,HeNan("xiaokun");                  //河南方言

        String name;
        String voiceSpeed = "50";           //讲述人语速
        String volume = "80";               //讲述人音量
        private Speaker(String speaker){
            name = speaker;
        }

        public String getVoiceSpeed() {
            return voiceSpeed;
        }

        public void setVoiceSpeed(String voiceSpeed) {
            this.voiceSpeed = voiceSpeed;
        }

        public String getVolume() {
            return volume;
        }

        public void setVolume(String volume) {
            this.volume = volume;
        }

        public String getName(){
            return name;
        }
    }
}
