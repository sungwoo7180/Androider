package com.example.bottomnavi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Frag1 extends Fragment {
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag1, container, false);

        view.findViewById(R.id.my_button2).setOnClickListener(new View.OnClickListener() {          //심장 대화상자
            @Override
            public void onClick(View v) {
                showHeartDialog();
            }
        });

        view.findViewById(R.id.my_button7).setOnClickListener(new View.OnClickListener() {          //저체온증 대화상자
            @Override
            public void onClick(View v) {
                showHypothermiaDialog();
            }
        });

        view.findViewById(R.id.my_button6).setOnClickListener(new View.OnClickListener() {          //열사병 대화상자
            @Override
            public void onClick(View v) {
                showHeatstrokeDialog();
            }
        });

        view.findViewById(R.id.my_button3).setOnClickListener(new View.OnClickListener() {          //상처 대화상자
            @Override
            public void onClick(View v) {
               showWoundDialog();
            }
        });

        view.findViewById(R.id.my_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 현재 프래그먼트를 새로운 프래그먼트(etc)로 교체합니다
                Fragment etc = new ETC();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.main_frame, etc);
                transaction.addToBackStack(null); // 프래그먼트 트랜잭션을 백 스택에 추가
                transaction.commit();
            }
        });

        return view;

    }
    //심장 버튼 대화상자
    private void showHeartDialog() {
        final String[] heartOptions = {"응급처치 AED 사용법", "심폐소생술", "심장 질환 정보"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("심장과 관련된 응급처치");
        builder.setItems(heartOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        navigateToFragment(new heartOption1());
                        break;
                    case 1:
                        navigateToFragment(new heartOption2());
                        break;
                    case 2:
                        // Handle "심장 질환 정보" option
                        break;
                }
            }
        });
        builder.setNegativeButton("취소", null);
        builder.create().show();
    }
    //저체온증 버튼 대화상자
    private void showHypothermiaDialog() {
        final String[] heartOptions = {"저체온증", "동상"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("냉에 의한 손상");
        builder.setItems(heartOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        navigateToFragment(new heartOption1());
                        break;
                    case 1:
                        navigateToFragment(new heartOption2());
                        break;
                    case 2:
                        break;
                }
            }
        });
        builder.setNegativeButton("취소", null);
        builder.create().show();
    }
    //열사병 버튼 대화상자
    private void showHeatstrokeDialog() {
        final String[] heartOptions = {"열사병/일사병", "탈수", "열상화상", "화학화상"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("열에 의한 손상");
        builder.setItems(heartOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        navigateToFragment(new heartOption1());
                        break;
                    case 1:
                        navigateToFragment(new heartOption2());
                        break;
                    case 2:
                        break;
                }
            }
        });
        builder.setNegativeButton("취소", null);
        builder.create().show();
    }
    //상처 버튼 대화상자
    private void showWoundDialog() {
        final String[] heartOptions = {"상처의 종류", "상처 응급처치"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("상처");
        builder.setItems(heartOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        navigateToFragment(new heartOption1());
                        break;
                    case 1:
                        navigateToFragment(new heartOption2());
                        break;
                    case 2:
                        break;
                }
            }
        });
        builder.setNegativeButton("취소", null);
        builder.create().show();
    }

    private void navigateToFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_frame, fragment)
                .addToBackStack(null)
                .commit();
    }
}
