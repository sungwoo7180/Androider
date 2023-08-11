package com.example.bottomnavi;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Frag3 extends Fragment {
    private View view;
    private EditText editText1, editText2, editText3, editText4;
    private TextView textView1, textView2, textView3, textView4;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag3, container, false);
        editText1 = view.findViewById(R.id.textView15);
        editText2 = view.findViewById(R.id.textView19);
        editText3 = view.findViewById(R.id.textView23);
        editText4 = view.findViewById(R.id.textView27);

        textView1 = view.findViewById(R.id.editTextTextMultiLine2);
        textView2 = view.findViewById(R.id.editTextTextMultiLine3);
        textView3 = view.findViewById(R.id.editTextTextMultiLine4);
        textView4 = view.findViewById(R.id.textView24);

        Button saveButton = view.findViewById(R.id.button1);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDataToFile();
                updateTextViews();
                clearEditTexts();
            }
        });

        Button loadButton = view.findViewById(R.id.button2);
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDataFromFile(); // 저장된 데이터 불러오기
                updateTextViews(); // 추가: 불러온 데이터를 TextView에 업데이트
            }
        });
        loadDataFromFile(); // 추가: 파일에서 데이터를 불러옴
        updateTextViews();
        return view;
    }

    private void saveDataToFile() {
        String data = "병력 : " +editText1.getText().toString() + "\n" + "장기 복용중인 약 : " +
                                editText2.getText().toString() + "\n" + "단기 복용중인 약 : " +
                                editText3.getText().toString() + "\n" + "알르레기 : " +
                                editText4.getText().toString();
        Log.d("FileContents", "Saving data: " + data); // 로그 추가
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = requireContext().openFileOutput("data.txt", Context.MODE_PRIVATE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            outputStreamWriter.write(data);
            outputStreamWriter.close();
            Toast.makeText(requireContext(), "데이터가 저장되었습니다.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateTextViews() {
        textView1.setText("병력 : " + editText1.getText().toString());
        //if(editText2.getText().toString() != null)
        textView2.setText("수술내역 : " + editText2.getText().toString());
        //if(editText3.getText().toString() != null)
        textView3.setText("복용 중인 약 : " + editText3.getText().toString());
        //if(editText4.getText().toString() != null)
        textView4.setText("알르레기 : " + editText4.getText().toString());
    }

    // 파일에서 데이터를 load 하는 함수
    public void loadDataFromFile() {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            FileInputStream fileInputStream = requireContext().openFileInput("data.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            bufferedReader.close();

            String[] data = stringBuilder.toString().split("\n");
            if (data.length >= 4) {
                textView1.setText(data[0]);
                textView2.setText(data[1]);
                textView3.setText(data[2]);
                textView4.setText(data[3]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 저장 버튼 및 불러오기 버튼에 대한 리스너를 등록하는 메서드
    private void setupButtonListeners() {
        Button saveButton = view.findViewById(R.id.button1);
        Button loadButton = view.findViewById(R.id.button2);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDataToFile();
            }
        });

        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDataFromFile();
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        loadDataFromFile(); // 저장된 데이터 불러오기
    }
    private void clearEditTexts() {
        editText1.setText("");
        editText2.setText("");
        editText3.setText("");
        editText4.setText("");
    }
    private void clearTextViews() {
        textView1.setText("");
        textView2.setText("");
        textView3.setText("");
        textView4.setText("");
    }

}

