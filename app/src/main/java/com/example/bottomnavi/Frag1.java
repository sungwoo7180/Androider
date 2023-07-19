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

        view.findViewById(R.id.my_button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHeartDialog();
            }
        });

        return view;
    }

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

    private void navigateToFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_frame, fragment)
                .addToBackStack(null)
                .commit();
    }
}
