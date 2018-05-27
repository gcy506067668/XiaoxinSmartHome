package online.letmesleep.iotbasedhome.ifly;

import android.content.Context;
import android.os.Bundle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;

/**
 * Created by letmesleep on 2016/12/8.
 * QQ:506067668
 */

public class IFLYVoiceReader implements RecognizerListener {

    private Context mContext;
    private final SpeechRecognizer r;
    private ReaderAnalListener mListener;
    private StringBuffer sb;

    /***
     *      初始化监听器
     * @param context
     */
    public IFLYVoiceReader(Context context, ReaderAnalListener listener){
        mContext = context;
        mListener = listener;
        sb = new StringBuffer();
        r = SpeechRecognizer.createRecognizer(context,null);
        r.setParameter(SpeechConstant.DOMAIN,"iat");
        r.setParameter(SpeechConstant.LANGUAGE,"zh_cn");
        r.setParameter(SpeechConstant.ACCENT,"mandarin");
    }

    /***
     *    开始监听语音
     */
    public void beginListenering(){
        r.startListening(this);
    }

    /***
     *    结束监听语音
     */
    public void endListenering(){
        r.stopListening();
    }


    /***
     *          解析IFLY返回的json
     * @param jsonString
     * @return
     */
    public String ReaderJSONUtil(String jsonString){
        StringBuffer sb = new StringBuffer();
        JSONObject object = JSON.parseObject(jsonString);
        JSONArray wsArray = object.getJSONArray("ws");
        for (int i = 0; i < wsArray.size(); i++) {
            object = wsArray.getJSONObject(i);
            JSONArray cwArray = object.getJSONArray("cw");
            sb.append(cwArray.getJSONObject(0).getString("w"));
        }
        return sb.toString();
    }


    @Override
    public void onVolumeChanged(int i, byte[] bytes) {

    }


    @Override
    public void onBeginOfSpeech() {

    }


    @Override
    public void onEndOfSpeech() {

    }

    /***
     *          语音识别返回结果
     * @param recognizerResult
     * @param b
     */
    @Override
    public void onResult(RecognizerResult recognizerResult, boolean b) {
        sb.append(ReaderJSONUtil(recognizerResult.getResultString()));
        if(b){
            if(mListener!=null)
                mListener.onSuccess(sb.toString());
            sb.delete(0,sb.length()-1);
        }
    }

    @Override
    public void onError(SpeechError speechError) {
        if(mListener!=null)
            mListener.onFalie(speechError.getErrorDescription());
    }

    @Override
    public void onEvent(int i, int i1, int i2, Bundle bundle) {

    }

    public interface ReaderAnalListener{
        public void onSuccess(String result);
        public void onFalie(String err);
    }
}
