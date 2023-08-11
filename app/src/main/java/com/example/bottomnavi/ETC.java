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
import androidx.viewpager.widget.ViewPager;

import com.example.bottomnavi.R;
import com.example.bottomnavi.etc.BoneFracture;
import com.example.bottomnavi.etc.Bug;
import com.example.bottomnavi.etc.Dia;
import com.example.bottomnavi.etc.Exacer;
import com.example.bottomnavi.etc.EyeAttack;
import com.example.bottomnavi.etc.Fire;
import com.example.bottomnavi.etc.HeatStroke;
import com.example.bottomnavi.etc.Hemorr;
import com.example.bottomnavi.etc.Hypo;
import com.example.bottomnavi.etc.Myocardial;
import com.example.bottomnavi.etc.Poison;


public class ETC extends Fragment {
    private View view;
    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.etc, container, false);

        // 툴바 생성
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true 만 해도 백버튼이 생김
        activity.getSupportActionBar().setTitle("기타");                 // 툴바 제목 설정


        view.findViewById(R.id.BackButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 뒤로가기 동작을 처리하는 코드 작성
                getParentFragmentManager().popBackStack();
            }
        });
        //1. 천식발작 asthma(exacer)
        view.findViewById(R.id.asthmaButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 현재 프래그먼트를 새로운 프래그먼트(etc)로 교체합니다
                Fragment asthma = new Exacer();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.main_frame, asthma);
                transaction.addToBackStack(null); // 프래그먼트 트랜잭션을 백 스택에 추가
                transaction.commit();
            }
        });
        //2. 저체온증 hypothermia
        view.findViewById(R.id.hypothermiaButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 현재 프래그먼트를 새로운 프래그먼트(etc)로 교체합니다
                Fragment hypothermia = new Hypo();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.main_frame, hypothermia);
                transaction.addToBackStack(null); // 프래그먼트 트랜잭션을 백 스택에 추가
                transaction.commit();
            }
        });
        //3. 당뇨 diabetes
        view.findViewById(R.id.diaButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 현재 프래그먼트를 새로운 프래그먼트(etc)로 교체합니다
                Fragment diabetes = new Dia();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.main_frame, diabetes);
                transaction.addToBackStack(null); // 프래그먼트 트랜잭션을 백 스택에 추가
                transaction.commit();
            }
        });
        //4. 출혈 bleeding(hemorrhage)
        view.findViewById(R.id.bleedingButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 현재 프래그먼트를 새로운 프래그먼트(etc)로 교체합니다
                Fragment bleeding = new Hemorr();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.main_frame, bleeding);
                transaction.addToBackStack(null); // 프래그먼트 트랜잭션을 백 스택에 추가
                transaction.commit();
            }
        });
        //5. 머리손상 bleeding(hemorrhage) 미구현
        /*
        view.findViewById(R.id.bleedingButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 현재 프래그먼트를 새로운 프래그먼트(etc)로 교체합니다
                Fragment bleeding = new Hemorr();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.main_frame, bleeding);
                transaction.addToBackStack(null); // 프래그먼트 트랜잭션을 백 스택에 추가
                transaction.commit();
            }
        });
        */
        view.findViewById(R.id.bugButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 현재 프래그먼트를 새로운 프래그먼트(etc)로 교체합니다
                Fragment bug = new Bug();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.main_frame, bug);
                transaction.addToBackStack(null); // 프래그먼트 트랜잭션을 백 스택에 추가
                transaction.commit();
            }
        });
        //6. 심장 발작 myocardial
        view.findViewById(R.id.myocardialButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 현재 프래그먼트를 새로운 프래그먼트(etc)로 교체합니다
                Fragment myocardial = new Myocardial();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.main_frame, myocardial);
                transaction.addToBackStack(null); // 프래그먼트 트랜잭션을 백 스택에 추가
                transaction.commit();
            }
        });
        //7. 중독 poison
        view.findViewById(R.id.addictionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 현재 프래그먼트를 새로운 프래그먼트(etc)로 교체합니다
                Fragment poison = new Poison();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.main_frame, poison);
                transaction.addToBackStack(null); // 프래그먼트 트랜잭션을 백 스택에 추가
                transaction.commit();
            }
        });
        //8. 열사병	heatstroke
        view.findViewById(R.id.heatStrokeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 현재 프래그먼트를 새로운 프래그먼트(etc)로 교체합니다
                Fragment heatStroke = new HeatStroke();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.main_frame, heatStroke);
                transaction.addToBackStack(null); // 프래그먼트 트랜잭션을 백 스택에 추가
                transaction.commit();
            }
        });
        //화상
        view.findViewById(R.id.boneFractureButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 현재 프래그먼트를 새로운 프래그먼트(etc)로 교체합니다
                Fragment bone = new BoneFracture();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.main_frame, bone);
                transaction.addToBackStack(null); // 프래그먼트 트랜잭션을 백 스택에 추가
                transaction.commit();
            }
        });
        //눈 손상
        view.findViewById(R.id.eyeAttackButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 현재 프래그먼트를 새로운 프래그먼트(etc)로 교체합니다
                Fragment eye = new EyeAttack();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.main_frame, eye);
                transaction.addToBackStack(null); // 프래그먼트 트랜잭션을 백 스택에 추가
                transaction.commit();
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
