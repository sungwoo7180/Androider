package com.example.bottomnavi;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.net.Uri;
import android.os.Bundle;
//import android.telecom.Call;
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

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;
import androidx.appcompat.app.AlertDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
//import com.google.android.gms.common.api.Response;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;



//import javax.security.auth.callback.Callback;

//implements MapView.CurrentLocationEventListener, MapView.MapViewEventListener

public class Frag2 extends Fragment implements MapView.CurrentLocationEventListener {
    private static final String BASE_URL = "http://apis.data.go.kr/B552657/AEDInfoInqireService/getAedLcinfoInqire";
    private static final String SERVICE_KEY = "dsgAnv21hAD0I2zmlB0pu8nJRXkMbhHqHsh3BeNjcUkA21zMCvIBnm8EugArNwVwYD0/IcSNjsAYpO9gyS+EdA==";
    private static final String BASE_URL2 = "http://apis.data.go.kr/B552657/ErmctInfoInqireService/getEgytBassInfoInqire";  //5번

    //http://apis.data.go.kr/B552657/ErmctInfoInqireService/getEgytLcinfoInqire : 4번 응급의료기관 기본정보 조회
    //http://apis.data.go.kr/B552657/ErmctInfoInqireService/getEgytBassInfoInqire : 5번 응급의료기관 기본정보 조회
    private static final int ACCESS_FINE_LOCATION = 1000; // Request Code
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private List<AedLocation> aedLocations = new ArrayList<>();
    private List<AedLocation> emergencyLocations = new ArrayList<>();
    private MapView mapView;
    private ViewGroup mapViewContainer;
    private MapPOIItem customMarker;
    private MapPOIItem customMarker1;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private TextView locationTextView;
    private double currentUserLatitude;
    private double currentUserLongitude;
    private static final double MAX_DISTANCE = 3000000;
    // SMS 전송 여부를 확인하는 불리언 변수
    private boolean isSmsSent = false;
    private boolean shouldAskForDirections = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag2, container, false);

        // TextView 초기화
        locationTextView = rootView.findViewById(R.id.locationTextView);
        locationTextView.bringToFront();

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
                    currentUserLatitude = latitude;
                    currentUserLongitude = longitude;

                    // 소수점 5자리까지만 저장
                    String formattedLatitude = String.format(Locale.US, "%.5f", latitude);
                    String formattedLongitude = String.format(Locale.US, "%.5f", longitude);
                    // 지도 위의 마커를 이동시키는 로직 구현
                    // 마커 위치 업데이트
                    MapPoint MARKER_POINT = MapPoint.mapPointWithGeoCoord(latitude, longitude);
                    if (customMarker != null) {
                        customMarker.setMapPoint(MARKER_POINT);
                    }else {
                        // customMarker가 null일 때는 처음 위치를 가리키는 마커를 추가합니다.
                        showMyLocation(latitude, longitude);
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
            // 최초 위치 표시
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
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


        /*오류 발생해서 주석처리
        mapView.setMapViewEventListener(this);
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading); */


        /* 마커를 표시하자
        MapPOIItem customMarker1 = new MapPOIItem();
        MapPoint mapPoint= MapPoint.mapPointWithGeoCoord(37.480426, 126.900177); //마커 표시할 위도경도
        customMarker1.setItemName("우리집 근처당");
        customMarker1.setTag(1);
        customMarker1.setMapPoint(mapPoint);
        customMarker1.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.
        customMarker1.setCustomImageResourceId(R.drawable.custom_marker_red); // 마커 이미지.
        customMarker1.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
        customMarker1.setCustomImageAnchor(0.5f, 1.0f); // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.
        mapView.addPOIItem(customMarker1); */

        // AED 버튼 클릭 이벤트 처리
        Button btnStart = rootView.findViewById(R.id.btn_start);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aedLocations.clear(); // AED 위치 초기화
                stopShowingLocation(); //모든 마커 제거
                if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    LocationManager locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
                    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        showMyLocation(latitude, longitude);
                        getAedLocations(latitude, longitude);
                    }
                } else {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
                }
            }
        });
        // 응급실 버튼 클릭 이벤트 처리
        Button btnEmergency = rootView.findViewById(R.id.btn_emergency);
        btnEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emergencyLocations.clear();
                stopShowingLocation(); //모든 마커 제거
                if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    LocationManager locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
                    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        showMyLocation(latitude, longitude);
                        getEmergencyLocations(latitude, longitude);
                    }
                } else {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
                }
            }
        });
        return rootView;

    }

    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint currentLocation, float accuracyInMeters) {
        // 이곳에 위치 업데이트 이벤트 처리 코드를 작성하세요.
    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float headingAngle) {
        // 이곳에 장치 헤딩 업데이트 이벤트 처리 코드를 작성하세요.
    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {
        // 이곳에 위치 업데이트 취소 이벤트 처리 코드를 작성하세요.
    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {
        // 이곳에 위치 업데이트 실패 이벤트 처리 코드를 작성하세요.
    }
    // 권한 체크 이후로직
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // READ_PHONE_STATE 의 권한 체크 결과를 불러온다
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            boolean allPermissionsGranted = true;

            // 모든 퍼미션을 허용했는지 체크
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }

            // 권한 체크에 동의를 하지 않으면 안드로이드 종료
            if (!allPermissionsGranted) {
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

            mapView.setMapCenterPointAndZoomLevel(MARKER_POINT, 2, true); //zoom level 2
        }
    }
    // 마커 표시를 중지하는 메서드
    private void stopShowingLocation() {
        if (mapView != null && customMarker != null) {
            mapView.removePOIItem(customMarker);
            customMarker = null;
            mapView.removeAllPOIItems(); // 모든 마커 제거
        }
    }
    private void getAedLocations(double myLatitude, double myLongitude) {
        OkHttpClient client = new OkHttpClient();

        // URL 생성
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL).newBuilder();
        urlBuilder.addQueryParameter("ServiceKey", SERVICE_KEY);
        urlBuilder.addQueryParameter("WGS84_LON", String.valueOf(myLongitude));
        urlBuilder.addQueryParameter("WGS84_LAT", String.valueOf(myLatitude));
        urlBuilder.addQueryParameter("pageNo", "1");
        urlBuilder.addQueryParameter("numOfRows", "300");
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();
        //Toast.makeText(requireContext(), url , Toast.LENGTH_SHORT).show();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), "네트워크 요청 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }


            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    Log.i("Response", responseBody); // 이 부분을 추가하여 응답 내용을 로그로 확인합니다
                    int len = responseBody.length();
                    final int MAX_LEN = 4000; // 2000 bytes 마다 끊어서 출력
                    if(len > MAX_LEN) {
                        int idx = 0, nextIdx = 0;
                        while(idx < len) {
                            nextIdx += MAX_LEN;
                            Log.d(TAG, responseBody.substring(idx, nextIdx > len ? len : nextIdx));
                            idx = nextIdx;
                        }
                    } else {
                        Log.d("Response", responseBody);
                    }
                    Log.d("Response", url); // 이 부분을 추가하여 응답 내용을 로그로 확인합니다

                    try {
                        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                        XmlPullParser parser = factory.newPullParser();
                        parser.setInput(new StringReader(responseBody));

                        String latitudeString = null;
                        String longitudeString = null;
                        String name = null;
                        int eventType = parser.getEventType();
                        while (eventType != XmlPullParser.END_DOCUMENT) {
                            if (eventType == XmlPullParser.START_TAG) {
                                String tagName = parser.getName();
                                if (tagName.equals("wgs84Lat")) {
                                    latitudeString = parser.nextText();
                                    Log.d("Response 경도:",latitudeString);
                                } else if (tagName.equals("wgs84Lon")) {
                                    longitudeString = parser.nextText();
                                    Log.d("Response 위도:",longitudeString);
                                } else if (tagName.equals("org")) {
                                    name = parser.nextText();
                                    Log.d("Response 이름:",name);
                                }

                            } else if (eventType == XmlPullParser.END_TAG) {
                                if (parser.getName().equals("item")) {
                                    if (latitudeString != null && longitudeString != null && name != null) {
                                        AedLocation aedLocation = new AedLocation(parseDoubleSafely(latitudeString), parseDoubleSafely(longitudeString), name);
                                        aedLocations.add(aedLocation);
                                    }
                                    latitudeString = null;
                                    longitudeString = null;
                                    name = null;
                                }
                            }

                            eventType = parser.next();
                        }
                    } catch (XmlPullParserException | IOException e) {
                        e.printStackTrace();
                    }
                    requireActivity().runOnUiThread(() -> {
                        showAedLocationsOnMap();
                    });
                } else {
                    // 응답이 실패한 경우에 대한 처리
                    requireActivity().runOnUiThread(() -> {

                        Toast.makeText(requireContext(), "Response error: " + response.code(), Toast.LENGTH_SHORT).show();
                    });
                }
            }

        });
    }
    //응급실
    private void getEmergencyLocations(double myLatitude, double myLongitude) {
        OkHttpClient client = new OkHttpClient();

        // URL 생성
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL2).newBuilder();
        urlBuilder.addQueryParameter("ServiceKey", SERVICE_KEY);
        urlBuilder.addQueryParameter("WGS84_LON", String.valueOf(myLongitude));
        urlBuilder.addQueryParameter("WGS84_LAT", String.valueOf(myLatitude));
        urlBuilder.addQueryParameter("pageNo", "1");
        urlBuilder.addQueryParameter("numOfRows", "10");
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();
        //Toast.makeText(requireContext(), url , Toast.LENGTH_SHORT).show();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), "네트워크 요청 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
            //응급실 반응
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    Log.i("Response", responseBody); // 이 부분을 추가하여 응답 내용을 로그로 확인합니다
                    int len = responseBody.length();
                    final int MAX_LEN = 4000; // 2000 bytes 마다 끊어서 출력
                    if(len > MAX_LEN) {
                        int idx = 0, nextIdx = 0;
                        while(idx < len) {
                            nextIdx += MAX_LEN;
                            Log.d(TAG, responseBody.substring(idx, nextIdx > len ? len : nextIdx));
                            idx = nextIdx;
                        }
                    } else {
                        Log.d("Response", responseBody);
                    }
                    Log.d("Response", url); // 이 부분을 추가하여 응답 내용을 로그로 확인합니다

                    try {
                        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                        XmlPullParser parser = factory.newPullParser();
                        parser.setInput(new StringReader(responseBody));

                        String latitudeString1 = null;
                        String longitudeString1 = null;
                        String name1 = null;
                        int eventType = parser.getEventType();
                        while (eventType != XmlPullParser.END_DOCUMENT) {
                            if (eventType == XmlPullParser.START_TAG) {
                                String tagName = parser.getName();
                                if (tagName.equals("wgs84Lon")) {                                   //4번 longitude 5번 wgs84Lon
                                    latitudeString1 = parser.nextText();
                                    Log.d("Response 응급실 파서 경도:",latitudeString1);
                                } else if (tagName.equals("wgs84Lat")) {                            //4번 latitude  5번 wgs84Lat
                                    longitudeString1 = parser.nextText();
                                    Log.d("Response 응급실 파서 위도:",longitudeString1);
                                } else if (tagName.equals("dutyName")) {
                                    name1 = parser.nextText();
                                    Log.d("Response 응급실 파서 이름:",name1);
                                }
                            } else if (eventType == XmlPullParser.END_TAG) {
                                if (parser.getName().equals("item")) {
                                    if (latitudeString1 != null && longitudeString1 != null && name1 != null) {
                                        AedLocation emergencyLocation = new AedLocation(parseDoubleSafely(latitudeString1), parseDoubleSafely(longitudeString1), name1);
                                        Log.d("Emergency Location", "Name: " + name1 +
                                                ", Latitude: " +  latitudeString1 +
                                                ", Longitude: " + longitudeString1);
                                        Log.d("Emergency Location", "hi:");
                                        emergencyLocations.add(emergencyLocation);
                                    }
                                    Log.d("Emergency Location", "hello");
                                    latitudeString1 = null;
                                    longitudeString1 = null;
                                    name1 = null;
                                }
                            }
                            eventType = parser.next();
                        }
                    } catch (XmlPullParserException | IOException e) {
                        Log.e("Parsing Error", "Error parsing latitude or longitude: " + e.getMessage());
                        e.printStackTrace();
                    }
                    // 응급실 위치 정보 파싱 후 로그를 통해 확인
                    for (AedLocation location : emergencyLocations) {
                        Log.d("Emergency Location", "Name: " + location.getName() +
                                ", Latitude: " + location.getLatitude() +
                                ", Longitude: " + location.getLongitude());
                    }

                    requireActivity().runOnUiThread(() -> {
                        showEmergencyLocationsOnMap(); // 응급실 위치를 지도에 표시하는 메서드 호출
                    });
                } else {
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(requireContext(), "Response error: " + response.code(), Toast.LENGTH_SHORT).show();
                    });
                }
            }

        });
    }

    private double parseDoubleSafely(String stringValue) {
        if (stringValue != null) {
            try {
                return Double.parseDouble(stringValue.trim());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return 0.0; // 기본값 설정 또는 다른 처리
    }

    private void showAedLocation(double latitude, double longitude, String name) {
        if (mapViewContainer != null) {
            MapPOIItem aedMarker = new MapPOIItem();
            MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(latitude, longitude);
            aedMarker.setItemName(name);  // 마커 이름 설정
            aedMarker.setTag(2);          // 태그 설정 (고유한 값)
            aedMarker.setMapPoint(mapPoint);
            aedMarker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.
            aedMarker.setCustomImageAutoscale(false); // 자동 크기 조정 비활성화
            int markerWidth = 50; // 원하는 너비 값 (픽셀)
            int markerHeight = 50; // 원하는 높이 값 (픽셀)
            aedMarker.setCustomImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.custom_marker_aed), markerWidth, markerHeight, false));
            aedMarker.setCustomImageResourceId(R.drawable.custom_marker_aed); // 마커 이미지.       //custom_marker_red1 or aedmarker6
            aedMarker.setCustomImageAnchor(0.5f, 0.5f);
            mapView.addPOIItem(aedMarker); // 마커 추가
            Log.d("AED Marker", "response: " + name); // 이 로그 추가
        }

    }
    private void showAedLocationsOnMap() {
        for (AedLocation location : aedLocations) {
            showAedLocation(location.getLatitude(), location.getLongitude(), location.getName());
            Log.d("Emergency Location", "Name: " + location.getName() +
                    ", Latitude: " + location.getLatitude() +
                    ", Longitude: " + location.getLongitude());
        }
        mapView.setPOIItemEventListener(new MapView.POIItemEventListener() {
            @Override
            public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
                // 위치 마커를 클릭했을 때 대화상자를 보여준다.
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("길찾기")
                        .setMessage("선택한 위치로 길찾기를 하시겠습니까?")
                        .setPositiveButton("길찾기", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                double destinationLatitude = mapPOIItem.getMapPoint().getMapPointGeoCoord().latitude;
                                double destinationLongitude = mapPOIItem.getMapPoint().getMapPointGeoCoord().longitude;

                                // 카카오맵 URL 형식을 사용하여 길찾기 화면으로 이동
                                String kakaoMapUrl = "kakaomap://route?sp=" + currentUserLatitude + "," + currentUserLongitude +
                                        "&ep=" + destinationLatitude + "," + destinationLongitude +
                                        "&by=FOOT";
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(kakaoMapUrl));
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("취소", null)
                        .show();
            }

            @Override
            public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {}

            @Override
            public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {}

            @Override
            public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {}
        });
    }
    private void showEmergencyLocation(double latitude, double longitude, String name) {
        if (mapViewContainer != null) {

            Log.d("Emergency Location3", "Name: " + name +
                    ", Latitude: " + latitude +
                    ", Longitude: " + longitude);
            MapPOIItem emergencyMarker = new MapPOIItem();
            MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(latitude, longitude);
            emergencyMarker.setItemName(name);  // 마커 이름 설정
            emergencyMarker.setTag(3);          // 태그 설정 (고유한 값)
            emergencyMarker.setMapPoint(mapPoint);
            emergencyMarker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.
            emergencyMarker.setCustomImageResourceId(R.drawable.custom_marker_aed); // 마커 이미지 (다른 색상으로 표시)
            emergencyMarker.setCustomImageAnchor(0.5f, 0.5f);
            mapView.addPOIItem(emergencyMarker); // 마커 추가
            mapView.invalidate();
            //Log.d("Emergency Marker", "response 이름 : " + name); // 이 로그 추가
            //Log.d("Emergency Marker", "response 위도 : " + latitude); // 이 로그 추가
            //Log.d("Emergency Marker", "response: 경도 :" + longitude); // 이 로그 추가


            //동강병원
            MapPOIItem customMarker1 = new MapPOIItem();
            MapPoint mapPoint1= MapPoint.mapPointWithGeoCoord(35.55346089, 129.30153777); //마커 표시할 위도경도
            customMarker1.setItemName("동강병원");
            customMarker1.setTag(4);
            customMarker1.setMapPoint(mapPoint1);
            customMarker1.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.
            customMarker1.setCustomImageResourceId(R.drawable.custom_marker_red); // 마커 이미지.
            customMarker1.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
            customMarker1.setCustomImageAnchor(0.5f, 1.0f); // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.
            mapView.addPOIItem(customMarker1);
            //삼정병원
            MapPOIItem customMarker2 = new MapPOIItem();
            MapPoint mapPoint2= MapPoint.mapPointWithGeoCoord(35.54848987, 129.2709871); //마커 표시할 위도경도
            customMarker2.setItemName("삼정병원");
            customMarker2.setTag(5);
            customMarker2.setMapPoint(mapPoint2);
            customMarker2.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.
            customMarker2.setCustomImageResourceId(R.drawable.custom_marker_red); // 마커 이미지.
            customMarker2.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
            customMarker2.setCustomImageAnchor(0.5f, 1.0f); // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.
            mapView.addPOIItem(customMarker2);


        }
    }
    private void showEmergencyLocationsOnMap() {
        Log.d("Emergency Location", "Name");
        for (AedLocation location : emergencyLocations) {
            showEmergencyLocation(location.getLatitude(), location.getLongitude(), location.getName());
            Log.d("Emergency Location2", "Name: " + location.getName() +
                    ", Latitude: " + location.getLatitude() +
                    ", Longitude: " + location.getLongitude());
        }
        mapView.setPOIItemEventListener(new MapView.POIItemEventListener() {
            @Override
            public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
                // 위치 마커를 클릭했을 때 대화상자를 보여준다.
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("길찾기")
                        .setMessage("선택한 위치로 길찾기를 하시겠습니까?")
                        .setPositiveButton("길찾기", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                double destinationLatitude = mapPOIItem.getMapPoint().getMapPointGeoCoord().latitude;
                                double destinationLongitude = mapPOIItem.getMapPoint().getMapPointGeoCoord().longitude;

                                // 카카오맵 URL 형식을 사용하여 길찾기 화면으로 이동
                                String kakaoMapUrl = "kakaomap://route?sp=" + currentUserLatitude + "," + currentUserLongitude +
                                        "&ep=" + destinationLatitude + "," + destinationLongitude +
                                        "&by=FOOT";
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(kakaoMapUrl));
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("취소", null)
                        .show();
            }

            @Override
            public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {}

            @Override
            public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {}

            @Override
            public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {}
        });

    }

}