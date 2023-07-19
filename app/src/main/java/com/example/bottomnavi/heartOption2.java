package com.example.bottomnavi;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class heartOption2 extends Fragment {
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.heart2, container, false);

        // 툴바 생성
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true 만 해도 백버튼이 생김
        activity.getSupportActionBar().setTitle("심폐소생술(CPR)"); // 툴바 제목 설정
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setTitle("AED(자동 심장 충격기) 사용법");
        }

        TextView textView3 = view.findViewById(R.id.textView3);
        String text = "1. 심정지 및 무호흡 확인 \n\n양어깨를 두드리며 말을 걸고 눈과 귀로 심정지 및 무호흡 유무를 확인한다. (반응과 호흡이 있으면 심정지 아님)";

        // 스타일을 적용하기 위해 SpannableString 생성
        SpannableString spannableString = new SpannableString(text);

        // 특정 부분에 볼드 스타일을 적용
        int startIndex = text.indexOf("1. 심정지 및 무호흡 확인");
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), startIndex, startIndex + 16, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // TextView 에 SpannableString 설정
        textView3.setText(spannableString);

        view.findViewById(R.id.heartBackButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 뒤로가기 동작을 처리하는 코드 작성
                getParentFragmentManager().popBackStack();
            }
        });
        return view;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: {// toolbar 의 back 키 눌렀을 때 동작
                // 액티비티 이동 또는 프래그먼트의 뒤로가기 동작을 처리하는 코드 작성
                getParentFragmentManager().popBackStack();
                return true; }
        }
        return super.onOptionsItemSelected(item);
    }
}
