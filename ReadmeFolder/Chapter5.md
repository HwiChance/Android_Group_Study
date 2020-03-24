# 5장  다양한 사용자 알림 효과
## 목록
[5.1.](https://github.com/HwiChance/Android_Group_Study/blob/master/ReadmeFolder/Chapter5.md#51-%EC%A7%84%EB%8F%99%EA%B3%BC-%EC%86%8C%EB%A6%AC) 진동과 소리  
  - [5.1.1.](https://github.com/HwiChance/Android_Group_Study/blob/master/ReadmeFolder/Chapter5.md#511-%EC%A7%84%EB%8F%99-%EC%9A%B8%EB%A6%AC%EA%B8%B0) 진동 울리기  
  - [5.1.2.](https://github.com/HwiChance/Android_Group_Study/blob/master/ReadmeFolder/Chapter5.md#512-%EC%86%8C%EB%A6%AC-%EC%9A%B8%EB%A6%AC%EA%B8%B0) 소리 울리기  

[5.2.](https://github.com/HwiChance/Android_Group_Study/blob/master/ReadmeFolder/Chapter5.md#52-%EB%8B%A4%EC%9D%B4%EC%96%BC%EB%A1%9C%EA%B7%B8) 다이얼로그
  - [5.2.1.](https://github.com/HwiChance/Android_Group_Study/blob/master/ReadmeFolder/Chapter5.md#521-%ED%86%A0%EC%8A%A4%ED%8A%B8toast) 토스트(Toast)
  - [5.2.2.](https://github.com/HwiChance/Android_Group_Study/blob/master/ReadmeFolder/Chapter5.md#522-%EC%95%8C%EB%A6%BC-%EC%B0%BDalertdialog) 알림창(AlertDialog)
  - [5.2.3.](https://github.com/HwiChance/Android_Group_Study/blob/master/ReadmeFolder/Chapter5.md#523-%EB%AA%A9%EB%A1%9Dalertdialog) 목록(AlertDialog)
  - [5.2.4.](https://github.com/HwiChance/Android_Group_Study/blob/master/ReadmeFolder/Chapter5.md#524-%EB%82%A0%EC%A7%9C-%EC%84%A0%ED%83%9Ddatepickerdialog) 날짜 선택(DatePickerDialog)
  - [5.2.5.](https://github.com/HwiChance/Android_Group_Study/blob/master/ReadmeFolder/Chapter5.md#525-%EC%8B%9C%EA%B0%84-%EC%84%A0%ED%83%9Dtimepickerdialog) 시간 선택(TimePickerDialog)
  - [5.2.6.](https://github.com/HwiChance/Android_Group_Study/blob/master/ReadmeFolder/Chapter5.md#526-%EC%BB%A4%EC%8A%A4%ED%85%80-%EB%8B%A4%EC%9D%B4%EC%96%BC%EB%A1%9C%EA%B7%B8alertdialog) 커스텀 다이얼로그(AlertDialog)

## 5.1. 진동과 소리
### 5.1.1. 진동 울리기
- 진동을 위해 `Vibrator`라는 `SystemService`가 제공됨
  > *SystemService* - 시스템 레벨에서 제공하는 특수 목적의 서비스

- `Permission` 설정
  ``` Java
  // AndroidManifest.xml
  <user-permission android:name="android.permission.VIBRATE"/>
  ```
  > *AndroidManifest.xml* - 앱의 메인 환경 파일

- 진동 울리기
  ``` Java
  Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
  vib.vibrate(1000);
  vib.vibrate(new long[]{500, 1000, 500, 1000}, -1);
  ```
  - 시스템 서비스 오브젝트 획득
    ``` Java
    public abstract Object getSystemService (String ServiceName)
    ```
  - 일정 시간 동안 진동 울리기
    ```Java
    vibrate(long milliseconds)
    ```
  - 일정 패턴으로 진동 울리기
    ```Java
    vibrate(long[] pattern, int repeat)
    ```
    > `pattern` - 홀수 번째 값은 대기시간, 짝수 번째 값은 진동시간 (단위: *millisecond*)
      `repeat` - 패턴 반복을 시작할 패턴 인덱스(취소할 때까지 무한 반복), -1을 주면 패턴 한 번 반복


### 5.1.2. 소리 울리기
#### 시스템 효과음
- 안드로이드 스마트폰에 내장된 기본 효과음
  ``` Java
  Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
  Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), notification);
  ringtone.play();
  ```
  - 기본 효과음의 Uri 식별자 획득
    ```Java
    public static Uri getDefaultUri(int type)
    ```
  - Uri 식별자로 효과음 객체 획득
    ```Java
    public static Ringtone getRingtone (Context context, Uri ringtoneUri)
    ```
#### 개발자 임의의 효과음
- 직접 녹음한 효과음
- `../res/raw` 폴더에 효과음 리소스를 저장
- `MediaPlayer`를 통해 재생
  ```Java
  MediaPlayer player = MediaPlayer.create(this, R.raw.fallbackring);
  player.play();
  ```
  > *MediaPlayer* - 오디오/비디오 파일 및 스트림의 재생을 제어하는데 사용되는 클래스


## 5.2. 다이얼로그
다양한 상황을 알리기 위해 사용하는 팝업 형태의 창. 애플리케이션에서 다이얼로그는 모달과 모달리스로 구분함
> 모달(*modal*) - 다이얼로그가 프로그램 제어권을 독점하여 다이얼로그를 닫기 전까지 원래의 창을 사용할 수 없음

>  모달리스(*moodeless*) - 다이얼로그가 프로그램 제어권을 독점하지 않아 다이얼로그가 떠 있더라도 다른 작업을 할 수 있음

### 5.2.1. 토스트(Toast)
- 화면 하단에 검정 바탕의 흰색 글이 잠깐 보이다가 사라지는, 모달리스 형식의 다이얼로그
  ``` Java
  Toast t = Toast.makeText(this, "종료하려면 한번 더 누르세요", Toast.LENGTH_SHORT);
  t.show();

  // 객체 생성 없이 사용하기도 함
  Toast.makeText(this, "종료하려면 한번 더 누르세요", Toast.LENGTH_SHORT).show();
  ```
- 토스트 생성
  ```Java
  // String 리소스 사용
  makeText(Context context, int resId, int duration)
  // 문자열 사용
  makeText(Context context, CharSequence text, int duration)
  ```
  - `duration`
    - `Toast.LENGTH_SHORT` - 3초
    - `Toast.LENGTH_LONG` - 5초
    - 임의의 숫자값으로 지정할 수 없음
- 토스트 설정
  - `setDutation(int duration)`: 화면에 보이는 시간 설정
  - `setText(int resId)`: 토스트 메세지 설정
  - `setText(CharSequence text)`: 토스트 메세지 설정
  - `setView(View view)`: 임의의 뷰를 토스트로 띄우도록 설정
  - `setGravity(int gravity, int xOffset, int yOffset)`: 토스트가 뜨는 위치 설정
  - `setMargin(float horizontalMargin, float verticalMargin)`: 토스트가 뜨는 위치 설정

### 5.2.2. 알림 창(AlertDialog)
타이틀, 최대 세 개의 버튼, 메시지, 선택 가능한 목록 또는 사용자 지정 레이아웃을 표시할 수 있는 다이얼로그. 다이얼로그를 사용할 때 일반적으로 `Dialog` 클래스를 직접 인스턴스화 하기보단 서브 클래스인 `AlertDialog`를 사용함
#### 구성
- 타이틀: `AlertDialog`의 맨 윗줄, 아이콘 이미지와 문자열이 위치
- 본문: `AlertDialog`의 가운데 부분, 문자열을 비롯하여 다양하게 구성할 수 있음
- 버튼: `AlertDialog`의 맨 아랫줄, Positive Button, Negative Button, Neutral Button이 존재할 수 있음
- `AlertDialog` 구성 시 타이틀과 버튼에 대한 정보를 지정하지 않으면 두 영역은 출력되지 않음
#### Builder
`AlertDialog`는 `new` 연산자로 생성하지 못하고 `Builder` 클래스를 통해 생성함
- `Builder` 객체 생성
  ```Java
  AlertDialog.Builder builder = new AlertDialog.Builder(this);
  ```
- `Builder` 객체 설정
  ```Java
  builder.setICon(android.R.drawable.ic_dialog_alert);
  builder.setTile("알림");
  builder.setMessage("정말 종료 하시겠습니까?");
  builder.setPositiveButton("OK", listener);
  builder.setNegativeButton("NO", listener);
  ```
  - `setter` 메소드를 통해 다이얼로그 구성을 설정
    - `setIcon(int inconId)`: 타이틀 영역의 아이콘 지정
    - `setMessage(CharSequence message)`: 본문을 단순 문자열로 구성
    - `setTitle(CharSequence title)`: 타이틀 문자열 지정  
  - `Button` 추가
    ```Java
    setPositiveButton(CharSequence text, DialogInterface.OnClickListener listener)
    setNegativeButton(CharSequence text, DialogInterface.OnClickListener listener)
    setNeutralButton(CharSequence text, DialogInterface.OnClickListener listener)
    ```
    - 버튼의 성격을 구분하여 버튼 클릭 이벤트 처리를 다르게 함
    - 같은 성격의 버튼을 두 개 추가할 수 없음
  - `DialogInterface.OnClickListener`: 버튼 클릭 이벤트 처리 리스너
    ```Java
    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        //...
      }
    };
    ```
- `AlertDialog` 닫힘 제어
  - 아래 세가지 경우 `AlertDialog`가 닫힘
    - `AlertDialog` 하단의 버튼을 누름
    - 스마트폰의 뒤로가기 버튼을 누름
    - `AlertDialog` 밖의 화면을 터치
  - `AlertDialog` 닫힘 제어
    - `setCancelable(boolean cancelable)`: 뒤로가기 버튼에 의한 닫힘 제어
    - `setCanceledOnTouchOutside(boolean cancel)`: `AlertDialog` 밖의 화면 터치에 의한 닫힘 제어
#### AlertDialog
- `AlertDialog` 생성 및 출력
  ```Java
  AlertDialog alertDialog = builder.create();
  alertDialog.show();
  ```
- `Builder` 클래스를 이용하여 `AlertDialog`를 생성하는 이유
  - 다양한 설정에 따라 달라지는 객체에 대응하기 위해 `Builder` 클래스를 이용
  - 이러한 방식을 디자인 패턴에서는 빌더 패턴(Builder Pattern)이라고 함

### 5.2.3. 목록(AlertDialog)
`AlertDialog`의 본문에 목록을 출력하는 다이얼로그
- 목록 본문 생성 - `AlertDialog`의 본문에 문자열 배열을 주면 알아서 목록 형태로 만들어 줌
  ```Java
  // String 배열 리소스 사용
  setItems(int itemsId, DialogInterface.OnClickListener listener)
  // 문자열 사용
  setItems(CharSequence[] items, DialogInterface.OnClickListener listener)
  ```
- `DialogInterface.OnClickListener`: 버튼 클릭 이벤트 처리 리스너
  ```Java
  DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialog, int which) {
      //...
    }
  };
  ```
  - `int which` - 선택한 항목의 인덱스 값
- 항목 선택 - 체크 박스로 설정
  ```Java
  setMultiChoiceItems(int itemsId, boolean[] checkedItems, DialogInterface.OnMultiChoiceClickListener)
  setMultiChoiceItems(CharSequence[] items, boolean[] checkedItems, DialogInterface.OnMultiChoiceClickListener)
  ```
- 항목 선택 - 라디오 버튼으로 설정
  ```Java
  setSingleChoiceItems(int itemsId, int checkedItems, DialogInterface.OnClickListener)
  setSingleChoiceItems(CharSequence[] items, int checkedItems, DialogInterface.OnClickListener)
  ```

### 5.2.4. 날짜 선택(DatePickerDialog)
사용자가 날짜를 선택할 수 있는 다이얼로그
- 생성
  ```Java
  DatePickerDialog dateDialog = new DatePickerDialog(this, listener, year, month, day);
  ```
  - `year, month, day`: 기본으로 보여야 할 날짜의 연, 월, 일
- `DatePickerDialog.OnDateSetListener` - 날짜 조정 이벤트 처리 리스너
  ```Java
  DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
      //...
    }
  };
  ```

### 5.2.5. 시간 선택(TimePickerDialog)
사용자가 시간을 선택할 수 있는 다이얼로그
- 생성
  ```Java
  TimePickerDialog timeDialog = new TimePickerDialog(this, listener, hour, minute, true);
  ```
  - `hour, minute`: 기본으로 보여야 할 시간의 시, 분
  - `true(false)`: 24시간 체계 여부
- `TimePickerDialog.OnTimeSetListener` - 시간 조정 이벤트 처리 리스너
  ```Java
  TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
      //...
    }
  };
  ```

### 5.2.6. 커스텀 다이얼로그(AlertDialog)
개발자가 지정한 임의의 화면을 가진 다이얼로그
- 생성
  ```Java
  LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
  View view = inflater.inflate(R.layout.dialog_custom, null);
  builder.setView(view);
  ```
  - `R.layout.dialog_custom`: 다이얼로그 화면을 구성하는 레이아웃 XML
  - LayoutInflater 클래스를 이용하여 레이아웃 XML 파일을 초기화하고 뷰 생성
    > LayoutInflater - 레이아웃 XML 파일을 초기화고 View 객체로 인스턴스화하기 위해 사용되는 클래스
  - `builder.setView(view)`: 초기화된 뷰를 `AlertDialog`의 본문에 지정
