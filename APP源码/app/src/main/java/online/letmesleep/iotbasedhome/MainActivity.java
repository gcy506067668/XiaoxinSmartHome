package online.letmesleep.iotbasedhome;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.baidu.speech.EventListener;
import com.baidu.speech.asr.SpeechConstant;
import com.github.florent37.expectanim.ExpectAnim;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.thirdparty.C;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import at.markushi.ui.CircleButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import online.letmesleep.iotbasedhome.bean.ApplicationEntity;
import online.letmesleep.iotbasedhome.bean.MessageEvent;
import online.letmesleep.iotbasedhome.config.Config;
import online.letmesleep.iotbasedhome.fragments.ApplicationFragment;
import online.letmesleep.iotbasedhome.fragments.ControlFragment;
import online.letmesleep.iotbasedhome.fragments.UserFragment;
import online.letmesleep.iotbasedhome.ifly.IFLYSpeaker;
import online.letmesleep.iotbasedhome.util.ApplicationControlUtil;
import online.letmesleep.iotbasedhome.util.BaiduRecognizeUtil;
import online.letmesleep.iotbasedhome.util.CommandHandlUtil;
import online.letmesleep.iotbasedhome.util.JSONAnalysisUtil;
import online.letmesleep.iotbasedhome.util.MOKHttpUtil;
import online.letmesleep.iotbasedhome.util.SoundPoolUtil;
import online.letmesleep.iotbasedhome.util.TranslateUtil;

import static com.github.florent37.expectanim.core.Expectations.atItsOriginalPosition;
import static com.github.florent37.expectanim.core.Expectations.invisible;
import static com.github.florent37.expectanim.core.Expectations.outOfScreen;
import static com.github.florent37.expectanim.core.Expectations.visible;


