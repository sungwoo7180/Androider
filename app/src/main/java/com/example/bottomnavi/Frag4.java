package com.example.bottomnavi;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;
import androidx.appcompat.app.AlertDialog;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;

//implements MapView.CurrentLocationEventListener, MapView.MapViewEventListener

public class Frag4 extends Fragment  {
    private static final int ACCESS_FINE_LOCATION = 1000; // Request Code
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private MapView mapView;
    private ViewGroup mapViewContainer;
    private MapPOIItem customMarker;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private TextView locationTextView;
    // SMS 전송 여부를 확인하는 불리언 변수
    private boolean isSmsSent = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag4, container, false);

        // TextView 초기화
        locationTextView = rootView.findViewById(R.id.locationTextView);

        // 위치 권한이 허용되었는지 체크
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // 위치 업데이트를 요청할 LocationManager 객체 생성
            locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
            // LocationListener 객체 생성
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    // 위치가 변경될 때마다 호출되는 메서드
                    // 여기서 지도 위 마커 위치를 업데이트하고 움직이는 로직을 구현할 수 있습니다.
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    // 소수점 5자리까지만 저장
                    String formattedLatitude = String.format(Locale.US, "%.5f", latitude);
                    String formattedLongitude = String.format(Locale.US, "%.5f", longitude);
                    // 지도 위의 마커를 이동시키는 로직 구현
                    // 마커 위치 업데이트
                    MapPoint MARKER_POINT = MapPoint.mapPointWithGeoCoord(latitude, longitude);
                    if (customMarker != null) {
                        customMarker.setMapPoint(MARKER_POINT);
                    }
                    // TextView 에 경도, 위도, 그리고 주소 정보 표시
                    locationTextView.setText("위도: " + formattedLatitude + "\n경도: " + formattedLongitude);

                    // Geocoder 를 사용하여 주소 정보 가져오기
                    Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                        if (addresses != null && addresses.size() > 0) {
                            Address address = addresses.get(0);
                            String addressText = address.getAddressLine(0);
                            locationTextView.append("\n주소: " + addressText);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {}

                @Override
                public void onProviderEnabled(String provider) {}

                @Override
                public void onProviderDisabled(String provider) {}
            };
            // 위치 업데이트 요청
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }

        try {
            PackageInfo info = requireActivity().getPackageManager().getPackageInfo(requireActivity().getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("키해시는 :", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        // 권한 ID 를 가져옵니다
        int permission = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.INTERNET);
        int permission2 = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
        int permission3 = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION);

        // 권한이 열려있는지 확인
        if (permission == PackageManager.PERMISSION_DENIED || permission2 == PackageManager.PERMISSION_DENIED || permission3 == PackageManager.PERMISSION_DENIED) {
            // 마쉬멜로우 이상 버전부터 권한을 물어본다
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 권한 체크(READ_PHONE_STATE 의 requestCode 를 1000으로 세팅)
                requestPermissions(new String[]{Manifest.permission.INTERNET, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
            }
            return rootView;
        }
        //지도를 띄우자
        // java code
        mapView = new MapView(requireActivity());
        mapViewContainer = rootView.findViewById(R.id.map_View);
        mapViewContainer.addView(mapView);


        /* 오류 발생해서 주석처리
        mapView.setMapViewEventListener(this);
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
        */

        /* 마커를 표시하자 */
        MapPOIItem customMarker = new MapPOIItem();
        MapPoint mapPoint= MapPoint.mapPointWithGeoCoord(37.480426, 126.900177); //마커 표시할 위도경도
        customMarker.setItemName("우리집 근처당");
        customMarker.setTag(1);
        customMarker.setMapPoint(mapPoint);
        customMarker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.
        customMarker.setCustomImageResourceId(R.drawable.custom_marker_red); // 마커 이미지.
        customMarker.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
        customMarker.setCustomImageAnchor(0.5f, 1.0f); // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.
        mapView.addPOIItem(customMarker);

        // 내 위치 버튼 클릭 이벤트 처리
        Button btnStart = rootView.findViewById(R.id.btn_start);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    LocationManager locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
                    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        showMyLocation(latitude, longitude);
                    }
                } else {
                    // 권한이 없을 경우에 대한 처리 (생략)
                }
            }
        });
        // "문자 전송" 버튼 찾기
        Button btnSend = rootView.findViewById(R.id.btn_send);
        // "내 위치" 버튼에 클릭 리스너 설정
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSmsSent) {
                    // "locationTextView"의 내용 가져오기
                    String locationText = locationTextView.getText().toString();
                    locationText = locationText + "\n[위치 전송 앱 사용]";

                    // 문자 메시지를 보내기 위한 암시적 인텐트 생성
                    Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
                    sendIntent.setData(Uri.parse("smsto:" + Uri.encode("119")));
                    sendIntent.putExtra("sms_body", locationText);

                    // SMS 인텐트를 처리할 앱이 있는지 확인
                    if (sendIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
                        // SMS 인텐트 시작
                        startActivity(sendIntent);

                        // SMS 가 전송되었음을 표시하기 위해 isSmsSent 를 true 로 설정
                        isSmsSent = true;
                    } else {
                        // SMS 인텐트를 처리할 앱이 없으면 사용자에게 안내 메시지 표시
                        Toast.makeText(requireContext(), "문자 메시지를 처리할 앱이 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // SMS가 이미 전송되었음을 사용자에게 안내
                    Toast.makeText(requireContext(), "이미 문자 메시지를 전송했습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // 추적중지 버튼 클릭 이벤트 처리 고민중
        /* Button btnStop = rootView.findViewById(R.id.btn_stop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopShowingLocation();
            }
        });*/
        return rootView;

    }

    // 권한 체크 이후로직
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // READ_PHONE_STATE 의 권한 체크 결과를 불러온다
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            boolean check_result = true;

            // 모든 퍼미션을 허용했는지 체크
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }

            // 권한 체크에 동의를 하지 않으면 안드로이드 종료
            if (!check_result) {
                requireActivity().finish();
            }
        }
    }

    // 내 위치를 지도 위에 표시하는 메서드
    private void showMyLocation(double latitude, double longitude) {
        if (mapView != null) {
            MapPoint MARKER_POINT = MapPoint.mapPointWithGeoCoord(latitude, longitude);
            if (customMarker == null) {
                customMarker = new MapPOIItem();
                customMarker.setItemName("내 위치");
                customMarker.setTag(1);
                customMarker.setMapPoint(MARKER_POINT);
                customMarker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                customMarker.setCustomImageResourceId(R.drawable.custom_marker_red);
                customMarker.setCustomImageAutoscale(false);
                customMarker.setCustomImageAnchor(0.5f, 1.0f);
                mapView.addPOIItem(customMarker);
            } else {
                customMarker.setMapPoint(MARKER_POINT);
            }

            mapView.setMapCenterPoint(MARKER_POINT, true);
        }
    }
    // 마커 표시를 중지하는 메서드
    private void stopShowingLocation() {
        if (mapView != null && customMarker != null) {
            mapView.removePOIItem(customMarker);
            customMarker = null;
        }
    }




}
