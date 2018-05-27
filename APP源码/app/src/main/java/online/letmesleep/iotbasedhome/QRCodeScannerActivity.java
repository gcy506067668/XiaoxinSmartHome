package online.letmesleep.iotbasedhome;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;


import com.alibaba.fastjson.JSON;
import com.github.florent37.expectanim.ExpectAnim;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import online.letmesleep.iotbasedhome.bean.ApplicationEntity;
import online.letmesleep.iotbasedhome.util.ImageUtil;

import static com.github.florent37.expectanim.core.Expectations.atItsOriginalPosition;
import static com.github.florent37.expectanim.core.Expectations.invisible;
import static com.github.florent37.expectanim.core.Expectations.outOfScreen;
import static com.github.florent37.expectanim.core.Expectations.visible;

public class QRCodeScannerActivity extends AppCompatActivity {

    @BindView(R.id.add_application_name_edittext)
    EditText addApplicationNameEdittext;
    @BindView(R.id.add_application_id_edittext)
    EditText addApplicationIdEdittext;
    @BindView(R.id.add_application_layout)
    LinearLayout addApplicationLayout;
    private boolean light = false;
    private int REQUEST_IMAGE = 109;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        setContentView(R.layout.activity_qrcode_scanner);
        ButterKnife.bind(this);
        /**
         * 执行扫面Fragment的初始化操作
         */
        CaptureFragment captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.my_camera);

        captureFragment.setAnalyzeCallback(analyzeCallback);
        /**
         * 替换我们的扫描控件
         */
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showAddApplicationByText(false);
            }
        }, 1000);
    }

    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
            bundle.putString(CodeUtils.RESULT_STRING, result);
            resultIntent.putExtras(bundle);
            QRCodeScannerActivity.this.setResult(RESULT_OK, resultIntent);
            QRCodeScannerActivity.this.finish();
        }

        @Override
        public void onAnalyzeFailed() {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
            bundle.putString(CodeUtils.RESULT_STRING, "");
            resultIntent.putExtras(bundle);
            QRCodeScannerActivity.this.setResult(RESULT_OK, resultIntent);
            QRCodeScannerActivity.this.finish();
        }
    };

    @OnClick({R.id.add_application_by_text, R.id.button_exit, R.id.button_album, R.id.button_openlight, R.id.button_create_qrcode, R.id.button_ok, R.id.button_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.add_application_by_text:
                showAddApplicationByText(true);
                break;
            case R.id.button_exit:
                finish();
                break;
            case R.id.button_album:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE);
                break;
            case R.id.button_openlight:
                if (light) {
                    light = false;
                    CodeUtils.isLightEnable(false);
                } else {
                    light = true;
                    CodeUtils.isLightEnable(true);
                }
                break;
            case R.id.button_create_qrcode:
                startActivity(new Intent(this,CreateQRCodeActivity.class));
                break;
            case R.id.button_ok:
                if("".equals(addApplicationIdEdittext.getText().toString())
                        ||"".equals(addApplicationNameEdittext.getText().toString()))
                return;
                ApplicationEntity ae = new ApplicationEntity();
                ae.setApplicationId(addApplicationIdEdittext.getText().toString());
                ae.setApplicationName(addApplicationNameEdittext.getText().toString());
                Intent resultIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
                bundle.putString(CodeUtils.RESULT_STRING, JSON.toJSONString(ae));
                resultIntent.putExtras(bundle);
                QRCodeScannerActivity.this.setResult(RESULT_OK, resultIntent);
                QRCodeScannerActivity.this.finish();
                break;
            case R.id.button_cancel:
                showAddApplicationByText(false);
                break;
        }
    }

    public void showAddApplicationByText(boolean show) {
        if (!show) {
            new ExpectAnim()
                    .expect(addApplicationLayout)
                    .toBe(
                            outOfScreen(Gravity.BOTTOM),
                            invisible()
                    )
                    .toAnimation()
                    .setDuration(500)
                    .start();
        } else {
            new ExpectAnim()
                    .expect(addApplicationLayout)
                    .toBe(
                            atItsOriginalPosition(),
                            visible()
                    )
                    .toAnimation()
                    .setDuration(500)
                    .start();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (data != null) {
                final Uri uri = data.getData();
                //ContentResolver cr = getContentResolver();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            CodeUtils.analyzeBitmap(ImageUtil.getImageAbsolutePath(QRCodeScannerActivity.this,uri), new CodeUtils.AnalyzeCallback() {
                                @Override
                                public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                                    Log.e("result",""+result);
                                }

                                @Override
                                public void onAnalyzeFailed() {
                                    Log.e("result","error");
                                }
                            });


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }
    }
}
