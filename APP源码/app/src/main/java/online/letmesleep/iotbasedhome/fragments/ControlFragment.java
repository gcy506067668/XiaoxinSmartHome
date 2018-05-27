package online.letmesleep.iotbasedhome.fragments;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
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
import com.github.florent37.expectanim.ExpectAnim;
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
import online.letmesleep.iotbasedhome.R;
import online.letmesleep.iotbasedhome.adapters.LightFragmentAdapter;
import online.letmesleep.iotbasedhome.bean.ApplicationEntity;
import online.letmesleep.iotbasedhome.bean.ApplicationToJson;
import online.letmesleep.iotbasedhome.bean.MessageEvent;
import online.letmesleep.iotbasedhome.config.Config;

import static com.github.florent37.expectanim.core.Expectations.alpha;
import static com.github.florent37.expectanim.core.Expectations.atItsOriginalPosition;
import static com.github.florent37.expectanim.core.Expectations.invisible;
import static com.github.florent37.expectanim.core.Expectations.outOfScreen;
import static com.github.florent37.expectanim.core.Expectations.visible;


/***
 * 灯  Fragment
 */
public class ControlFragment extends BaseFragment implements BaseQuickAdapter.OnItemChildClickListener {


    @BindView(R.id.rv_list)
    RecyclerView rvList;
    Unbinder unbinder;

    LightFragmentAdapter adapter;
    List<ApplicationEntity> datas;
    @BindView(R.id.show_qrcode_layout)
    LinearLayout showQrcodeLayout;
    @BindView(R.id.dialog_qrcode_imageview)
    ImageView dialogQrcodeImageview;
    @BindView(R.id.dialog_application_type)
    TextView dialogApplicationType;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_control, container, false);
        unbinder = ButterKnife.bind(this, v);
        datas = new ArrayList<>();
        adapter = new LightFragmentAdapter(datas);
        adapter.setOnItemChildClickListener(this);
        rvList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvList.setAdapter(adapter);
        loadData();
        return v;
    }


    private void loadData() {
        if (datas != null)
            datas.clear();


        adapter.notifyDataSetChanged();
        datas.addAll(DataSupport.where("applicationType < 4").find(ApplicationEntity.class));
        adapter.notifyDataSetChanged();
//        if (datas.size() == 0) {
//            ApplicationEntity ae2 = new ApplicationEntity();
//            ae2.setApplicationName("庭院");
//            ae2.setApplicationId("temp");
//            ae2.setApplicationType(Config.APPLICATION_TYPE_TEMPERATURE);
//            ae2.setStatus(Config.LIGHT_STATUS_ON);
//            ae2.save();
//
//            ApplicationEntity ae3 = new ApplicationEntity();
//            ae3.setApplicationName("庭院");
//            ae3.setApplicationId("temp");
//            ae3.setApplicationType(Config.APPLICATION_TYPE_HUMIDITY);
//            ae3.setStatus(Config.LIGHT_STATUS_ON);
//            ae3.save();
//
//            ApplicationEntity ae = new ApplicationEntity();
//            ae.setApplicationName("厨房");
//            ae.setApplicationId("light");
//            ae.setApplicationType(Config.APPLICATION_TYPE_LED);
//            ae.setStatus(Config.LIGHT_STATUS_ON);
//            ae.save();
//
//            ApplicationEntity ae1 = new ApplicationEntity();
//            ae1.setApplicationName("客厅");
//            ae1.setApplicationId("light");
//            ae1.setApplicationType(Config.APPLICATION_TYPE_RED_LED);
//            ae1.setStatus(Config.LIGHT_STATUS_OFF);
//            ae1.save();
//            datas.add(ae);
//            datas.add(ae1);
//        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.what == MessageEvent.LIGHT_CHANGE) {
            loadData();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onItemChildClick(final BaseQuickAdapter adapter, View view, final int position) {
        switch (view.getId()) {
            case R.id.adapter_light_edit_button:
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
                                datas.get(position).setApplicationName(editText.getText().toString());
                                datas.get(position).save();
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
            case R.id.adapter_light_remove_button:
                new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("移除设备")
                        .setContentText("是否移除" + Config.getApplicationType(datas.get(position).getApplicationType()) + "设备?")
                        .setConfirmText("移除")
                        .setCancelText("取消")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                datas.get(position).delete();
                                datas.remove(position);
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
            case R.id.adapter_light_show_qrcode_image:
                ApplicationEntity ae = datas.get(position);
                ApplicationToJson aeCopy = new ApplicationToJson();
                aeCopy.setApplicationId(ae.getApplicationId());
                aeCopy.setApplicationType(ae.getApplicationType());
                aeCopy.setApplicationName(ae.getApplicationName());
                String textContent = JSON.toJSONString(aeCopy);
                final Bitmap mBitmap = CodeUtils.createImage(textContent,
                        400,
                        400,
                        BitmapFactory.decodeResource(getResources(), R.drawable.xxxxiaoxin));
                dialogQrcodeImageview.setImageBitmap(mBitmap);
                dialogApplicationType.setText(Config.getApplicationType(datas.get(position).getApplicationType()));
                showQrcodeLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.adapter_light_share_button:
                Toast.makeText(getActivity(), "获取APP_ID失败！", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @OnClick(R.id.show_qrcode_layout)
    public void onViewClicked() {
        showQrcodeLayout.setVisibility(View.INVISIBLE);
    }
}
