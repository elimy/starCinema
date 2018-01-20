package com.andy.free.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.andy.free.R;
import com.andy.free.X5PlayWebActivity;
import com.andy.free.utils.ConstantUtil;

import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/10/18.
 */

public class SearchFragment extends Fragment {

    private EditText editText;
    private Button playBtn;
    private RelativeLayout searchRootLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_search,null);

        editText= (EditText) view.findViewById(R.id.url_edit);
        playBtn= (Button) view.findViewById(R.id.play);
        searchRootLayout= (RelativeLayout) view.findViewById(R.id.search_root_layout);
        searchRootLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                searchRootLayout.setFocusable(true);
                searchRootLayout.setFocusableInTouchMode(true);
                searchRootLayout.requestFocus();

                InputMethodManager imm= (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);

                if (imm.isActive()){

                    //imm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);//如果是现实则隐藏，如果是隐藏则显示
                    imm.hideSoftInputFromWindow(editText.getWindowToken(),0);
                }
                return false;
            }
        });

        checkAddPlay();

        return view;
    }

    /*
    * 检测链接是否正确，并跳转播放页面
    * */
    private void checkAddPlay() {

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String inputContent=editText.getText().toString().trim();
                if (inputContent.equals("")){
                    inputContent=editText.getHint().toString().trim();

                    Log.d("checkAddPlay",inputContent);

                    Toast.makeText(getActivity(),"小伙伴儿,你没输入链接哦！",Toast.LENGTH_SHORT).show();

                    Intent intent=new Intent(getActivity(), X5PlayWebActivity.class);
                    intent.putExtra("trueUrl", ConstantUtil.BASE_URL+inputContent);
                    startActivity(intent);
                }else {
                    if (Patterns.WEB_URL.matcher(inputContent).matches()){
                        Toast.makeText(getActivity(),"格式合法，不知道能不能播放！",Toast.LENGTH_SHORT).show();

                        Intent intent=new Intent(getActivity(), X5PlayWebActivity.class);
                        intent.putExtra("trueUrl",ConstantUtil.BASE_URL+inputContent);
                        startActivity(intent);

                    }else {

                        Toast.makeText(getActivity(),"小伙伴儿,你的链接有问题！",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }
}
