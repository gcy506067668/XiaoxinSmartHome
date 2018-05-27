package online.letmesleep.iotbasedhome.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.florent37.expectanim.ExpectAnim;
import com.iflytek.thirdparty.I;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import online.letmesleep.iotbasedhome.IntroduceActivity;
import online.letmesleep.iotbasedhome.LoginActivity;
import online.letmesleep.iotbasedhome.MainActivity;
import online.letmesleep.iotbasedhome.MyApplication;
import online.letmesleep.iotbasedhome.R;
import online.letmesleep.iotbasedhome.config.Config;
import online.letmesleep.iotbasedhome.ifly.IFLYSpeaker;
import online.letmesleep.iotbasedhome.util.PreferenceUtil;

import static com.github.florent37.expectanim.core.Expectations.alpha;
import static com.github.florent37.expectanim.core.Expectations.invisible;
import static com.github.florent37.expectanim.core.Expectations.visible;


/**
 * Created by letmesleep on 2016/11/15.
 * QQ:506067668
 */

public class UserFragment extends BaseFragment {

    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.master_layout)
    LinearLayout masterLayout;
    @BindView(R.id.depency_scroll_text)
    TextView depencyScrollText;
    @BindView(R.id.depency_layout)
    LinearLayout depencyLayout;
    @BindView(R.id.version_layout)
    LinearLayout versionLayout;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user, container, false);
        unbinder = ButterKnife.bind(this, v);
        depencyScrollText.setMovementMethod(ScrollingMovementMethod.getInstance());
        username.setText(PreferenceUtil.load(MyApplication.getInstance(),"username","登录/注册"));
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.fragment_user_login,R.id.button_basic_setting, R.id.button_voice_setting, R.id.button_th_limit, R.id.button_master, R.id.button_depency, R.id.button_version})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragment_user_login:
                startActivity(LoginActivity.class);
                break;

            case R.id.button_basic_setting:
                Intent intent = new Intent(getActivity(),IntroduceActivity.class);
                startActivity(intent);
                break;
            case R.id.button_voice_setting:
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View v = inflater.inflate(R.layout.dialog_voice_setting
                        , (ViewGroup) getActivity().findViewById(R.id.dialog_user_fragment_choose_speaker));
                final RadioGroup radis = (RadioGroup) v.findViewById(R.id.dialog_choose_speaker);
                new MaterialDialog.Builder(getActivity())
                        .title("选择发音人")
                        .customView(v, false)
                        .positiveText("确认")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                switch(radis.getCheckedRadioButtonId()){
                                    case R.id.speaker_dongbei:
                                        MyApplication.speaker = IFLYSpeaker.Speaker.DongBei;
                                        PreferenceUtil.save(MyApplication.getInstance(),"speaker", Config.DONGBEI_SPEAKER);
                                        break;
                                    case R.id.speaker_henan:
                                        MyApplication.speaker = IFLYSpeaker.Speaker.HeNan;
                                        PreferenceUtil.save(MyApplication.getInstance(),"speaker", Config.HENAN_SPEAKER);
                                        break;
                                    case R.id.speaker_hunan:
                                        MyApplication.speaker = IFLYSpeaker.Speaker.HUNAN;
                                        //PreferenceUtil.save(MyApplication.getInstance(),"speaker", Config.DONGBEI_SPEAKER);
                                        break;
                                    case R.id.speaker_little_girl:
                                        MyApplication.speaker = IFLYSpeaker.Speaker.LittleGirl;
                                        PreferenceUtil.save(MyApplication.getInstance(),"speaker", Config.LITTLE_GIRL_SPEAKER);
                                        break;
                                    case R.id.speaker_taiwan:
                                        MyApplication.speaker = IFLYSpeaker.Speaker.TaiWan;
                                        PreferenceUtil.save(MyApplication.getInstance(),"speaker", Config.TAIWAN_SPEAKER);
                                        break;
                                    case R.id.speaker_sichuan:
                                        MyApplication.speaker = IFLYSpeaker.Speaker.SiChuan;
                                        PreferenceUtil.save(MyApplication.getInstance(),"speaker", Config.SICHUAN_SPEAKER);
                                        break;
                                    case R.id.speaker_xiaoxin:
                                        MyApplication.speaker = IFLYSpeaker.Speaker.Xiaoxin;
                                        PreferenceUtil.save(MyApplication.getInstance(),"speaker", Config.XIAOXIN_SPEAKER);
                                        break;
                                    case R.id.speaker_young_boy:
                                        MyApplication.speaker = IFLYSpeaker.Speaker.EnglishOrChineseBoy;
                                        //PreferenceUtil.save(MyApplication.getInstance(),"speaker", Config.DONGBEI_SPEAKER);
                                        break;
                                    case R.id.speaker_young_english_boy:
                                        MyApplication.speaker = IFLYSpeaker.Speaker.EnglishOrChineseGirl;
                                        PreferenceUtil.save(MyApplication.getInstance(),"speaker", Config.NORMAL_SPEAKER);
                                        break;
                                    case R.id.speaker_young_english_girl:
                                        MyApplication.speaker = IFLYSpeaker.Speaker.PureEnglishGirl;
                                        PreferenceUtil.save(MyApplication.getInstance(),"speaker", Config.NORMAL_SPEAKER);
                                        break;
                                    case R.id.speaker_young_girl:
                                        MyApplication.speaker = IFLYSpeaker.Speaker.EnglishOrChineseGirl;
                                        PreferenceUtil.save(MyApplication.getInstance(),"speaker", Config.NORMAL_SPEAKER);
                                        break;
                                    case R.id.speaker_yueyu:
                                        MyApplication.speaker = IFLYSpeaker.Speaker.Yueyu;
                                        PreferenceUtil.save(MyApplication.getInstance(),"speaker", Config.YUYUE_SPEAKER);
                                        break;
                                }
                                IFLYSpeaker.Speak(getActivity(), "设置成功", MyApplication.speaker);
                                dialog.dismiss();

                            }
                        })
                        .negativeText("取消")
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
            case R.id.button_th_limit:
                LayoutInflater inflater1 = getActivity().getLayoutInflater();
                View dialog = inflater1.inflate(R.layout.dialog_limit_setting
                        , (ViewGroup) getActivity().findViewById(R.id.dialog_user_fragment_limitsetting));
                final EditText tempLow = (EditText)dialog.findViewById(R.id.dialog_limit_setting_temp_low);
                final EditText tempHigh = (EditText)dialog.findViewById(R.id.dialog_limit_setting_temp_high);
                final EditText humiLow = (EditText)dialog.findViewById(R.id.dialog_limit_setting_humi_low);
                final EditText humiHigh = (EditText)dialog.findViewById(R.id.dialog_limit_setting_humi_high);
                new MaterialDialog.Builder(getActivity())
                        .title("温湿度设置")
                        .customView(dialog, false)
                        .positiveText("确认")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                Config.HIGH_TEMPERATURE = Integer.parseInt(tempHigh.getText().toString());
                                Config.LOW_TEMPERATURE = Integer.parseInt(tempLow.getText().toString());
                                Config.HIGH_HUMIDITY_STANDARD = Integer.parseInt(humiHigh.getText().toString());
                                Config.LOW_HUMIDITY_STANDARD = Integer.parseInt(humiLow.getText().toString());
                                PreferenceUtil.save(MyApplication.getInstance(),Config.PREFENCE_TEMP_HIGH, Config.HIGH_TEMPERATURE);
                                PreferenceUtil.save(MyApplication.getInstance(),Config.PREFENCE_TEMP_LOW, Config.LOW_TEMPERATURE);
                                PreferenceUtil.save(MyApplication.getInstance(),Config.PREFENCE_HUMIDITY_HIGH, Config.HIGH_HUMIDITY_STANDARD);
                                PreferenceUtil.save(MyApplication.getInstance(),Config.PREFENCE_HUMIDITY_LOW, Config.LOW_HUMIDITY_STANDARD);
                                dialog.dismiss();

                            }
                        })
                        .negativeText("取消")
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
            case R.id.button_master:
                showTextview(1);
                break;
            case R.id.button_depency:
                showTextview(2);
                break;
            case R.id.button_version:
                showTextview(3);
                break;
        }
    }
    private void showTextview(int which){
        switch (which){
            case 1:
                playAnim(masterLayout,depencyLayout,versionLayout);
                break;
            case 2:
                playAnim(depencyLayout,masterLayout,versionLayout);
                break;
            case 3:
                playAnim(versionLayout,masterLayout,depencyLayout);
                break;
        }
    }

    private void playAnim(View vshow,View vhide1,View vhide2){
        new ExpectAnim()
                .expect(vshow)
                .toBe(
                        visible(),
                        alpha(1)

                )
                .expect(vhide1)
                .toBe(
                        alpha(0),
                        invisible()
                )
                .expect(vhide2)
                .toBe(
                        alpha(0),
                        invisible()
                )
                .toAnimation()
                .start();
    }
}
