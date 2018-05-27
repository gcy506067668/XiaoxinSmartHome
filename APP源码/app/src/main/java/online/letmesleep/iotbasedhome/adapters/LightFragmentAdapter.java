package online.letmesleep.iotbasedhome.adapters;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.kyleduo.switchbutton.SwitchButton;

import java.util.List;

import online.letmesleep.iotbasedhome.R;
import online.letmesleep.iotbasedhome.bean.ApplicationEntity;
import online.letmesleep.iotbasedhome.config.Config;
import online.letmesleep.iotbasedhome.util.ApplicationControlUtil;

/**
 * Created by Letmesleep on 2018/4/22.
 */

public class LightFragmentAdapter extends BaseQuickAdapter<ApplicationEntity,BaseViewHolder> {

    private List<ApplicationEntity> data ;

    public LightFragmentAdapter(@Nullable List<ApplicationEntity> data) {
        super(R.layout.adapter_light_fragment, data);
        this.data = data;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final ApplicationEntity item) {
        helper.setText(R.id.adapter_textview_application_name,item.getApplicationName())
                .setText(R.id.adapter_textview_application_type, Config.getApplicationType(item.getApplicationType()));
        setViewContent(helper,item);
        ((SwitchButton)helper.getView(R.id.adapter_switch_button)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    item.setStatus(Config.LIGHT_STATUS_ON);
                    ApplicationControlUtil.sendLightControlCommand(item.getApplicationId()+"on");
                    item.save();
                }else{
                    item.setStatus(Config.LIGHT_STATUS_OFF);
                    ApplicationControlUtil.sendLightControlCommand(item.getApplicationId()+"off");
                    item.save();
                }
                setViewContent(helper,item);

            }
        });

        helper.addOnClickListener(R.id.adapter_light_remove_button)
                .addOnClickListener(R.id.adapter_light_edit_button)
                .addOnClickListener(R.id.adapter_light_show_qrcode_image)
                .addOnClickListener(R.id.adapter_light_share_button);
    }

    private void setViewContent(BaseViewHolder helper,ApplicationEntity item){
        if(Config.LIGHT_STATUS_ON.equals(item.getStatus())){
            helper.setText(R.id.adapter_textview_switchbutton, "ON")
                    .setImageResource(R.id.adapter_light_picture,R.drawable.light_on_24dp);
            ((TextView)helper.getView(R.id.adapter_textview_application_name)).setTextColor(Color.parseColor("#FF9D00"));
            ((TextView)helper.getView(R.id.adapter_textview_application_type)).setTextColor(Color.parseColor("#FF9D00"));
            ((TextView)helper.getView(R.id.adapter_textview_switchbutton)).setTextColor(Color.parseColor("#FF9D00"));
            ((SwitchButton)helper.getView(R.id.adapter_switch_button)).setChecked(true);
            helper.setBackgroundRes(R.id.adapter_light_layout,R.drawable.adapter_control_light_on);
        }else{
            helper.setText(R.id.adapter_textview_switchbutton, "OFF")
                    .setImageResource(R.id.adapter_light_picture,R.drawable.light_off_24dp);
            ((TextView)helper.getView(R.id.adapter_textview_application_name)).setTextColor(Color.parseColor("#50b2b8d7"));
            ((TextView)helper.getView(R.id.adapter_textview_application_type)).setTextColor(Color.parseColor("#50b2b8d7"));
            ((TextView)helper.getView(R.id.adapter_textview_switchbutton)).setTextColor(Color.parseColor("#b2b8d7"));
            ((SwitchButton)helper.getView(R.id.adapter_switch_button)).setChecked(false);
            helper.setBackgroundRes(R.id.adapter_light_layout,R.drawable.adapter_control_light_off);
        }
    }
}
