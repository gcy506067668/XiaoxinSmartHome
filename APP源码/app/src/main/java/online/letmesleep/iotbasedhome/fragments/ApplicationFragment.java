package online.letmesleep.iotbasedhome.fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.mikephil.charting.data.Entry;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;
import online.letmesleep.iotbasedhome.ChartActivity;
import online.letmesleep.iotbasedhome.MyApplication;
import online.letmesleep.iotbasedhome.R;
import online.letmesleep.iotbasedhome.adapters.ApplicationFragmentAdapter;
import online.letmesleep.iotbasedhome.bean.ApplicationEntity;
import online.letmesleep.iotbasedhome.bean.ApplicationEntityWithData;
import online.letmesleep.iotbasedhome.bean.ApplicationToJson;
import online.letmesleep.iotbasedhome.bean.MessageEvent;
import online.letmesleep.iotbasedhome.config.Config;

import static online.letmesleep.iotbasedhome.MyApplication.applications;
import static org.litepal.crud.DataSupport.find;


/**
 * Created by letmesleep on 2016/11/15.
 * QQ:506067668
 */

public class ApplicationFragment extends BaseFragment implements BaseQuickAdapter.OnItemChildClickListener {

    @BindView(R.id.fa_list)
    RecyclerView faList;
    Unbinder unbinder;
    @BindView(R.id.dialog_qrcode_imageview)
    ImageView dialogQrcodeImageview;
    @BindView(R.id.dialog_application_type)
    TextView dialogApplicationType;
    @BindView(R.id.show_qrcode_layout)
    LinearLayout showQrcodeLayout;
    private List<ApplicationEntityWithData> datas = new ArrayList<>();
    private List<Entry> pointsOfTemp = new ArrayList<>();
    private List<Entry> pointsOfHumi = new ArrayList<>();
    private ApplicationFragmentAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_application, container, false);
        unbinder = ButterKnife.bind(this, v);
        faList.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ApplicationFragmentAdapter(datas);
        adapter.setOnItemChildClickListener(this);
        faList.setAdapter(adapter);
        loadData();
        return v;
    }




    public void loadData() {
        if(MyApplication.applications==null
                ||MyApplication.applications.size()!=DataSupport.count(ApplicationEntity.class)){
            MyApplication.applications = DataSupport.findAll(ApplicationEntity.class);
        }
        datas.clear();
        for (ApplicationEntity entity : MyApplication.applications) {
            if(entity.getApplicationType()==Config.APPLICATION_TYPE_TEMPERATURE){
                ApplicationEntityWithData applicationEntityWithData = new ApplicationEntityWithData(entity, pointsOfTemp);
                applicationEntityWithData.setLimithigh(String.valueOf(Config.HIGH_TEMPERATURE));
                applicationEntityWithData.setLimitlow(String.valueOf(Config.LOW_TEMPERATURE));
                applicationEntityWithData.setLineData(pointsOfTemp);
                applicationEntityWithData.setCurrentValue(currentTemp);
                datas.add(applicationEntityWithData);
            }
        }
        for (ApplicationEntity entity:applications) {
            if(entity.getApplicationType()==Config.APPLICATION_TYPE_HUMIDITY){
                ApplicationEntityWithData applicationEntityWithData = new ApplicationEntityWithData(entity, pointsOfHumi);
                applicationEntityWithData.setLimitlow(String.valueOf(Config.LOW_HUMIDITY_STANDARD));
                applicationEntityWithData.setLimithigh(String.valueOf(Config.HIGH_HUMIDITY_STANDARD));
                applicationEntityWithData.setLineData(pointsOfHumi);
                applicationEntityWithData.setCurrentValue(currentHumi);
                datas.add(applicationEntityWithData);
                }
        }
        adapter.notifyDataSetChanged();
    }






    private String currentTemp = "";
    private String currentHumi = "";
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.what == MessageEvent.TEMPERATURE_UPDATA) {
            pointsOfTemp.add(new Entry(pointsOfTemp.size(),event.value));
            currentTemp = String.valueOf(event.value);

        }
        if(event.what == MessageEvent.HUMIDITY_UPDATA){
            pointsOfHumi.add(new Entry(pointsOfHumi.size(),event.value));
            currentHumi = String.valueOf(event.value);
            loadData();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onItemChildClick(final BaseQuickAdapter adapter, View view, final int position) {
        switch (view.getId()) {
            case R.id.adapter_application__share_button:
                Toast.makeText(getActivity(), "获取APP_ID失败！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.adapter_application_edit_button:
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View v = inflater.inflate(R.layout.dialog_fragment_show_qrcode
                        , (ViewGroup) getActivity().findViewById(R.id.dialog_fragment_show_qrcode));
                final EditText editText = (EditText) v.findViewById(R.id.dialog_fragment_light_edit_application_name);
                editText.setText(datas.get(position).getApplicationName());
                new MaterialDialog.Builder(getActivity())
                        .customView(v, false)
                        .positiveText("确认")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                ApplicationEntity ae = DataSupport.find(ApplicationEntity.class,datas.get(position).getId());
                                ae.setApplicationName(editText.getText().toString());
                                ae.save();
                                dialog.dismiss();
                                loadData();
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
            case R.id.adapter_application_remove_button:
                new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("移除设备")
                        .setContentText("是否移除" + Config.getApplicationType(datas.get(position).getApplicationType()) + "设备?")
                        .setConfirmText("移除")
                        .setCancelText("取消")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                DataSupport.delete(ApplicationEntity.class,datas.get(position).getId());
                                loadData();
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();
                break;
            case R.id.adapter_application_setting_button:
                Intent intent = new Intent(getActivity(), ChartActivity.class);
                intent.putExtra("id",datas.get(position).getId());
                startActivity(intent);
                break;
            case R.id.adapter_application_qrcode_image:
                ApplicationEntityWithData ae = datas.get(position);
                ApplicationToJson aeCopy = new ApplicationToJson();
                aeCopy.setApplicationId(ae.getApplicationId());
                aeCopy.setApplicationType(ae.getApplicationType());
                aeCopy.setApplicationName(ae.getApplicationName());
                String textContent = JSON.toJSONString(aeCopy);
                final Bitmap mBitmap = CodeUtils.createImage(textContent,
                        400,
                        400,
                        BitmapFactory.decodeResource(getResources(),  R.drawable.xxxxiaoxin));
                dialogQrcodeImageview.setImageBitmap(mBitmap);
                dialogApplicationType.setText(Config.getApplicationType(datas.get(position).getApplicationType()));
                showQrcodeLayout.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onStart() {
        EventBus.getDefault().register(this);
        super.onStart();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @OnClick(R.id.show_qrcode_layout)
    public void onViewClicked() {
        showQrcodeLayout.setVisibility(View.INVISIBLE);
    }
}
