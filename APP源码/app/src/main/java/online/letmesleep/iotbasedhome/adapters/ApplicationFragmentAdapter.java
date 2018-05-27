package online.letmesleep.iotbasedhome.adapters;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.kyleduo.switchbutton.SwitchButton;

import java.util.List;

import online.letmesleep.iotbasedhome.R;
import online.letmesleep.iotbasedhome.bean.ApplicationEntity;
import online.letmesleep.iotbasedhome.bean.ApplicationEntityWithData;
import online.letmesleep.iotbasedhome.config.Config;
import online.letmesleep.iotbasedhome.util.MChartUtil;

/**
 * Created by Letmesleep on 2018/4/22.
 */

public class ApplicationFragmentAdapter extends BaseQuickAdapter<ApplicationEntityWithData,BaseViewHolder> {


    public ApplicationFragmentAdapter(@Nullable List<ApplicationEntityWithData> data) {
        super(R.layout.adapter_application_fragment, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ApplicationEntityWithData item) {
        helper.setText(R.id.adapter_textview_application_type,  Config.getApplicationType(item.getApplicationType()))
                .setText(R.id.adapter_textview_application_name,item.getApplicationName())
                .setText(R.id.adapter_textview_application_current_value,"当前值:"+item.getCurrentValue())
                .setText(R.id.adapter_textview_application_highlimit,"高预警值:"+item.getLimithigh())
                .setText(R.id.adapter_textview_application_lowlimit,"低预警值:"+item.getLimitlow())
                .addOnClickListener(R.id.adapter_application__share_button)
                .addOnClickListener(R.id.adapter_application_qrcode_image)
                .addOnClickListener(R.id.adapter_application_setting_button)
                .addOnClickListener(R.id.adapter_application_edit_button)
                .addOnClickListener(R.id.adapter_application_remove_button);
        MChartUtil.getSettedChart((LineChart)helper.getView(R.id.chart),item.getLineData(),item);
    }
}
