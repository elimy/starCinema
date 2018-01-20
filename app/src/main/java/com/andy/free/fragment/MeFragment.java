package com.andy.free.fragment;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andy.free.R;

/**
 * Created by Andy Lau on 2017/10/18.
 */

public class MeFragment extends Fragment {

    private ImageView loginBg;
    private LinearLayout loginLayout;
    private ImageView headImage;
    private TextView userName;
    private LinearLayout saleLayout;
    private final static int radius=25;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView= inflater.inflate(R.layout.fragment_me,null);

        loginBg= (ImageView) rootView.findViewById(R.id.person_bg);
        loginLayout= (LinearLayout) rootView.findViewById(R.id.login_linear);
        headImage= (ImageView) rootView.findViewById(R.id.head_img);
        userName= (TextView) rootView.findViewById(R.id.name_or_tip);

        //设置模糊背景
        applyBlur();

        return rootView;

    }


    /*设置海报的模糊背景*/
    private void applyBlur(){
        Object localObject = ((BitmapDrawable)getResources().getDrawable(R.drawable.ic_image_loading)).getBitmap();
        RenderScript localRenderScript = RenderScript.create(getActivity());
        Allocation localAllocation1 = Allocation.createFromBitmap(localRenderScript, (Bitmap)localObject);
        Allocation localAllocation2 = Allocation.createTyped(localRenderScript, localAllocation1.getType());
        if (Build.VERSION.SDK_INT >= 17)
        {
            ScriptIntrinsicBlur localScriptIntrinsicBlur = ScriptIntrinsicBlur.create(localRenderScript, Element.U8_4(localRenderScript));
            localScriptIntrinsicBlur.setInput(localAllocation1);
            localScriptIntrinsicBlur.setRadius(radius);
            localScriptIntrinsicBlur.forEach(localAllocation2);
            localAllocation2.copyTo((Bitmap)localObject);
            localRenderScript.destroy();
        }
        localObject = new BitmapDrawable(getResources(), (Bitmap)localObject);
        if (Build.VERSION.SDK_INT >= 16) {
            loginBg.setBackground((Drawable)localObject);
        }
    }
}
