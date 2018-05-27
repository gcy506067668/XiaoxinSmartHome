package online.letmesleep.iotbasedhome;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.florent37.expectanim.ExpectAnim;
import com.github.florent37.expectanim.listener.AnimationEndListener;
import com.github.florent37.expectanim.listener.AnimationStartListener;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SynthesizerListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import online.letmesleep.iotbasedhome.ifly.IFLYSpeaker;

import static com.github.florent37.expectanim.core.Expectations.alpha;
import static com.github.florent37.expectanim.core.Expectations.atItsOriginalPosition;
import static com.github.florent37.expectanim.core.Expectations.outOfScreen;
import static com.github.florent37.expectanim.core.Expectations.visible;

public class IntroduceActivity extends AppCompatActivity {

    @BindView(R.id.button_login_register)
    Button buttonLoginRegister;
    @BindView(R.id.text_login)
    TextView textLogin;
    @BindView(R.id.text_register)
    TextView textRegister;
    @BindView(R.id.text_follow_user)
    TextView textFollowUser;
    @BindView(R.id.layout_login_register)
    LinearLayout layoutLoginRegister;
    @BindView(R.id.text_add_application)
    TextView textAddApplication;
    @BindView(R.id.text_edit_application)
    TextView textEditApplication;
    @BindView(R.id.text_remove_application)
    TextView textRemoveApplication;
    @BindView(R.id.text_setting_application)
    TextView textSettingApplication;
    @BindView(R.id.text_share_application)
    TextView textShareApplication;
    @BindView(R.id.text_qrcode_application)
    TextView textQrcodeApplication;
    @BindView(R.id.button_management_application)
    Button buttonManagementApplication;
    @BindView(R.id.layout_application_management)
    LinearLayout layoutApplicationManagement;
    @BindView(R.id.button_control_application)
    Button buttonControlApplication;
    @BindView(R.id.text_control_jdq)
    TextView textControlJdq;
    @BindView(R.id.text_control_led)
    TextView textControlLed;
    @BindView(R.id.text_control_by_voice)
    TextView textControlByVoice;
    @BindView(R.id.layout_light_control)
    LinearLayout layoutLightControl;
    @BindView(R.id.text_alert_high_temp)
    TextView textAlertHighTemp;
    @BindView(R.id.text_alert_low_temp)
    TextView textAlertLowTemp;
    @BindView(R.id.text_temp)
    TextView textTemp;
    @BindView(R.id.text_alert_high_humi)
    TextView textAlertHighHumi;
    @BindView(R.id.text_alert_low_humi)
    TextView textAlertLowHumi;
    @BindView(R.id.text_humi)
    TextView textHumi;
    @BindView(R.id.button_humi_temp)
    Button buttonHumiTemp;
    @BindView(R.id.layout_temp_humidity)
    LinearLayout layoutTempHumidity;
    @BindView(R.id.button_speak_transalte)
    Button buttonSpeakTransalte;
    @BindView(R.id.layout_local_language)
    LinearLayout layoutLocalLanguage;
    @BindView(R.id.layout_chat_tuling)
    LinearLayout layoutChatTuling;
    @BindView(R.id.layout_start_up)
    LinearLayout layoutStartUp;
    @BindView(R.id.layout_start_down)
    LinearLayout layoutStartDown;
    @BindView(R.id.tuling_chat_1)
    TextView tulingChat1;
    @BindView(R.id.tuling_chat_2)
    TextView tulingChat2;
    @BindView(R.id.tuling_chat_3)
    TextView tulingChat3;
    @BindView(R.id.tuling_chat_4)
    TextView tulingChat4;
    @BindView(R.id.tuling_chat_5)
    TextView tulingChat5;
    @BindView(R.id.tuling_chat_6)
    TextView tulingChat6;
    @BindView(R.id.button_tuling_chat)
    Button buttonTulingChat;
    @BindView(R.id.finish_introduce)
    Button finishIntroduce;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        setContentView(R.layout.activity_introduce);
        ButterKnife.bind(this);
        handler = new Handler();
        speak("你好，我是小新，欢迎来到智能家居系统介绍页面，你可以叫我小新。我是智能家居系" +
                "统的语音交互控制终端。下面是智能家居系统的功能简述：",1);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startAnim();
            }
        }, 2000);

        initView();

    }

    public void startAnim() {
        new ExpectAnim()
                .expect(layoutStartUp)
                .toBe(
                        outOfScreen(Gravity.TOP)
                )
                .expect(layoutStartDown)
                .toBe(
                        outOfScreen(Gravity.BOTTOM)
                )
                .toAnimation()
                .setDuration(1500)
                .start();
    }

    int count = 0;
    /****1.本系统主要功能包括登录与注册，登录之后设备信息将会跟随用户，完成设备信息的云端存储与实时更新
     *   2.在设备管理中你可以操作自己家用电器设备例如L E D灯，继电器，温湿度传感器等的增、删、改、查，其中包括
     *   3.在设备控制中本系统使用继电器，" +
                 "L E D灯模拟家用电灯的控制，" +
                "继电器可以控制二百二十伏交流电，" +
                "所以我们可以将本系统的继电器控制端直接接入家用照明设备，" +
                "即可改造家用照明设备，使之成为智能家居系统中的一员
     *    4.本系统集成了科大讯飞的语音合成，" +
                "百度的语音唤醒和语音识别，" +
                "在A P P运行期间所有的A P P交互均可以由语音交互来实现，" +
                 "例如你有一个L E D灯设备，起个名字叫厨房，表示厨房的灯，" +
                 "你可以使用 小新同学 命令词来唤醒我，在听到滴的一声以后使用语音" +
                 "“帮我打开厨房的灯”，来打开该 L E D灯，或者唤醒之后对我说 " +
              "我们家的温度是多少来获取温度传感器的参数。" +
                 "如果想了解更多的语音控制命令请阅读命令文档
     *    5.在环境感知中系统以图表的形式展示温湿度传感器的实时数据，" +
                "另外用户可以设置传感器数据的上下限来预警，" +
                "当实时数据超出上下限度时系统会弹出警报来提醒用户。
     *     6.在用户与A P P进行语音交互时用户可更改发音人，" +
                 "该系统支持六种方言发音包括粤语、台湾话、四川话、" +
                 "东北话、湖南话、河南话，以及引文发音，" +
                 "例如你可以使用唤醒词 小新同学  唤醒之后  " +
                 "对我说 你会说四川话吗？ " +
                 "系统将会自动识别并记忆方言语种将发音更改为四川方言 " +
                 "另外用户可以使用诸如命令词“把你好翻译成河南话”" +
                 "来把普通话翻译成以上六种方言之一，" +
                 "或者直接使用命令词 翻译某某某   将普通话翻译为英语***/
    private void startIntroduceAnim(int which) {
        count++;
        switch (which){
            case 1:
                new ExpectAnim()
                        .expect(buttonLoginRegister)
                        .toBe(
                                atItsOriginalPosition()
                        )
                        .toAnimation()
                        .addStartListener(new AnimationStartListener() {
                            @Override
                            public void onAnimationStart(ExpectAnim expectAnim) {
                                speak("首先是登录与注册，包括登录、注册、设备信息云端存储", 5);
                            }
                        })
                        .addEndListener(new AnimationEndListener() {
                            @Override
                            public void onAnimationEnd(ExpectAnim expectAnim) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        startIntroduceAnim(2);
                                    }
                                });
                            }
                        })
                        .setDuration(1000)
                        .start();
                break;
            case 2:
                new ExpectAnim()
                        .expect(textLogin)
                        .toBe(
                                atItsOriginalPosition()
                        )
                        .toAnimation()
                        .addEndListener(new AnimationEndListener() {
                            @Override
                            public void onAnimationEnd(ExpectAnim expectAnim) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        startIntroduceAnim(3);
                                    }
                                });
                            }
                        })
                        .setDuration(500)
                        .start();
                break;
            case 3:
                new ExpectAnim()
                        .expect(textRegister)
                        .toBe(
                                atItsOriginalPosition()
                        )
                        .toAnimation()
                        .addEndListener(new AnimationEndListener() {
                            @Override
                            public void onAnimationEnd(ExpectAnim expectAnim) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        startIntroduceAnim(4);
                                    }
                                });
                            }
                        })
                        .setDuration(500)
                        .start();
                break;
            case 4:
                new ExpectAnim()
                        .expect(textFollowUser)
                        .toBe(
                                atItsOriginalPosition()
                        )
                        .toAnimation()
                        .setDuration(500)
                        .start();
                break;
            case 5:
                new ExpectAnim()
                        .expect(buttonManagementApplication)
                        .toBe(
                                atItsOriginalPosition()
                        )
                        .toAnimation()
                        .addStartListener(new AnimationStartListener() {
                            @Override
                            public void onAnimationStart(ExpectAnim expectAnim) {
                                speak("在设备管理功能中你可以，", 7);
                            }
                        })
                        .setDuration(1000)
                        .start();
                break;
            case 6:
                new ExpectAnim()
                        .expect(buttonManagementApplication)
                        .toBe(
                                atItsOriginalPosition()
                        )
                        .toAnimation()
                        .setDuration(1000)
                        .start();
                break;
            case 7:
                new ExpectAnim()
                        .expect(textAddApplication)
                        .toBe(
                                atItsOriginalPosition()
                        )
                        .toAnimation()
                        .addStartListener(new AnimationStartListener() {
                            @Override
                            public void onAnimationStart(ExpectAnim expectAnim) {
                                speak("扫描二维码添加设备、", 8);

                            }
                        })
                        .setDuration(500)
                        .start();
                break;
            case 8:
                new ExpectAnim()
                        .expect(textEditApplication)
                        .toBe(
                                atItsOriginalPosition()
                        )

                        .toAnimation()
                        .addStartListener(new AnimationStartListener() {
                            @Override
                            public void onAnimationStart(ExpectAnim expectAnim) {
                                speak("编辑设备昵称。", 9);
                            }
                        })
                        .setDuration(500)
                        .start();
                break;
            case 9:
                new ExpectAnim()
                        .expect(textRemoveApplication)
                        .toBe(
                                atItsOriginalPosition()
                        )
                        .toAnimation()
                        .addStartListener(new AnimationStartListener() {
                            @Override
                            public void onAnimationStart(ExpectAnim expectAnim) {
                                speak("移除设备、", 10);
                            }
                        })
                        .setDuration(500)
                        .start();
                break;
            case 10:
                new ExpectAnim()
                        .expect(textSettingApplication)
                        .toBe(
                                atItsOriginalPosition()
                        )
                        .toAnimation()
                        .addEndListener(new AnimationEndListener() {
                            @Override
                            public void onAnimationEnd(ExpectAnim expectAnim) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        startIntroduceAnim(11);
                                    }
                                });
                            }
                        })
                        .setDuration(500)
                        .start();
                break;
            case 11:
                new ExpectAnim()
                        .expect(textShareApplication)
                        .toBe(
                                atItsOriginalPosition()
                        )
                        .toAnimation()
                        .addStartListener(new AnimationStartListener() {
                            @Override
                            public void onAnimationStart(ExpectAnim expectAnim) {
                                speak("通过二维码分享设备。",12);
                            }
                        })
                        .setDuration(500)
                        .start();
                break;
            case 12:
                new ExpectAnim()
                        .expect(textQrcodeApplication)
                        .toBe(
                                atItsOriginalPosition()
                        )
                        .toAnimation()
                        .addStartListener(new AnimationStartListener() {
                            @Override
                            public void onAnimationStart(ExpectAnim expectAnim) {
                                speak("另外我可以帮助您生成二维码，扫描和识别二维码",13);
                            }
                        })
                        .setDuration(500)
                        .start();
                break;
            case 13:
                new ExpectAnim()
                        .expect(buttonControlApplication)
                        .toBe(
                                atItsOriginalPosition()
                        )
                        .toAnimation()
                        .addStartListener(new AnimationStartListener() {
                            @Override
                            public void onAnimationStart(ExpectAnim expectAnim) {
                                speak("下一个功能是设备控制",14);
                            }
                        })
                        .setDuration(1000)
                        .start();
                break;
            case 14:
                new ExpectAnim()
                        .expect(textControlJdq)
                        .toBe(
                                atItsOriginalPosition()
                        )
                        .toAnimation()
                        .addStartListener(new AnimationStartListener() {
                            @Override
                            public void onAnimationStart(ExpectAnim expectAnim) {
                                speak("在设备控制中你可以手动或语音控制灯的开关，继电器的开关。",16);
                            }
                        })
                        .addEndListener(new AnimationEndListener() {
                            @Override
                            public void onAnimationEnd(ExpectAnim expectAnim) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        startIntroduceAnim(15);
                                    }
                                });
                            }
                        })
                        .setDuration(500)
                        .start();
                break;
            case 15:
                new ExpectAnim()
                        .expect(textControlLed)
                        .toBe(
                                atItsOriginalPosition()
                        )
                        .toAnimation()
                        .setDuration(500)
                        .start();
                break;
            case 16:
                new ExpectAnim()
                        .expect(textControlByVoice)
                        .toBe(
                                atItsOriginalPosition()
                        )
                        .toAnimation()
                        .addStartListener(new AnimationStartListener() {
                            @Override
                            public void onAnimationStart(ExpectAnim expectAnim) {
                                speak("本系统内集成了语音唤醒、语音识别、语音合成、如果你想了解更多的语音控制命令请阅读命令文档",17);
                            }
                        })
                        .setDuration(500)
                        .start();
                break;
            case 17:
                new ExpectAnim()
                        .expect(buttonHumiTemp)
                        .toBe(
                                atItsOriginalPosition()
                        )
                        .toAnimation()
                        .addStartListener(new AnimationStartListener() {
                            @Override
                            public void onAnimationStart(ExpectAnim expectAnim) {
                                speak("在环境感知界面包括折线图展示温湿度、高低温、高低湿度警报功能。",21);
                            }
                        })
                        .addEndListener(new AnimationEndListener() {
                            @Override
                            public void onAnimationEnd(ExpectAnim expectAnim) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        startIntroduceAnim(18);
                                    }
                                });
                            }
                        })
                        .setDuration(1000)
                        .start();
                break;
            case 18:
                new ExpectAnim()
                        .expect(textTemp)
                        .toBe(
                                atItsOriginalPosition()
                        )
                        .toAnimation()
                        .addEndListener(new AnimationEndListener() {
                            @Override
                            public void onAnimationEnd(ExpectAnim expectAnim) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        startIntroduceAnim(19);
                                    }
                                });
                            }
                        })
                        .setDuration(500)
                        .start();
                break;
            case 19:
                new ExpectAnim()
                        .expect(textHumi)
                        .toBe(
                                atItsOriginalPosition()
                        )
                        .toAnimation()
                        .addEndListener(new AnimationEndListener() {
                            @Override
                            public void onAnimationEnd(ExpectAnim expectAnim) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        startIntroduceAnim(20);
                                    }
                                });
                            }
                        })
                        .setDuration(500)
                        .start();
                break;
            case 20:
                new ExpectAnim()
                        .expect(textAlertHighTemp)
                        .toBe(
                                atItsOriginalPosition()
                        )
                        .expect(textAlertLowTemp)
                        .toBe(
                                atItsOriginalPosition()
                        )
                        .expect(textAlertHighHumi)
                        .toBe(
                                atItsOriginalPosition()
                        )
                        .expect(textAlertLowHumi)
                        .toBe(
                                atItsOriginalPosition()
                        )
                        .toAnimation()
                        .setDuration(500)
                        .start();
                break;
            case 21:
                new ExpectAnim()
                        .expect(buttonSpeakTransalte)
                        .toBe(
                                atItsOriginalPosition()
                        )
                        .toAnimation()
                        .addStartListener(new AnimationStartListener() {
                            @Override
                            public void onAnimationStart(ExpectAnim expectAnim) {
                                speak("接下来介绍的是发音语翻译，其主要功能有语音或手动更改发音人、英文翻译、方言翻译。具体使用请阅读发音与翻译使用手册",23);
                            }
                        })
                        .addEndListener(new AnimationEndListener() {
                            @Override
                            public void onAnimationEnd(ExpectAnim expectAnim) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        startIntroduceAnim(22);
                                    }
                                });
                            }
                        })
                        .setDuration(1000)
                        .start();
                break;
            case 22:
                new ExpectAnim()
                        .expect(layoutLocalLanguage)
                        .toBe(
                                atItsOriginalPosition()
                        )
                        .toAnimation()
                        .setDuration(1000)
                        .start();
                break;
            case 23:
                new ExpectAnim()
                        .expect(buttonTulingChat)
                        .toBe(
                                atItsOriginalPosition()
                        )
                        .toAnimation()
                        .addStartListener(new AnimationStartListener() {
                            @Override
                            public void onAnimationStart(ExpectAnim expectAnim) {
                                speak("当语音交互命令词不能识别的时候系统会将命令词交给图灵机器人，" +
                                        "图灵机器人会与用户聊天、讲笑话、讲故事等等，");
                            }
                        })
                        .addEndListener(new AnimationEndListener() {
                            @Override
                            public void onAnimationEnd(ExpectAnim expectAnim) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        startIntroduceAnim(24);
                                    }
                                });
                            }
                        })
                        .setDuration(1000)
                        .start();
                break;
            case 24:
                new ExpectAnim()
                        .expect(tulingChat1)
                        .toBe(
                                atItsOriginalPosition()
                        )
                        .toAnimation()
                        .addEndListener(new AnimationEndListener() {
                            @Override
                            public void onAnimationEnd(ExpectAnim expectAnim) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        startIntroduceAnim(25);
                                    }
                                });
                            }
                        })
                        .setDuration(500)
                        .start();
                break;
            case 25:
                new ExpectAnim()
                        .expect(tulingChat2)
                        .toBe(
                                atItsOriginalPosition()
                        )
                        .toAnimation()
                        .addEndListener(new AnimationEndListener() {
                            @Override
                            public void onAnimationEnd(ExpectAnim expectAnim) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        startIntroduceAnim(26);
                                    }
                                });
                            }
                        })
                        .setDuration(500)
                        .start();
                break;
            case 26:
                new ExpectAnim()
                        .expect(tulingChat3)
                        .toBe(
                                atItsOriginalPosition()
                        )
                        .toAnimation()
                        .addEndListener(new AnimationEndListener() {
                            @Override
                            public void onAnimationEnd(ExpectAnim expectAnim) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        startIntroduceAnim(27);
                                    }
                                });
                            }
                        })
                        .setDuration(500)
                        .start();
                break;
            case 27:
                new ExpectAnim()
                        .expect(tulingChat4)
                        .toBe(
                                atItsOriginalPosition()
                        )
                        .toAnimation()
                        .addEndListener(new AnimationEndListener() {
                            @Override
                            public void onAnimationEnd(ExpectAnim expectAnim) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        startIntroduceAnim(28);
                                    }
                                });
                            }
                        })
                        .setDuration(500)
                        .start();
                break;
            case 28:
                new ExpectAnim()
                        .expect(tulingChat5)
                        .toBe(
                                atItsOriginalPosition()
                        )
                        .toAnimation()
                        .addEndListener(new AnimationEndListener() {
                            @Override
                            public void onAnimationEnd(ExpectAnim expectAnim) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        startIntroduceAnim(29);
                                    }
                                });
                            }
                        })
                        .setDuration(500)
                        .start();
                break;
            case 29:
                new ExpectAnim()
                        .expect(tulingChat6)
                        .toBe(
                                atItsOriginalPosition()
                        )
                        .toAnimation()
                        .addEndListener(new AnimationEndListener() {
                            @Override
                            public void onAnimationEnd(ExpectAnim expectAnim) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        startIntroduceAnim(30);
                                    }
                                });
                            }
                        })
                        .setDuration(500)
                        .start();
                break;
            case 30:
                new ExpectAnim()
                        .expect(finishIntroduce)
                        .toBe(
                                atItsOriginalPosition()
                        )
                        .toAnimation()
                        .setDuration(500)
                        .start();
                break;
        }
    }

    private void initView() {
        new ExpectAnim()
                .expect(buttonLoginRegister)
                .toBe(
                        outOfScreen(Gravity.LEFT)

                )
                .expect(buttonSpeakTransalte)
                .toBe(
                        outOfScreen(Gravity.LEFT)
                )
                .expect(buttonHumiTemp)
                .toBe(
                        outOfScreen(Gravity.RIGHT)
                )
                .expect(buttonControlApplication)
                .toBe(
                        outOfScreen(Gravity.LEFT)
                )
                .expect(buttonManagementApplication)
                .toBe(
                        outOfScreen(Gravity.RIGHT)
                )
                .expect(buttonTulingChat)
                .toBe(
                        outOfScreen(Gravity.RIGHT)
                )
                .expect(textAlertLowHumi)
                .toBe(
                        outOfScreen(Gravity.LEFT)
                )
                .expect(textAlertHighHumi)
                .toBe(
                        outOfScreen(Gravity.LEFT)
                )
                .expect(textAlertLowTemp)
                .toBe(
                        outOfScreen(Gravity.LEFT)
                )
                .expect(textAlertHighTemp)
                .toBe(
                        outOfScreen(Gravity.LEFT)
                )
                .expect(textHumi)
                .toBe(
                        outOfScreen(Gravity.LEFT)
                )
                .expect(textTemp)
                .toBe(
                        outOfScreen(Gravity.LEFT)
                )
                .expect(textLogin)
                .toBe(
                        outOfScreen(Gravity.RIGHT)
                )
                .expect(textRegister)
                .toBe(
                        outOfScreen(Gravity.RIGHT)
                )
                .expect(textFollowUser)
                .toBe(
                        outOfScreen(Gravity.RIGHT)
                )
                .expect(textAddApplication)
                .toBe(
                        outOfScreen(Gravity.LEFT)
                )
                .expect(textEditApplication)
                .toBe(
                        outOfScreen(Gravity.LEFT)
                )
                .expect(textRemoveApplication)
                .toBe(
                        outOfScreen(Gravity.LEFT)
                )
                .expect(textSettingApplication)
                .toBe(
                        outOfScreen(Gravity.LEFT)
                )
                .expect(textShareApplication)
                .toBe(
                        outOfScreen(Gravity.LEFT)
                )
                .expect(textQrcodeApplication)
                .toBe(
                        outOfScreen(Gravity.LEFT)
                )
                .expect(textControlJdq)
                .toBe(
                        outOfScreen(Gravity.RIGHT)
                )
                .expect(textControlLed)
                .toBe(
                        outOfScreen(Gravity.RIGHT)
                )
                .expect(textControlByVoice)
                .toBe(
                        outOfScreen(Gravity.RIGHT)
                )
                .expect(layoutLocalLanguage)
                .toBe(
                        outOfScreen(Gravity.RIGHT)
                )
                .expect(tulingChat1)
                .toBe(
                        outOfScreen(Gravity.LEFT)
                )
                .expect(tulingChat2)
                .toBe(
                        outOfScreen(Gravity.LEFT)
                )
                .expect(tulingChat3)
                .toBe(
                        outOfScreen(Gravity.LEFT)
                )
                .expect(tulingChat4)
                .toBe(
                        outOfScreen(Gravity.LEFT)
                )
                .expect(tulingChat5)
                .toBe(
                        outOfScreen(Gravity.LEFT)
                )
                .expect(tulingChat6)
                .toBe(
                        outOfScreen(Gravity.LEFT)
                )
                .expect(finishIntroduce)
                .toBe(
                        outOfScreen(Gravity.BOTTOM)
                )
                .toAnimation()
                .start();
    }

    @OnClick(R.id.finish_introduce)
    public void onClick() {
        finish();
    }

    public void speak(String text, SynthesizerListener listener) {
        IFLYSpeaker.Speak(this, text, MyApplication.speaker, listener);
    }

    public void speak(String text) {
        IFLYSpeaker.Speak(this, text, MyApplication.speaker);
    }

    public void speak(String text, final int startWhich){
        speak(text, new SynthesizerListener() {
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startIntroduceAnim(startWhich);
                    }
                });
            }

            @Override
            public void onEvent(int i, int i1, int i2, Bundle bundle) {

            }
        });
    }
}
