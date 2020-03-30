# 7장 리소스 활용 및 스마트폰 크기 호환성
## 목록
[7.1.]() 안드로이드 리소스
  - [7.1.1.]() 리소스 종류
  - [7.1.2.]() 다양한 리소스 활용

[7.2.]() 스마트폰 크기 호환성
  - [7.2.1.]() 리소스 폴더명 조건 명시법
  - [7.2.2.]() DisplayMetrics
  - [7.2.3.]() 논리적 단위로 스마트폰 크기 호환성 확보

## 7.1. 안드로이드 리소스
리소스는 안드로이드 프로그램 개발 생산성과 유지보수에 도움을 줌
### 7.1.1. 리소스 종류
#### 특징
- 리소스들은 모두 res 폴더 하위에 있어야 함
- 리소스별 폴더명이 지정되어 있음
- 리소스 폴더 하위에 서브 폴더 불가능
- 각 폴더에 리소스 파일을 추가하는 순간, 추가한 리소스를 식별하기 위한 `int`형 변수가 `R.java` 파일에 추가됨
- 파일명을 변수명으로 사용하므로 리소스 파일명은 자바 명명규칙을 따름
- 리소스 파일명에는 대문자를 사용할 수 없음
#### 종류
- `drawable`: 이미지, 이미지 관련 `XML`, 그림을 표현한 `XML`
- `layout`: 화면 UI를 정의한 레이아웃 `XML`
- `values`: 문자열, 색상, 크기 등 여러가지 값
- `menu`: 액티비티의 메뉴를 구성하기 위한 `XML`
- `xml`: 특정 폴더가 지정되어 있지 않은 기타 `XML`
- `anim`: 애니메이션을 위한 `XML`
- `raw`: 바이트 단위로 직접 이용되는 이진 파일
- `mipmap`: 앱 아이콘 이미지

### 7.1.2. 다양한 리소스 활용
#### 애니메이션 리소스
- 고정 애니메이션의 경우 리소스 파일을 이용
  > ex) 화면전환 애니메이션 등

- `../res/anim`에 애니메이션 리소스 `XML` 파일을 저장
  ```Java
  <set xmlns:android="http://schemas.android.com/apk/res/android">
    <translate />
    <rotate />
    <alpha />
    <scale />
  </set>
  ```
- 태그 종류
  - `scale`: 크기 변경 애니메이션 - 크기 확대 및 축소
  - `rotate`: 회전 애니메이션
  - `alpha`: 투명도 조정 애니메이션
  - `translate`: 이동 애니메이션
- 태그 공통 속성
  - `duration`: 애니메이션 지속 시간(단위: `millisecond`)
  - `startOffset`: 애니메이션 시작 후 얼마 뒤에 애니메이션 효과를 적용할지 결정(단위: `millisecond`)
  - `repeatCount`: 숫자 - 반복 횟수 / `infinite` - 무한 반복
  - `repeatMode`: 반복 방향(`restart` - 효과 그대로 / `reverse` - 효과 반대 방향으로)
- 태그 별 중요 속성
  - `<scale>`  
    | 속성 | 의미 |
    | :---: | --- |
    | `fromXScale` | x축 방향 시작 배율 |
    | `toXScale` | x축 방향 끝 배율 |
    | `fromYScale` | y축 방향 시작 배율 |
    | `toYScale` | y축 방향 끝 배율 |
    | `pivotX` | 확대 축소 x축 기준점 |
    | `pivotY` | 확대 축소 y축 기준점 |
  - `<rotate>`  
    | 속성 | 의미 |
    | :---: | --- |
    | `fromDegrees` | 회전 시작 각도 |
    | `toDegrees` | 회전 끝 각도 |
    | `pivotX` | 확대 축소 x축 기준점 |
    | `pivotY` | 확대 축소 y축 기준점 |
  - `<alpha>`
    | 속성 | 의미 |
    | :---: | --- |
    | `fromAlpha` | 시작 투명도 |
    | `toAlpha` | 끝 투명도 |
  - `<translate>`
    | 속성 | 의미 |
    | :---: | --- |
    | `fromXDelta` | x축 방향 이동 시작 지점 |
    | `toXDelta` | x축 방향 이동 끝 지점 |
    | `fromYDelta` | y축 방향 이동 시작 지점 |
    | `toYDelta` | y축 방향 이동 끝 지점 |