/*****3800 lines code now 2018/5/8  not inlcude layout files and drawable files
 *   by letmesleep 506067668@qq.com*****/

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private void log(String text){
        Log.e(TAG,text);
    }

    @BindView(R.id.activity_main_add_application)
    CircleButton activityMainAddApplication;
    private BaiduRecognizeUtil wakeUpBu;
    private BaiduRecognizeUtil asrBu;
    private FragmentManager fragmentManager;
    private UserFragment userFragment;
    private ControlFragment controlFragment;
    private ApplicationFragment applicationFragment;
    private boolean addApplicationButtontatue = false;
    private BottomNavigationView navigation;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_control:
                    navigationItemClick(1);
                    return true;
                case R.id.navigation_application:
                    navigationItemClick(2);
                    return true;
                case R.id.navigation_user:
                    navigationItemClick(3);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        initPermission();
        initview();
        startTemperatureAndHumidityThread();
    }

    private void initview() {
        ZXingLibrary.initDisplayOpinion(this);
        getSupportActionBar().setTitle("  设  备  ");
        fragmentManager = getFragmentManager();
        FragmentTransaction begin = fragmentManager.beginTransaction();
        controlFragment = new ControlFragment();
        if (!controlFragment.isAdded() && begin.isEmpty()) {
            begin.replace(R.id.content, controlFragment);
            begin.commit();
        }
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        wakeUpBu = new BaiduRecognizeUtil(this, wakeUpListener, BaiduRecognizeUtil.WAKE_UP);
        asrBu = new BaiduRecognizeUtil(this, Listener, BaiduRecognizeUtil.ASR);
        wakeUpBu.start();
    }

    EventListener wakeUpListener = new EventListener() {
        @Override
        public void onEvent(String name, String params, final byte[] data, final int offset, final int length) {
            if (name.equals(SpeechConstant.CALLBACK_EVENT_WAKEUP_SUCCESS)) {
                IFLYSpeaker.Speak(MainActivity.this, "我在", MyApplication.speaker, new SynthesizerListener() {
                    @Override
                    public void onSpeakBegin() {

                    }

                    @Override
                    public void onBufferProgress(int i, int i1, int i2, String s) {

                    }

                    @Override
                    public void onSpeakPaused() {

                    }

                    @Override
                    public void onSpeakResumed() {

                    }

                    @Override
                    public void onSpeakProgress(int i, int i1, int i2) {
                    }

                    @Override
                    public void onCompleted(SpeechError speechError) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                asrBu.start();
                            }
                        }, 500);
                    }

                    @Override
                    public void onEvent(int i, int i1, int i2, Bundle bundle) {
                    }
                });
            }
        }
    };

    String command = "";
    EventListener Listener = new EventListener() {
        @Override
        public void onEvent(String name, String params, final byte[] data, final int offset, final int length) {
            //唤醒  开始监听命令
            if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_READY)) {
                SoundPoolUtil.play(1);
            }
            //语音识别结束
            if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_FINISH)) {
                String action = CommandHandlUtil.handleMessage(command);
                //灯
                if (action.equals(CommandHandlUtil.OPEN_LIGHT_ACTION)) {
                    MessageEvent msg = new MessageEvent();
                    msg.what=MessageEvent.LIGHT_CHANGE;
                    EventBus.getDefault().post(msg);
                    speak("好的");
                    return;
                }
                //退出
                if (action.equals(CommandHandlUtil.EXIT_SELF)) {
                    IFLYSpeaker.Speak(MainActivity.this, "好的  再见！", MyApplication.speaker, new SynthesizerListener() {
                        @Override
                        public void onSpeakBegin() {

                        }

                        @Override
                        public void onBufferProgress(int i, int i1, int i2, String s) {

                        }

                        @Override
                        public void onSpeakPaused() {

                        }

                        @Override
                        public void onSpeakResumed() {

                        }

                        @Override
                        public void onSpeakProgress(int i, int i1, int i2) {
                        }

                        @Override
                        public void onCompleted(SpeechError speechError) {
                            finish();
                        }

                        @Override
                        public void onEvent(int i, int i1, int i2, Bundle bundle) {
                        }
                    });
                    return;
                }
                //温度
                if (action.equals(CommandHandlUtil.TEMPERATURE_ACTION)) {
                    ApplicationControlUtil.getTempAndHumidity(new ApplicationControlUtil.onTemperatureHumidityCallback() {
                        @Override
                        public void onTemperatureSuccess(float temperature) {
                            if(command.contains("温度"))
                                speak("当前温度为："+temperature+"摄氏度");
                        }

                        @Override
                        public void onHumiditySuccess(float humidity) {
                            if(command.contains("湿度"))
                                speak("当前湿度为：百分之"+humidity);
                        }

                        @Override
                        public void onFaild(final String err) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "err："+err, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            navigationItemClick(2);
                        }
                    });
                    return;
                }

                if (action.equals(CommandHandlUtil.TEMPERATURE_LIMIT)) {
                    speak("当前温度上限为："
                            + Config.HIGH_TEMPERATURE
                            +"摄氏度，下限为："
                            +Config.LOW_TEMPERATURE
                            +"摄氏度");
                    return;
                }

                //湿度
                if (action.equals(CommandHandlUtil.HUMIDITY_ACTION)) {
                    ApplicationControlUtil.getTempAndHumidity(new ApplicationControlUtil.onTemperatureHumidityCallback() {
                        @Override
                        public void onTemperatureSuccess(float temperature) {
                            if(command.contains("温度"))
                                speak("当前温度为："+temperature+"摄氏度");
                        }

                        @Override
                        public void onHumiditySuccess(float humidity) {
                            if(command.contains("湿度"))
                                speak("当前湿度为：百分之"+humidity);
                        }

                        @Override
                        public void onFaild(final String err) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "err："+err, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            navigationItemClick(2);
                        }
                    });
                    return;
                }
                if (action.equals(CommandHandlUtil.HUMIDITY_LIMIT_ACTION)) {
                    speak("当前湿度上限为：百分之"
                            + Config.HIGH_HUMIDITY_STANDARD
                            +"下限为：百分之"
                            +Config.LOW_HUMIDITY_STANDARD);
                    return;
                }

                if (action.equals("是这样吗") || action.equals("现在呢")) {
                    speak(action);
                    return;
                }
                //翻译
                if (action.equals(CommandHandlUtil.TRANSLATE_LANGUAGE)) {
                    TranslateUtil.translateLanguage(command, MainActivity.this);
                    return;
                }
                //无命令识别访问图灵机器人
                MOKHttpUtil.strFromTuling(action, new MOKHttpUtil.Callback() {
                    @Override
                    public void onSuccess(String resp) {
                        speak(JSONAnalysisUtil.analysisRobotJSON(resp));
                    }

                    @Override
                    public void onFaild(String err) {

                    }
                });
                SoundPoolUtil.play(4);
            }
            //语音识别结果
            if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL)) {
                command = params;
            }
        }
    };

    /**
     * 权限申请
     */
    private void initPermission() {
        String permissions[] = {Manifest.permission.RECORD_AUDIO,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.MODIFY_AUDIO_SETTINGS,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_SETTINGS,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.CAMERA
        };

        ArrayList<String> toApplyList = new ArrayList<String>();

        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm);
                //进入到这里代表没有权限.

            }
        }
        String tmpList[] = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123);
        }

    }

    public void speak(String text) {
        IFLYSpeaker.Speak(MainActivity.this, text, MyApplication.speaker);
    }

    public void hideAddApplicationActionButton(boolean hide) {
        if(addApplicationButtontatue==hide)
            return;
        if(hide){
            addApplicationButtontatue = true;
            new ExpectAnim()
                    .expect(activityMainAddApplication)
                    .toBe(
                            outOfScreen(Gravity.RIGHT),
                            invisible()
                    )
                    .toAnimation()
                    .start();
        }else{
            addApplicationButtontatue = false;
            new ExpectAnim()

                    .expect(activityMainAddApplication)
                    .toBe(
                            atItsOriginalPosition(),
                            visible()
                    )
                    .toAnimation()
                    .start();
        }

    }

    private boolean TemperatureHumidityFlag = true;
    private boolean printErrorOnce = true;

    public void startTemperatureAndHumidityThread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (TemperatureHumidityFlag) {
                    ApplicationControlUtil.getTempAndHumidity(new ApplicationControlUtil.onTemperatureHumidityCallback() {
                        @Override
                        public void onTemperatureSuccess(float temperature) {
                            printErrorOnce = true;
                            MessageEvent msg = new MessageEvent();
                            msg.what=MessageEvent.TEMPERATURE_UPDATA;
                            msg.value = temperature;
                            EventBus.getDefault().post(msg);
                            alertForLimit(temperature,Config.APPLICATION_TYPE_TEMPERATURE);
                        }

                        @Override
                        public void onHumiditySuccess(float humidity) {
                            MessageEvent msg = new MessageEvent();
                            msg.what=MessageEvent.HUMIDITY_UPDATA;
                            msg.value = humidity;
                            EventBus.getDefault().post(msg);
                            alertForLimit(humidity,Config.APPLICATION_TYPE_HUMIDITY);
                        }

                        @Override
                        public void onFaild(final String err) {
                            if(printErrorOnce){
                                printErrorOnce = false;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MainActivity.this, "解析温湿度失败:" + err, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                    MOKHttpUtil.getInstance().HttpGet(Config.LIGHT_STATUE_URL, new MOKHttpUtil.Callback() {
                        @Override
                        public void onSuccess(String resp) {
                            log(resp);
                            String lightStatue = Config.LIGHT_STATUS_ON;
                            if(resp.contains("on")){
                                resp = resp.replace("on","");
                                lightStatue = Config.LIGHT_STATUS_ON;
                            }
                            if(resp.contains("off")){
                                resp = resp.replace("off","");
                                lightStatue = Config.LIGHT_STATUS_OFF;
                            }
                            for (ApplicationEntity entity:Config.getAllApplication()) {
                                if(entity.getApplicationType()<4&&entity.getApplicationId().equals(resp)){
                                    if(!lightStatue.equals(entity.getStatus())){
                                        log(entity.getApplicationName());
                                        entity.setStatus(lightStatue);
                                        entity.save();
                                        MessageEvent msg = new MessageEvent();
                                        msg.what=MessageEvent.LIGHT_CHANGE;
                                        EventBus.getDefault().post(msg);
                                    }
                                }
                            }

                        }

                        @Override
                        public void onFaild(String err) {

                        }
                    });
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @OnClick(R.id.activity_main_add_application)
    public void onViewClicked() {
        Intent intent = new Intent(MainActivity.this, QRCodeScannerActivity.class);
        startActivityForResult(intent, 107);
    }

    /**
     * 该函数需要运行的ui线程中
     * **/
    private void navigationItemClick(int which){
        FragmentTransaction begin = fragmentManager.beginTransaction();
        switch (which) {
            case 1:
                getSupportActionBar().setTitle("  设  备  ");
                hideAddApplicationActionButton(false);
                if (controlFragment == null)
                    controlFragment = new ControlFragment();
                begin.replace(R.id.content, controlFragment);
                begin.commit();
                break;
            case 2:
                getSupportActionBar().setTitle("  监  测  ");
                hideAddApplicationActionButton(false);
                if (applicationFragment == null)
                    applicationFragment = new ApplicationFragment();
                begin.replace(R.id.content, applicationFragment);
                begin.commit();
                break;
            case 3:
                getSupportActionBar().setTitle("  我  的  ");
                hideAddApplicationActionButton(true);
                if (userFragment == null)
                    userFragment = new UserFragment();
                begin.replace(R.id.content, userFragment);
                begin.commit();
                break;
        }
    }

    private boolean alertOnce = true;
    private boolean humidityAlertOnce = true;
    public void alertForLimit(float value,int type){
        if(type==Config.APPLICATION_TYPE_TEMPERATURE){
            if(value< Config.LOW_TEMPERATURE||value> Config.HIGH_TEMPERATURE){
                if(alertOnce){
                    alertOnce = false;
                    SoundPoolUtil.play(5);
                    speak("当前温度为:"+value+"摄氏度，已超过设置限度");
                    MessageEvent msg = new MessageEvent();
                    msg.what=MessageEvent.TEMPERATURE_LIMIT_ALERT;
                    msg.value = value;
                    EventBus.getDefault().post(msg);
                }
            }else{
                if(!alertOnce)
                    speak("当前温度为:"+value+"摄氏度，已恢复正常");
                alertOnce = true;
            }
        }
        if(type==Config.APPLICATION_TYPE_HUMIDITY){
            if(value< Config.LOW_HUMIDITY_STANDARD||value> Config.HIGH_HUMIDITY_STANDARD){
                if(humidityAlertOnce){
                    humidityAlertOnce = false;
                    SoundPoolUtil.play(5);
                    speak("当前湿度为:百分之"+value+"，已超过设置限度");
                    MessageEvent msg = new MessageEvent();
                    msg.what=MessageEvent.HUMIDITY_LIMIT_ALERT;
                    msg.value = value;
                    EventBus.getDefault().post(msg);
                }
            }
            else{
                if(!humidityAlertOnce)
                    speak("当前湿度为:百分之"+value+"，已恢复正常");
                humidityAlertOnce = true;
            }
        }
    }

    @Override
    protected void onDestroy() {
        wakeUpBu.stop();
        TemperatureHumidityFlag = false;
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==107){
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    try {
                        final ApplicationEntity application = JSONObject.parseObject(result, ApplicationEntity.class);
                        if(application!=null){
                            application.save();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    MessageEvent msg = new MessageEvent();
                                    if(application.getApplicationType()<4)
                                        msg.what=MessageEvent.LIGHT_CHANGE;
                                    if(application.getApplicationType()==Config.APPLICATION_TYPE_HUMIDITY
                                            ||application.getApplicationType()==Config.APPLICATION_TYPE_TEMPERATURE){
                                        msg.what=MessageEvent.SHOW_APPLICATION_FRAGMENT;
                                    }
                                    EventBus.getDefault().post(msg);
                                }
                            }, 500);

                        }
                        else
                            Toast.makeText(this, "非设备类型二维码！", Toast.LENGTH_SHORT).show();
                    }catch (JSONException e){
                        Toast.makeText(this, "非设备类型二维码！", Toast.LENGTH_SHORT).show();
                    }

                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(this, "二维码识别失败！", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onStart() {
        EventBus.getDefault().register(this);
        super.onStart();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if(event.what==MessageEvent.LIGHT_CHANGE){
            navigationItemClick(1);
            navigation.getMenu().getItem(0).setChecked(true);
        }
        if(event.what==MessageEvent.SHOW_APPLICATION_FRAGMENT){
            navigationItemClick(2);
            navigation.getMenu().getItem(1).setChecked(true);
        }

        if(event.what==MessageEvent.TEMPERATURE_LIMIT_ALERT){
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("温度异常")
                    .setContentText("当前温度为："+event.value+"℃")
                    .setConfirmText("确认")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                        }
                    })
                    .show();
        }

        if(event.what==MessageEvent.HUMIDITY_LIMIT_ALERT){
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("湿度异常")
                    .setContentText("当前湿度为："+event.value+"%")
                    .setConfirmText("确认")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                        }
                    })
                    .show();
        }
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}
