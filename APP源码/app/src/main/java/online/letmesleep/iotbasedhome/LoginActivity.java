package online.letmesleep.iotbasedhome;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.github.florent37.expectanim.ExpectAnim;
import com.github.florent37.expectanim.listener.AnimationEndListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import online.letmesleep.iotbasedhome.util.MOKHttpUtil;
import online.letmesleep.iotbasedhome.util.PreferenceUtil;

import static com.github.florent37.expectanim.core.Expectations.alpha;
import static com.github.florent37.expectanim.core.Expectations.atItsOriginalPosition;
import static com.github.florent37.expectanim.core.Expectations.invisible;
import static com.github.florent37.expectanim.core.Expectations.outOfScreen;
import static com.github.florent37.expectanim.core.Expectations.visible;


public class LoginActivity extends AppCompatActivity {


    @BindView(R.id.login_activity_left_layout)
    LinearLayout loginActivityLeftLayout;
    @BindView(R.id.login_activity_right_layout)
    LinearLayout loginActivityRightLayout;
    @BindView(R.id.email)
    AutoCompleteTextView email;
    @BindView(R.id.user_name_layer)
    TextInputLayout userNameLayer;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.user_password_layer)
    TextInputLayout userPasswordLayer;
    @BindView(R.id.email_sign_in_button)
    CircularProgressButton emailSignInButton;
    @BindView(R.id.user_register)
    TextView userRegister;
    @BindView(R.id.forget_password)
    TextView forgetPassword;
    @BindView(R.id.email_login_form)
    LinearLayout emailLoginForm;
    @BindView(R.id.login_form_alpha)
    ImageView loginFormAlpha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        email.setText(PreferenceUtil.load(MyApplication.getInstance(),"username",""));
        password.setText(PreferenceUtil.load(MyApplication.getInstance(),"password",""));

    }

    private void atemptLogin(final String username, final String password){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                    MOKHttpUtil.login(username, password, new MOKHttpUtil.Callback() {
                        @Override
                        public void onSuccess(String resp) {
                            if(resp.equals("true")){
                                PreferenceUtil.save(MyApplication.getInstance(),"username",username);
                                PreferenceUtil.save(MyApplication.getInstance(),"password",password);
                                finish();
                            }else{
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginActivity.this, "用户名或密码错误！", Toast.LENGTH_SHORT).show();
                                }
                            });
                            }
                            showProgress(false);
                        }

                        @Override
                        public void onFaild(final String err) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginActivity.this, ""+err, Toast.LENGTH_SHORT).show();
                                }
                            });
                            showProgress(false);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void loginAnimPlay(final String email, final String password) {
        new ExpectAnim()
                .expect(userNameLayer)
                .toBe(
                        outOfScreen(Gravity.LEFT),
                        invisible()
                )
                .expect(loginFormAlpha)
                .toBe(
                        invisible(),
                        alpha(0)
                )
                .expect(loginActivityLeftLayout)
                .toBe(
                        outOfScreen(Gravity.LEFT),
                        invisible()
                )
                .expect(loginActivityRightLayout)
                .toBe(
                        outOfScreen(Gravity.RIGHT),
                        invisible()
                )
                .expect(userPasswordLayer)
                .toBe(
                        outOfScreen(Gravity.RIGHT),
                        invisible()
                )
                .expect(userRegister)
                .toBe(
                        outOfScreen(Gravity.BOTTOM),
                        invisible()
                )
                .expect(forgetPassword)
                .toBe(
                        outOfScreen(Gravity.BOTTOM),
                        invisible()
                )
                .toAnimation()
                .setDuration(1000)
                .addEndListener(new AnimationEndListener() {
                    @Override
                    public void onAnimationEnd(ExpectAnim expectAnim) {
                        showProgress(true);
                        atemptLogin(email, password);
                    }
                })
                .start();

    }

    private void loginErrorAnimPlay() {
        new ExpectAnim()
                .expect(userNameLayer)
                .toBe(
                        visible(),
                        atItsOriginalPosition()
                )
                .expect(loginFormAlpha)
                .toBe(
                        visible(),
                        alpha(1)
                )
                .expect(userPasswordLayer)
                .toBe(
                        visible(),
                        atItsOriginalPosition()
                )
                .expect(loginActivityLeftLayout)
                .toBe(
                        visible(),
                        atItsOriginalPosition()
                )
                .expect(loginActivityRightLayout)
                .toBe(
                        visible(),
                        atItsOriginalPosition()
                )
                .expect(userRegister)
                .toBe(
                        visible(),
                        atItsOriginalPosition()
                )
                .expect(forgetPassword)
                .toBe(
                        visible(),
                        atItsOriginalPosition()
                )
                .toAnimation()
                .setDuration(1000)
                .start();
    }


    @OnClick({R.id.user_register, R.id.forget_password,R.id.email_sign_in_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.user_register:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.email_sign_in_button:
                checkUsernameAndPassword();
                break;
            case R.id.forget_password:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, "功能暂未开启", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
    }

    private void checkUsernameAndPassword() {
        loginAnimPlay(email.getText().toString(),password.getText().toString());

    }

    private String usernameFromRegisterActivity;
    private String passwordFromRegisterActivity;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            usernameFromRegisterActivity = data.getStringExtra("telephone");
            passwordFromRegisterActivity = data.getStringExtra("password");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    email.setText(usernameFromRegisterActivity);
                    password.setText(passwordFromRegisterActivity);
                }
            });
            loginAnimPlay(usernameFromRegisterActivity, passwordFromRegisterActivity);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showProgress(boolean show) {
        if (show) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    emailSignInButton.setIndeterminateProgressMode(true);
                    emailSignInButton.setProgress(50);
                }
            });

        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    emailSignInButton.setProgress(0);
                    loginErrorAnimPlay();

                }
            });

        }


    }
}
