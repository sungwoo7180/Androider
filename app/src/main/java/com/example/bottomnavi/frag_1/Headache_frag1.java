package com.example.bottomnavi.frag_1;

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

import com.example.bottomnavi.R;
import com.example.bottomnavi.frag_1.heartOption2;

public class Headache_frag1 extends Fragment {
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.headache_frag1, container, false);

        // 툴바 생성
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true 만 해도 백버튼이 생김
        activity.getSupportActionBar().setTitle("두통의 응급처치 및 예방"); // 툴바 제목 설정

        view.findViewById(R.id.BackButton).setOnClickListener(new View.OnClickListener() {
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
