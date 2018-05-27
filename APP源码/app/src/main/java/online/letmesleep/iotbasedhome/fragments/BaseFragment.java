package online.letmesleep.iotbasedhome.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by letmesleep on 2016/11/15.
 * QQ:506067668
 */

public class BaseFragment extends Fragment implements View.OnClickListener {


    @Override
    public void onClick(View view) {

    }

    protected void startActivity(Class<?> clazz){
        Intent intent = new Intent(getActivity(),clazz);
        getActivity().startActivity(intent);
    }

    protected void startActivity(Class<?> clazz, Bundle bundle){
        Intent intent = new Intent(getActivity(),clazz);
        if(bundle!=null)
            intent.putExtras(bundle);
        getActivity().startActivity(intent);
    }


    public void startActivityForResult(Class<?> tClass, Bundle bundle,
                                       int requestCode) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), tClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

}
