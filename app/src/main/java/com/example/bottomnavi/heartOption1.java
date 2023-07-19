package com.example.bottomnavi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

public class heartOption1 extends Fragment {
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.heart1, container, false);

        // 툴바 생성
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true 만 해도 백버튼이 생김
        activity.getSupportActionBar().setTitle("AED(자동 심장 충격기) 사용법"); // 툴바 제목 설정

        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 현재 프래그먼트를 새로운 프래그먼트(heartOption2)로 교체합니다
                Fragment heartOption2Fragment = new heartOption2();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.main_frame, heartOption2Fragment);
                transaction.addToBackStack(null); // 프래그먼트 트랜잭션을 백 스택에 추가하여, heartOption1로 돌아갈 수 있도록 합니다
                transaction.commit();
            }
        });

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
