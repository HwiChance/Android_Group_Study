# 14장 인텐트와 구글 기본 앱 연동
## 목록
[14.1.](#141-인텐트) 인텐트
  - [14.1.1.](#1411-인텐트의-기본-개념) 인텐트의 기본 개념
  - [14.1.2.](#1412-명시적-인텐트와-암시적-인텐트) 명시적 인텐트와 암시적 인텐트
  - [14.1.3.](#1413-인텐트-필터intent-filter) 인텐트 필터(Intent Filter)
  - [14.1.4.](#1414-extra-데이터) Extra 데이터
  - [14.1.5.](#1415-결과-되돌리기) 결과 되돌리기

[14.2.](#142-구글-기본-앱-연동) 구글 기본 앱 연동
  - [14.2.1.](#1421-주소록-앱) 주소록 앱
  - [14.2.2.](#1422-카메라-앱) 카메라 앱
  - [14.2.3.](#1423-갤러리-앱) 갤러리 앱
  - [14.2.4.](#1424-음성인식-앱) 음성인식 앱
  - [14.2.5.](#1425-기타-앱-연동) 기타 앱 연동

## 14.1. 인텐트
안드로이드 앱의 핵심 아키텍처는 컴포넌트 기반의 앱 수행이고 인텐트(Intent)는 이 개념을 완성해주는 클래스임
### 14.1.1. 인텐트의 기본 개념
- 안드로이드의 컴포넌트 구조
  - `Activity`: 화면 출력
  - `Service`: 백그라운드 작업
  - `Content Provider`: 앱 간의 데이터 공유
  - `Broadcast Receiver`: 이벤트 모델로 수행되며 브로드캐스트를 수신
- 컴포넌트들은 개발자가 작성한 Java Class이지만 일반적인 OOP프로그램의 Java Class들이 직접 결합되어 있는 것과 달리 독립적으로 실행되기 때문에 컴포넌트로 부름
  > 직접 결합의 의미: A라는 class에서 `new`연산자를 통해 B라는 class를 이용할 경우 두 class는 직접 결합되어 있다고 표현함
  >> 컴포넌트가 직접 결합되어 있지 않은 것은 유지보수와 다양한 앱 연동이 가능하다는 측면에서 장점이 있음


- 컴포넌트의 실행(사용)은 자바 코드에서 직접 실행하는 대신 안드로이드 시스템에 의뢰를 하는 방식으로 이루어짐
  > 컴포넌트 객체를 직접 생성해서 사용할 경우 컴포넌트로써의 기능을 잃게 됨

- 컴포넌트의 생명주기 역시 안드로이드 시스템이 관리
- 인텐트 - 컴포넌트를 실행하기 위해 시스템에 넘기는 정보
- 실행하고자 하는 컴포넌트 정보를 담은 인텐트를 구성해서 시스템에 넘기면 시스템에서 인텐트 정보를 분석해 맞는 컴포넌트를 실행해주는 구조

### 14.1.2. 명시적 인텐트와 암시적 인텐트
- 명시적 인텐트: 실행하고자 하는 컴포넌트의 클래스명을 담은 인텐트
  ```java
  Intent intent = new Intent(this, DetailActivity.class);
  startActivity(intent);
  ```
  - 주로 같은 앱의 컴포넌트를 실행할 때 사용
  - `startActivity()`: 시스템에 액티비티 실행을 의뢰하는 함수
- 암시적 인텐트: 클래스명을 알 수 없는 외부 앱의 컴포넌트를 실행할 때 사용하는 인텐트
  ```XML
  // AndroidManifest.xml
  <activity android:name=".DetailActivity">
    <intent-filter>
      <action android:name="com.example.ACTION_VIEW"/>
      <category android:name="android.intent.category.DEFAULT"/>
    </intent-filter>
  </activity>
  ```
  - 암시적 인텐트는 안드로이드 시스템이 인텐트 속에 담긴 Intent Filter 정보를 해석해서 컴포넌트를 실행하도록 함
  ```java
  Intent intent = new Intent();
  intent.setAction("com.example.ACTION_VIEW");
  startActivity(intent);
  ```
  - 클래스명을 담는 대신 Action 문자열이 `com.example.ACTION_VIEW`로 등록된 액티비티를 실행하라는 Action 정보를 담음
- 같은 앱 내의 컴포넌트를 실행할 때는 명시적 인텐트를 사용하는 것이 권장사항

### 14.1.3. 인텐트 필터(Intent Filter)
```XML
// AndroidManifest.xml
<activity android:name=".SomeActivity">
  <intent-filter>
    <action android:name="com.some.ACTION_VIEW"/>
    <category android:name="android.intent.category.DEFAULT"/>
    <data android:scheme="http"/>
  </intent-filter>
</activity>
```
- `<action>`
  - 컴포넌트가 어떤 능력을 갖추고 있는지에 대한 문자열
  - 개발자 임의의 단어 혹은 라이브러리 지정 문자열 가능
- `<category>`
  - 컴포넌트에 대한 추가 정보로 어느 범주의 컴포넌트인지를 표현하기 위해 사용
  - 개발자 임의의 단어도 가능하지만 대부분 라이브러리 지정 문자열을 사용함
- `<data>`
  - 컴포넌트를 실행하기 위해 필요한 데이터에 대한 상세 정보를 명시하기 위해 사용
  - data는 URL 형식으로 표현되어 `android:scheme`, `android:host`, `android:port` 등으로 선언함
- `<action>`은 한가지만, `<category>`는 여러가지가 선언될 수 있음
- [Docs](https://developer.android.com/reference/android/content/Intent#summary)
```java
Intent intent = new Intent();
intent.setAction("com.example.ACTION_VIEW");
startActivity(intent);
```
- startActivity 메소드는 자동으로 `android.intent.category.DEFAULT`를 추가함
- 따라서 암시적 인텐트로 실행될 컴포넌트는 다른 `<category>`가 설정되어 있더라도 `<intent-filter>`에 `<category android:name="android.intent.category.DEFAULT"/>`를 반드시 선언해야 함
```XML
// AndroidManifest.xml
<activity android:name=".SomeActivity">
  <intent-filter>
    <action android:name="com.some.ACTION_VIEW"/>
    <category android:name="android.intent.category.DEFAULT"/>
    <data android:scheme="geo"/>
  </intent-filter>
</activity>

// java
Intent intent = new Intent();
intent.setAction("com.some.ACTION_VIEW");
intent.setData(Uri.parse("geo:"));
startActivity(intent);
```
- `<intent-filter>`에 `<data>`가 선언된 경우 인텐트 정보에 data 정보를 맞춰 줘야 함
- data 정보는 URL 문자열로, Uri 객체로 표현됨

### 14.1.4. Extra 데이터
한 컴포넌트에서 다른 컴포넌트를 실행할 때 데이터를 전달하기 위해 인텐트에 담는 데이터
```java
Intent intent = new Intent(this, DetailActivity.class);
intent.putExtra("data1", "hello");
intent.putExtra("data2", 100);
startActivity(intent);
```
- `putExtra(key, value)`: key-value 형식으로 extra data를 인텐트에 담는 메소드
- extra data는 문자열, 숫자, boolean, 객체 등 모든 타입의 데이터를 넘길 수 있음
-
```java
Intent intent = getIntent();
String data1 = intent.getStringExtra("data1");
int data2 = intent.getIntExtra("data2", 0);
```
- 데이터를 넘겨 받는 쪽에선 `getIntent()` 메소드를 통해 인텐트 객체를 얻고, 그 안에 담긴 extra data를 `getXXXExtra()` 메소드를 통해 얻음
- 컴포넌트 객체를 직접 생성하지 않고 시스템을 통해 실행하기 때문에 이러한 방식을 사용
```java
// send
Intent intent = new Intent(this, DetailActivity.class);
Bundle bundle = new Bundle();
bundle.putString("data", "hello");
intent.putExtra("bundleData", bundle);
startActivity(intent);

// receive
Intent intent = getIntent();
Bundle bundle = intent.getBundleExtra("bundleData");
String data = bundle.getString("data");
```
- 데이터를 넘겨줄 때 `bundle`객체에 데이터를 담아서 넘겨주는 방법도 있음
- 인텐트가 택배 기사라면 `bundle`객체는 택배 물품
```java
@Override
protected void onCreate(Bundle savedInstanceState){
    //...
}
```
- `bundle`객체는 화면 전환 시에 사용되는 객체
- `savedInstanceState`: 화면 전환 시 이전 액티비티에서 저장되어야 할 값이 있는 경우 그 값을 저장하기 위한 객체
- `bundle Class`는 `Parcelable` 인터페이스를 구현한 Class임
- 참고: [Docs](https://developer.android.com/guide/components/activities/parcelables-and-bundles?hl=ko)
- 인텐트를 통해 객체를 전달할 때 해당 객체는 `Parcelable`이나 `Serializable` `implements`해야 함
- `Parcelable implements` [예시](https://developer88.tistory.com/64)
- `Serializable implements` [예시](https://developer88.tistory.com/9)

### 14.1.5. 결과 되돌리기
```java
Intent intent = new Intent(this, DetailActivity.class);
startActivityForResult(intent, 10);
```
- 한 액티비티에서 다른 액티비티로 넘어갔다 돌아와야 할 경우 `startActivity()` 메소드 대신 `startActivityForResult()` 메소드를 사용해야 함
- `startActivityForResult()`의 두번째 매개변수는 requestCode값으로 돌아왔을 때 어느 요청으로부터 되돌아온 것인지를 구분하기 위해 사용됨
- `requestCode`: 0 이상의 숫자
```java
Intent intent = getIntent();
intent.putExtra("rstData", "hello");
setResult(RESULT_OK, intent);
finish();
```
- `startActivityForResult()` 메소드로 실행된 액티비티에서 이전의 액티비티로 돌아가기 위해선 자신의 액티비티를 종료하면 되고 이때 `finish()` 메소드가 사용됨
- 이전의 액티비티로 돌아가면서 넘겨줄 데이터가 있다면 `putExtra()` 메소드를 사용하면 됨
- `setResult()` 메소드의 첫번째 매개변수는 resultCode로 처리 상태를 나타냄
  - `RESULT_OK`: 정상적으로 처리됨
  - `RESULT_CANCELED`: 처리가 취소됨
  - `RESULT_FIRST_USER`: 사용자 정의 처리 상태
```java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
  //..
}
```
- 이전의 액티비티로 돌아가게 되면 `onActivityResult()` 메소드가 자동으로 호출 됨
- `requestCode`: 어느 요청으로부터 되돌아온 것인지를 구분
- `resultCode`: 요청의 결과 상태
- `data`: 돌아오면서 넘겨 받은 인텐트

## 14.2. 구글 기본 앱 연동
### 14.2.1. 주소록
```java
Intent intent = new Intent(Intent.ACTION_PICK);
intent.setData(ContactsContract.Contacts.CONTENT_URI);
startActivityForResult(intent, 10);
```
- 주소록 목록 화면 띄우기
- `setData()`의 매개변수를 통해 주소록 화면에 출력되는 데이터를 조정할 수 있음
  - `ContactsContract.Contacts.CONTENT_URI`: 사람 이름만 출력
  - `ContactsContract.CommonDataKinds.Phone.CONTENT_URI`: 사람 이름과 전화번호 출력
  - `ContactsContract.CommonDataKinds.Email.CONTENT_URI`: 사람 이름과 이메일 출력
```java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
  if(requestCode == 10 && resultCode == RESULT_OK) {
    String result = data.getDataString();
    resultView.setText(result);
  }
}
```
- `getDataString()`: 주소록으로부터 넘어온 URL 값 획득
- URL 정보의 맨 마지막 단어는 선택된 연락처의 식별자
- 식벽자 값을 통해 다시 구체적인 전화번호나 이메일 등의 값을 가져와야 함(콘텐츠 프로바이더 부분에서 다루는 내용)
```java
Intent intent = new Intent(Intent.ACTION_VIEW);
intent.setData(Uri.parse(ContactsContract.Contacts.CONTENT_URI + "/" + 1066));
startActivity(intent);
```
- 한 사람에 대한 상세보기 화면 띄우기
- `setData()` 속 숫자는 주소록에서 띄우고 싶은 사람의 식별자 값

### 14.2.2. 카메라 앱
#### 섬네일로 결과 받기
```java
Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
startActivityForResult(intent, 10);

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
  if(requestCode == 10 && resultCode == RESULT_OK) {
    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
    resultImageView.setImageBitmap(bitmap);
  }
}
```
- 카메라 앱으로 사진 촬영 후 사진 데이터를 받아 화면에 출력
- 촬영된 사진이 파일로 저장되지 않고 사진 데이터 자체가 전달되는 구조
- 섬네일 이미지 데이터가 전달되기 때문에 사진이 굉장히 작음
#### 파일 공유 방법
개발자 앱에서 임의의 경로에 파일을 하나 만들고, 이 파일 경로를 카메라 앱에 전달하여 카메라 앱에서 사진 촬영 데이터를 파일에 쓴 다음, 성공 여부를 개발자 앱에서 확인하는 구조
```java
String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/myApp";
File dir = new File(dirPath);
if(!dir.exists()) {
 dir.mkdir();
}

filePath = File.createTempFile("IMG", ".jpg", dir);
if(!filePath.exists()) {
 filePath.createNewFile();
}
```
- 사진 데이터가 저장될 임의의 파일 생성
```java
intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(filePath));
```
- API Level 24 이전 버전까지 사용된 방식으로 인텐트에 파일 정보를 담아서 전달
- API Level 24 버전부터 앱 외부에 `file://` URI의 노출을 금지하는 Strict 모드 정책이 적용되어 파일 URI를 인텐트에 포함할 경우 `FileUriExposedException`이 발생함
- 앱 간에 파일을 공유하려면 `content://` URI를 보내고 이 URI에 대해 임시 액세스 권한을 부여해야 함
- 이 권한을 쉽게 부여하기 위해 `FileProvider` 클래스를 이용함
  - `FileProvider`: `Support 라이브러리 v4`에서 제공하는 콘텐츠 프로바이더
  - `XML`로 제공하는 설정을 기반으로 파일들에 대한 Content URI를 생성해 줌
    ```XML
    // ../res/xml 폴더
    <paths xmlns:android="http://schemas.android.con/apk/res/android">
      <external-path name="myfiles" path="."/>
    </paths>
    ```
    - `<external-path>`: 외부 저장 공간의 파일 공유를 위해 사용되는 태그
    - `<file-path>`: 내부 저장 공간의 파일 공유를 위해 사용되는 태그
  - `XML`파일을 `AndroidManifest.xml`에서 `FileProvider`를 등록하면서 설정해야 함
    ```XML
    <provider
      android:name="android.support.v4.content.FileProvider"
      android:authorities="com.example.test5_14.provider"
      android:exported="false"
      android:grantUriPermissions="true">
      <meta-data
        android:name="android.support.FILE_PROVIDER_PATHS"
        android:resource="@xml/provider_paths"/>
    </provider>
    ```
    - `<authorities>`: 유일성이 확보된 식별자 문자열을 하나 선언
    - `<meta-data>`: `XML` 파일 정보를 설정
  - 공유하려는 파일 정보의 Uri 값 획득
    ```java
    Uri photoURI = FileProvider.getUriForFile(Lab2Activity.this,
            BuildConfig.APPLICATION_ID + ".provider", filePath);
    ```
  - Uri 값을 인텐트의 extra data로 설정
    ```java
    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
    startActivityForResult(intent, 10);
    ```
#### 이미지 로딩으로 인한 OutOfMemoryException 문제
- 데이터의 크기가 큰 이미지를 화면 출력을 목적으로 로딩하면 `OutOfMemoryException`이 발생할 수 있음
- `BitmapFactory`: 이미지 크기를 줄이는 API
  ```java
  Bitmap bitmap = BitmapFactory.decodeFile(filePath.getAbsolutePath());
  ```
  - `decodeFile()`: 파일 경로를 주면 `FileInputStream`을 만들어서 `decodeStream` 이용하여 `Bitmap` 생성
  - `decodeByteArray()`: `byte[]` 배열로 `Bitmap` 생성
  - `decodeResource()`: `Resource` 폴더에 저장된 파일로 `Bitmap` 생성
  - `decodeStream()`: `InputStream`으로 `Bitmap` 생성
- `BitmapFactory` 옵션
  ```java
  BitmapFactory.Options imgOptions = new BitmapFactory.Options();
  imgOptions.inSampleSize = 10;
  Bitmap bitmap = BitmapFactory.decodeFile(filePath.getAbsolutePath(), imgOptions);
  ```
  - `inSampleSize`: 이미지 조정 값 (10 = 1/10으로 줄여서 로딩)
- 원본 이미지의 크기를 얻고 적절한 `inSampleSize` 계산하기
  ```java
  BitmapFactory.Options options = new BitmapFactory.Options();
  options.inJustDecodeBounds = true;
  try {
    InputStream in = new FileInpitStream(filePath);
    BitmapFactory.decodeStream(in, null, options);
    in.close();
    in = null;
  } catch (Exception e) {
    e.printStackTrace();
  }

  final int height = options.outHeight;
  final int width = options.outWidth;

  if(height > reqHeight || widht > reqWidth) {
    final int heightRatio = Math.round((float) height / (float) reqHeight);
    final int widthRatio = Math.round((float) width / (float) reqWidth);
    inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
  }
  ```
#### 동영상 촬영
```java
Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0); // 화질: 0-low, 1-high
intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 20); // 촬영 시간 제한
intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 1024 * 1024 * 10); // 촬영 데이터 크기 제한
intent.putExtra(MediaStore.EXTRA_OUPUT, videoURI); // FileProvider 이용
startActivityForResult(intent, 10);
```

### 14.2.3. 갤러리 앱
```java
Intent intent = new Intent(Intent.ACTION_PICK);
intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
startActivityForResult(intent, 10);

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
  if(requestCode == 10 && resultCode == RESULT_OK) {
    String result = data.getDataString();
    resultView.setText(result);
  }
}
```
- 갤러리 앱의 목록 액티비티를 띄우고 선택된 사진의 식별자 정보를 URL 형식으로 획득
- 식별자는 ContentProvide에서 사진 데이터를 얻는데 사용됨
```java
Intent intent = new Intent();
intent.setAction(Intent.ACTION_VIEW);
Uri photoURI = FileProvider.getUriForFile(Lab2Activity.this, BuildConfig.APPLICATION_ID + ".provider", filePath);
intent.setDataAndType(photoURI, "image/*");
intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
startActivity(intent);
```
- 갤러리 앱을 통해 이미지 전체화면으로 보기
- 파일 정보를 갤러리 앱에 전달해야 하므로 `FileProvider`가 사용됨
- 외부 앱에서 우리의 데이터를 이용하는 것이므로 권한 정보도 추가되어야 함

### 14.2.4. 음성인식 앱
```java
Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "음성인식 테스트");
startActivityForResult(intent, 10);
```
- `EXTRA_LANGUAGE_MODEL`: 음성인식의 언어 모델을 지정하기 위한 데이터
- `LANGUAGE_MODEL_FREE_FORM`: 일반적인 음성에 대한 문자열 변환 서비스
- `EXTRA_LANGUAGE`를 이용하여 언어를 선택할 수 있고 만약 값을 지정하지 하지 않으면 스마트폰에 설정된 기본 언어를 따름
- `EXTRA_PROMPT`: 음성인식 다이얼로그의 타이틀 설정
```java
ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
String result = results.get(0);
```

### 14.2.5. 기타 앱 연동
#### 지도 앱 연동
```java
Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:37.5662952, 126.9779451"));
startActivity(intent);
```
- 위도, 경도 값으로 지도 앱 화면의 가운데 설정
#### 브라우저 앱
```java
Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.seoul.go.kr"));
startActivity(intent);
```
#### 전화 앱
- 권한 설정
  ```XML
  <uses-permission android:name="android.permission.CALL_PHONE"/>
  ```
- 전화 걸기
  ```java
  Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:02-120"));
  startActivity(intent);
  ```
#### 액티비티 존재 여부 확인
```java
PackageManager pm = getPackageManager();
List<ResolveInfo> activities = pm.queryIntentActivities(intent, 0);
if(activities.size() != 0) {
  // 있을 때
} else {
  // 없을 때
}
```
- 연동될 외부 앱이 존재하는지 여부를 미리 확인하여 에러 발생을 방지
