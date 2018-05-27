package online.letmesleep.iotbasedhome;

import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import online.letmesleep.iotbasedhome.bean.ApplicationEntity;

public class CreateQRCodeActivity extends AppCompatActivity {

    @BindView(R.id.create_qrcode_edit_application_name)
    EditText createQrcodeEditApplicationName;
    @BindView(R.id.create_qrcode_edit_application_id)
    EditText createQrcodeEditApplicationId;
    @BindView(R.id.create_qrcode_edit_application_type)
    EditText createQrcodeEditApplicationType;
    @BindView(R.id.create_qrcode_imageview)
    ImageView createQrcodeImageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        setContentView(R.layout.activity_create_qrcode);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
    }

    @OnClick(R.id.create_qrcode_edit_application_create_button)
    public void onViewClicked() {
        ApplicationEntity ae = new ApplicationEntity();
        ae.setApplicationId(createQrcodeEditApplicationId.getText().toString());
        ae.setApplicationName(createQrcodeEditApplicationName.getText().toString());
        ae.setApplicationType(Integer.parseInt(createQrcodeEditApplicationType.getText().toString()));
        createQrcodeImageview.setImageBitmap(CodeUtils.createImage(JSON.toJSONString(ae),
                400,
                400,
                BitmapFactory.decodeResource(getResources(),  R.drawable.xxxxiaoxin)));
    }
}