- 애니메이션 적용 예시
  ```Java
  Animation anim = AnimationUtils.loadAnimation(this, R.anim.sample);
  imageView.startAnimation(anim);
  ```
- 애니메이션과 관련된 이벤트
  ```Java
   anim.setAnimationListener(new Animation.AnimationListener() {
     @Override
     public void onAnimationStart(Animation animation) {
       //...
     }
     @Override
     public void onAnimationEnd(Animation animation) {
       //...
     }
     @Override
     public void onAnimationRepeat(Animation animation) {
       //...
     }
   })
  ```

#### values 리소스
- 문자열, 배열, 색상, 크기 등 값이라고 표현되는 리소스
- 다른 리소스와 달리 각 `XML` 파일의 태그 이름값으로 식별됨
- 종류 및 권장 파일명
  | 파일명 | 설명 | 태그 | `R.java` 변수 |
  | :---: | :---: | :---: | :---: |
  | `strings.xml` | 문자열 | `<string>` | `string` |
  | `colors.xml` | 색상 | `<color>` | `color` |
  | `styles.xml` | 스타일 |  `<style>` | `style` |
  | `arrays.xml` | 배열 | `<string-array>` `<integer-array>` |  `array` |
  | `dimens.xml` | 크기 | `<dimen>` | `dimen` |
- `style` 리소스
  - 여러 속성을 하나의 스타일로 묶어 필요한 곳에 적용하기 위해 사용
  - 화면 구성 시 같은 속성의 중복을 피할 때 유용
  - 다른 스타일을 상속받아 재정의할 수도 있음
    ```Java
    <style name="myStyle">
      <item name="android:textColor">#FF0000FF</item>
      <item name="android:textSize">20dp</item>
      <item name="android:textStyle">bold</item>
    </style>

    <style name="mySubStyle" parent="myStyle">
      <item name="android:textStyle">italic</item>
    </style>
    ```

#### 테마(Theme) 리소스
- 액티비티 전체 혹은 앱 전체를 위한 `style` 리소스
- `styles.xml`에 `<style>` 태그를 이용해 정의
- 뷰를 위한 스타일이 아니므로 `AndroidManifest.xml`에서 설정
  ```Java
  <application
    android:allowBackup="true"
    android:icon="@mipmap/ic_launcher"
    android:label="@string/app_name"
    android:supportsRtl="true"
    android:theme="@style/AppTheme">
    // 앱 전체에 테마 적용
  </application>
  ```
  ```Java
  <activity android:name=".MainActivity" android:theme="@style/AppTheme">
    // 특정 액티비티에만 적용
  </activity>
  ```
- 툴바 제거
  ```Java
  <style name="myTheme" parent="Theme.AppCompat.Light.DarkActionBar">
    <item name="windowNoTitle">true</item>
    <item name="windowActionBar">false</item>
  </style>
  ```
- 상태바 제거
  ```Java
  <style name="myTheme" parent="Theme.AppCompat.Light.DarkActionBar">
    <item name="android:windowFullscreen">false</item>
  </style>
  ```
- 액티비티 가로 or 세로 방향 고정
  ```Java
  <activity
    android:name=".MainActivity"
    // 가로 방향으로 고정
    android:screenOrientation="landscape"
    // 세로 방향으로 고정
    android:screenOrientation="portrait">
    //...
  </activity>
  ```

