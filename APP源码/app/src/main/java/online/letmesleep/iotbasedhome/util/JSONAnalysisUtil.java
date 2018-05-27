package online.letmesleep.iotbasedhome.util;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Letmesleep on 2018/4/14.
 */

public class JSONAnalysisUtil {
    public static String analysisCommandJSON(String command){
        try {
            command = new JSONObject(command).getString("results_recognition");
            if(command==null)
                command="";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return command;
    }

    public static String analysisRobotJSON(String jsonString){
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            int code = jsonObject.getInt("code");
            if(code>100000){
                return "这似乎超出了我的能力范围";
            }
            if(code==40004)
                return "今天的测试次数已经用完了";
            if(code==40007)
                return "你说话的风格好像有点不对哦，我听不大懂";
            if(code==100000){
                return jsonObject.getString("text");
            }

            return "啊略略";
        } catch (JSONException e) {
            e.printStackTrace();
            return "解析语句出了点问题";
        }
    }

    public static String analysisTranslateJSON(String command){
        try {
            String result = "";
            JSONObject jo = new JSONObject(command);
            if(jo!=null){
                JSONArray js = jo.getJSONArray("trans_result");
                for (int i = 0; i < js.length(); i++) {
                    jo = js.getJSONObject(i);
                    return jo.getString("dst");
                }
            }
            if(command==null)
                command="";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return command;
    }
}
