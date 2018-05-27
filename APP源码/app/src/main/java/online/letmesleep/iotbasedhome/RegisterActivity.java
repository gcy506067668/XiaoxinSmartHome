package online.letmesleep.iotbasedhome;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.dd.CircularProgressButton;
import com.github.florent37.expectanim.ExpectAnim;
import com.github.florent37.expectanim.listener.AnimationEndListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import online.letmesleep.iotbasedhome.util.MOKHttpUtil;


import static com.github.florent37.expectanim.core.Expectations.atItsOriginalPosition;
import static com.github.florent37.expectanim.core.Expectations.invisible;
import static com.github.florent37.expectanim.core.Expectations.outOfScreen;
import static com.github.florent37.expectanim.core.Expectations.visible;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.user_register_phone)
    EditText userRegisterPhone;
    @BindView(R.id.user_register_password)
    EditText userRegisterPassword;
    @BindView(R.id.user_register_truename)
    EditText userRegisterTruename;
    @BindView(R.id.register_phone_yanzhengma)
    CircularProgressButton registerPhoneYanzhengma;
    @BindView(R.id.register_user_button)
    CircularProgressButton registerUserButton;
    @BindView(R.id.user_register_login)
    TextView userRegisterLogin;
    @BindView(R.id.user_register_virify)
    EditText userRegisterVirify;
    @BindView(R.id.user_register_phone_layer)
    TextInputLayout userRegisterPhoneLayer;
    @BindView(R.id.user_register_password_layer)
    TextInputLayout userRegisterPasswordLayer;
    @BindView(R.id.user_register_truename_layer)
    TextInputLayout userRegisterTruenameLayer;
    @BindView(R.id.user_register_virify_layer)
    LinearLayout userRegisterVirifyLayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        getSupportActionBar().hide();
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(" 注 册 ");


    }

    @OnClick({R.id.register_phone_yanzhengma, R.id.register_user_button, R.id.user_register_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.register_phone_yanzhengma:
                registerPhoneYanzhengma.setIndeterminateProgressMode(true);
                registerPhoneYanzhengma.setProgress(50);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                registerPhoneYanzhengma.setProgress(100);
                            }
                        });
                    }
                }).start();
                break;
            case R.id.register_user_button:
                attempRegister();
                break;
            case R.id.user_register_login:
                finish();
                break;
        }
    }

    private void registerAnimPlay() {
        new ExpectAnim()
                .expect(userRegisterPhoneLayer)
                .toBe(
                        outOfScreen(Gravity.TOP),
                        invisible()
                )
                .expect(userRegisterPasswordLayer)
                .toBe(
                        outOfScreen(Gravity.RIGHT),
                        invisible()
                )
                .expect(userRegisterTruenameLayer)
                .toBe(
                        outOfScreen(Gravity.LEFT),
                        invisible()
                )
                .expect(userRegisterVirifyLayer)
                .toBe(
                        outOfScreen(Gravity.LEFT),
                        invisible()
                )
                .expect(registerPhoneYanzhengma)
                .toBe(
                        outOfScreen(Gravity.LEFT),
                        invisible()
                )
                .expect(userRegisterLogin)
                .toBe(
                        outOfScreen(Gravity.LEFT),
                        invisible()
                )
                .toAnimation()
                .setDuration(1000)
                .addEndListener(new AnimationEndListener() {
                    @Override
                    public void onAnimationEnd(ExpectAnim expectAnim) {
                        showProgress(true);
                    }
                })
                .start();

    }

    private void showProgress(boolean b) {
        if (b) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    registerUserButton.setIndeterminateProgressMode(true);
                    registerUserButton.setProgress(50);
                }
            });

            MOKHttpUtil.register(userRegisterPhone.getText().toString(),userRegisterPassword.getText().toString());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1500);
                        Intent intent = new Intent();
                        intent.putExtra("telephone", userRegisterPhone.getText().toString());
                        intent.putExtra("password", userRegisterPassword.getText().toString());
                        setResult(1, intent);
                        finish();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    registerUserButton.setIndeterminateProgressMode(true);
                    registerUserButton.setProgress(0);
                    registerErrorAnimPlay();
                }
            });
        }

    }

    private void registerErrorAnimPlay() {

        new ExpectAnim()
                .expect(userRegisterPhoneLayer)
                .toBe(
                        visible(),
                        atItsOriginalPosition()
                )
                .expect(userRegisterPasswordLayer)
                .toBe(
                        visible(),
                        atItsOriginalPosition()
                )
                .expect(userRegisterTruenameLayer)
                .toBe(
                        visible(),
                        atItsOriginalPosition()
                )
                .expect(userRegisterVirifyLayer)
                .toBe(
                        visible(),
                        atItsOriginalPosition()
                )
                .expect(registerPhoneYanzhengma)
                .toBe(
                        visible(),
                        atItsOriginalPosition()
                )
                .expect(userRegisterLogin)
                .toBe(
                        visible(),
                        atItsOriginalPosition()
                )
                .toAnimation()
                .setDuration(1000)
                .addEndListener(new AnimationEndListener() {
                    @Override
                    public void onAnimationEnd(ExpectAnim expectAnim) {
                        //showProgress(true);

                    }
                })
                .start();


    }

    public void attempRegister(){
        if(isValueValid())
            registerAnimPlay();
    }



    private boolean isValueValid(){
        String phone = userRegisterPhone.getText().toString();
        String password = userRegisterPassword.getText().toString();
        String trueName = userRegisterTruename.getText().toString();
        if(phone.length()!=11) {
            userRegisterPhone.setError("手机号有误！");
            userRegisterPhone.requestFocus();
            return false;
        }
        if(password.length()<6){
            userRegisterPassword.setError("密码长度过短！");
            userRegisterPassword.requestFocus();
            return false;
        }
        return true;
    }

}