## 7.2. 스마트폰 크기 호환성
### 7.2.1. 리소스 폴더명 조건 명시법
- 개발자가 리소스 폴더명만 적절한 규칙에 따라 작성해 놓으면 시스템에서 어떤 환경의 기기에 어떤 리소스를 적용할 것인지 스스로 결정
  > 5개의 `mipmap` 폴더에서 대시(-) 뒤에 단어는 기기 환경에 대한 조건이며 이를 통해 시스템에서 기기 환경에 맞는 폴더를 스스로 결정하게 함

- 폴더명에 지정할 수 있는 조건
  조건명 | 설명
  | --- | --- |
  | `MCC, MNC` | 모바일 국가 코드, 모바일 네트워크 코드 조건 |
  | `Language, region`| 언어 및 지역 조건 ex) en, en-rUS |
  | `Layout Direction`| 레이아웃 방향 조건(`ldrtl`: 오른쪽에서 왼쪽, `ldltr`: 왼쪽에서 오른쪽) |
  | `smallestWidth`| 화면 방향과는 상관없이 화면의 높이와 너비 중 짧은 쪽에 대한 조건 |
  | `Available width`| 화면의 너비에 대한 조건 |
  | `Available height`| 화면의 높이에 대한 조건 |
  | `Screen size`| 화면 크기에 대한 조건 |
  | `Screen aspect`| 화면의 종횡비 조건 |
  | `UI mode` | 기기가 독(dock)에 추가되거나 제거될 때 대응할 수 있는 방법의 조건 |
  | `Night mode` | 야간 모드에 대응할 수 있는 조건 |
  | `Screen pixel density(dpi)` | 화면 밀도에 대한 조건 |
  | `Touchscreen type` | 터치스크린 지원 여부 조건 |
  | `Keyboard availability` | 키보드에 대한 조건 |
  | `Primary text input method` | 키보드 타입에 대한 조건 |
  | `Navigation key availability` | UI 탐색 키 사용 유무에 대한 조건 |
  | `Primary non-touch navigation method` | 탐색 키를 지원하는지에 대한 조건 |
  | `Platform Version(API Level)` | 기기의 API 레벨에 대한 조건 |
- 조건 명시 규칙
  - 대시로 구분하여 하나의 폴더에 여러 조건을 명시할 수 있음 (AND 연산으로 적용)
  - 여러 조건을 명시할 때는 조건의 순서를 지켜야함
  - 폴더에 서브 폴더로 조건을 세분화하는 것은 불가능
  - 조건에 대한 대소문자는 구분하지 않음
  - 하나의 폴더에는 각 조건마다 하나의 값만을 명시할 수 있음

### 7.2.2. DisplayMetrics
- 직접 스마트폰의 크기 정보를 획득해야하는 경우, 이를 지원해주기 위한 클래스
  ```Java
  DisplayMetrics dm = new DisplayMetrics();
  getWindowManager().getDefaultDisplay().getMetrics(dm);
  ```
- 화면 정보
  - `widthPixels`: 가로 화소 수
  - `heightPixels`: 세로 화소 수
  - `densityDpi`: 화면 밀도
  - `density`: mdpi를 기준으로 한 배율. 스케일링 시 곱해지는 값
  - `scaledDensity`: 문자열 스케일링 시 곱해지는 값
  - `xdpi`: 정확한 가로 밀도
  - `ydpi`: 정확한 세로 밀도

### 7.2.3. 논리적 단위로 스마트폰 크기 호환성 확보
- 다양한 크기 단위
  - dp(dip): Density-Independent Pixels. 스크린의 물리적 밀도에 기초한 단위. 160dpi에 상대적 수치
  - sp(sip): Scale-Independent Pixels. dp와 유사하며 폰트 크기에 적용
  - pt: Points. 화면 크기의 1/72를 1pt
  - px: 픽셀
  - mm: 밀리미터
  - in: 인치
  - dp와 sp를 사용하길 권장
- 자바 코드에선 픽셀 단위로만 크기를 지정할 수 있기 때문에 `dimen`리소스를 사용하거나 `DisplayMetrics` 클래스로 직접 계산해야 함
