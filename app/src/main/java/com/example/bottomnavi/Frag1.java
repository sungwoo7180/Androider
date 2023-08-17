package com.example.bottomnavi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.bottomnavi.etc.HeatStroke;
import com.example.bottomnavi.etc.Hypo;
import com.example.bottomnavi.etc.Myocardial;
import com.example.bottomnavi.frag_1.Dehydration;
import com.example.bottomnavi.frag_1.Headache_frag1;
import com.example.bottomnavi.frag_1.Stomachache_frag1;
import com.example.bottomnavi.frag_1.heartOption1;
import com.example.bottomnavi.frag_1.heartOption2;

import java.util.List;

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
        view.findViewById(R.id.my_button0).setOnClickListener(new View.OnClickListener() {          //인터넷 대화상자
            @Override
            public void onClick(View v) {
                internet();
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
        view.findViewById(R.id.my_button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 현재 프래그먼트를 새로운 프래그먼트(etc)로 교체합니다
                Fragment dehydration = new Dehydration();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.main_frame, dehydration);
                transaction.addToBackStack(null); // 프래그먼트 트랜잭션을 백 스택에 추가
                transaction.commit();
            }
        });
        view.findViewById(R.id.my_button5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 현재 프래그먼트를 새로운 프래그먼트(etc)로 교체합니다
                Fragment Stomachache = new Stomachache_frag1();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.main_frame, Stomachache);
                transaction.addToBackStack(null); // 프래그먼트 트랜잭션을 백 스택에 추가
                transaction.commit();
            }
        });
        view.findViewById(R.id.my_button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 현재 프래그먼트를 새로운 프래그먼트(etc)로 교체합니다
                Fragment headache = new Headache_frag1();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.main_frame, headache);
                transaction.addToBackStack(null); // 프래그먼트 트랜잭션을 백 스택에 추가
                transaction.commit();
            }
        });

        return view;

    }
    //심장 버튼 대화상자
    private void showHeartDialog() {
        final String[] heartOptions = {"응급처치 AED 사용법", "심폐소생술", "심장 발작"};

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
                        navigateToFragment(new Myocardial());
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
                        navigateToFragment(new Hypo());
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
                        navigateToFragment(new HeatStroke());
                        break;
                    case 1:
                        navigateToFragment(new Dehydration());
                        break;
                    case 2:
                        break;                                  //추가 필요
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
    //인터넷 버튼 대화상자
    private void internet() {
        final String[] heartOptions = {"기본 응급처치요령", "상황별 응급 처치요령"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("인터넷 사용이 가능할 때");
        builder.setItems(heartOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        String url = "https://naver.com"; // 여기에 실제 URL을 넣으세요
                        openWebPage(url); // 웹 페이지 열기
                        break;
                    case 1:
                        String url2 = "http://www.e-gen.or.kr/app/contents2Basics.do"; // 여기에 실제 URL을 넣으세요
                        openWebPage(url2); // 웹 페이지 열기
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

    // 웹 페이지를 열기 위한 메서드
    private void openWebPage(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        PackageManager packageManager = getContext().getPackageManager();
        // 웹 브라우저 앱이 있는지 확인
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
        boolean isWebBrowserInstalled = !activities.isEmpty();

        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivity(intent);
        } else {
            // 웹 브라우저 앱이 없는 경우 처리
            Toast.makeText(getContext(), "웹 브라우저 앱을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}
