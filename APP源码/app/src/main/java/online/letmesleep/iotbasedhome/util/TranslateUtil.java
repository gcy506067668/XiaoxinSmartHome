package online.letmesleep.iotbasedhome.util;

import android.content.Context;
import android.util.Log;

import online.letmesleep.iotbasedhome.MyApplication;
import online.letmesleep.iotbasedhome.ifly.IFLYSpeaker;

/**
 * Created by Letmesleep on 2018/4/17.
 */

public class TranslateUtil {
    public static void translateLanguage(String command, final Context context){
        command = JSONAnalysisUtil.analysisCommandJSON(command);
        String translateMessages[] = null;
        IFLYSpeaker.Speaker speaker = MyApplication.speaker;
        if(command.contains("翻译成")){
            String words[] = command.split("把");
            if(words.length!=2){
                speak(context,"自己去翻译，我没那闲",speaker);
                return;
            }
            command = words[1];
            command = command.replace("翻译成","");
            command = command.replace("方言","话");
            if(command.contains("河南话")){
                speaker = IFLYSpeaker.Speaker.HeNan;
                command = command.replace("河南话","");
            }
            if(command.contains("东北话")){
                speaker = IFLYSpeaker.Speaker.DongBei;
                command = command.replace("东北话","");
            }
            if(command.contains("湖南话")){
                speaker = IFLYSpeaker.Speaker.HUNAN;
                command = command.replace("湖南话","");
            }
            if(command.contains("粤语")){
                speaker = IFLYSpeaker.Speaker.Yueyu;
                command = command.replace("粤语","");
            }
            if(command.contains("四川话")){
                speaker = IFLYSpeaker.Speaker.SiChuan;
                command = command.replace("四川话","");
            }
            speak(context,command,speaker);
            return;
        }


        if(command.contains("帮我翻译")){
            if(command.contains("翻译一下")){
                translateMessages = command.split("翻译一下");
            }else{
                translateMessages = command.split("帮我翻译");
            }
        }
        if(command.contains("翻译一下")){
            translateMessages = command.split("翻译一下");
        }
        if(translateMessages==null){
            return;
        }
        if(translateMessages.length==2){
            command = translateMessages[1];

        }else if(translateMessages.length==1){
            speak(context,"给你个重新组织语言的机会，你到底要我帮你翻译什么", speaker);
            return;
        }else if(translateMessages.length>2){
            for (int i = 1; i < translateMessages.length; i++) {
                command+=translateMessages[i];
            }
        }

        final String finalCommand = command;
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = JSONAnalysisUtil.analysisTranslateJSON(MyApplication.api.getTransResult(finalCommand,"auto","en"));
                Log.e("翻译",""+result);
                speak(context, result, IFLYSpeaker.Speaker.EnglishOrChineseGirl);
            }
        }).start();
        return;
    }
    public static void speak(Context context,String text, IFLYSpeaker.Speaker speaker) {
        IFLYSpeaker.Speak(context, text, speaker);
    }
}
