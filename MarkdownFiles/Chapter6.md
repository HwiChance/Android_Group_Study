# 6장 사용자 이벤트 처리
## 목록
[6.1.](https://github.com/HwiChance/Android_Group_Study/blob/master/ReadmeFolder/Chapter6.md#61-%EB%8D%B8%EB%A6%AC%EA%B2%8C%EC%9D%B4%EC%85%98-%EC%9D%B4%EB%B2%A4%ED%8A%B8-%EB%AA%A8%EB%8D%B8) 델리게이션 이벤트 모델
  - [6.1.1.](https://github.com/HwiChance/Android_Group_Study/blob/master/ReadmeFolder/Chapter6.md#611-%EC%9D%B4%EB%B2%A4%ED%8A%B8-%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%A8-%EA%B5%AC%EC%A1%B0) 이벤트 프로그램 구조
  - [6.1.2.](https://github.com/HwiChance/Android_Group_Study/blob/master/ReadmeFolder/Chapter6.md#612-%EB%8B%A4%EC%96%91%ED%95%9C-%EC%9D%B4%EB%B2%A4%ED%8A%B8-%EC%B2%98%EB%A6%AC) 다양한 이벤트 처리

[6.2.](https://github.com/HwiChance/Android_Group_Study/blob/master/ReadmeFolder/Chapter6.md#62-%ED%95%98%EC%9D%B4%EC%96%B4%EB%9D%BC%ED%82%A4-%EC%9D%B4%EB%B2%A4%ED%8A%B8-%EB%AA%A8%EB%8D%B8) 하이어라키 이벤트 모델
  - [6.2.1.](https://github.com/HwiChance/Android_Group_Study/blob/master/ReadmeFolder/Chapter6.md#621-%ED%84%B0%EC%B9%98-%EC%9D%B4%EB%B2%A4%ED%8A%B8) 터치 이벤트
  - [6.2.2.](https://github.com/HwiChance/Android_Group_Study/blob/master/ReadmeFolder/Chapter6.md#622-%ED%82%A4-%EC%9D%B4%EB%B2%A4%ED%8A%B8) 키 이벤트

## 6.1. 델리게이션 이벤트 모델
뷰에서 발생하는 이벤트를 처리하기 위한 모델
### 6.1.1. 이벤트 프로그램 구조
- 델리게이션 이벤트 모델 - 이벤트 소스와 이벤트 핸들러를 리스너로 여결하여 처리하는 구조
  - 이벤트 소스(Event Source): 이벤트가 발생한 뷰 객체
  - 이벤트 핸들러(Event Handler): 이벤트 처리 내용을 가지는 객체
  - 리스너(Listener): 이벤트 소스와 이벤트 핸들러를 연결하는 작업
- 모든 뷰의 이벤트를 터치(Touch) 이벤트로 처리하지 않고 이러한 구조를 사용하는 이유는 이벤트를 조금 더 명료하게 처리하기 위함
- 이벤트 소스와 이벤트 핸들러를 리스너로 연결
  ```Java
  view.setOn<XXX>Listener(new Handler());
  ```
  `Handler()`: 사용자 정의 핸들러 클래스를 사용할 경우 해당 클래스는 반드시 지정된 인터페이스를 구현해야 함
- 이벤트 코드 형태의 의미
  ```Java
  btn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      //...
    }
  });
  ```
  `OnClickListener`를 구현한 이름 없는 하위 클래스를 선언하고 생성해서 리스너로 연결한 것

### 6.1.2. 다양한 이벤트 처리
- 주요 이벤트

  | Event | 설명 |
  | --- | --- |
  | OnClickListener | 뷰 클릭 시 발생하는 이벤트 |
  | OnLongClickListener | 뷰를 오래 클릭했을 때 발생하는 이벤트 |
  | OnCheckedChangeListener | CheckBox의 상태 변경 이벤트 |
  | OnItemClickListener | ListView의 항목 선택 이벤트 |
  | OnDateSetListener | DatePicker의 날짜 선택 이벤트 |
  | OnTimeSetListener | TimePicker의 시간 선택 이벤트 |

- `OnClickListener` & `OnLongClickListener`
  ```Java
  view.setOnClickListener(new View.setOnClickListener() {
    @Override
    public void OnClick(View v) {
      //...
    }
  });

  view.setOnLongClickListener(new View.setOnLongClickListener() {
    @Override
    public boolean OnLongClick(View v) {
      //...
    }
  });
  ```
  - 모든 뷰에 적용할 수 있는 이벤트

## 6.2. 하이어라키 이벤트 모델
액티비티가 화면에 출력되었을 때 발생하는 사용자의 키 이벤트와 화면 터치 이벤트를 처리하기 위한 모델. 만약 액티비티에서 터치 이벤트와 키 이벤트를 직접 처리하고 싶다면 이벤트 발생 시 자동 호출되는 함수만 액티비티 내에서 재정의하면 됨
### 6.2.1. 터치 이벤트
- 화면 터치 이벤트 콜백 함수를 액티비티 내에서 정의하여 이벤트 처리
  ```Java
  @Override
  public boolean onTouchEvent(MotionEvent event) {
    return super.onTouchEvent(event);
  }
  ```
- 이벤트 타입
  - `event.getAction()`: 발생한 이벤트 타입 반환
  - `ACTION_DOWN`: 화면에 터치된 순간의 이벤트
  - `ACTION_UP`: 터치를 떼는 순간의 이벤트
  - `ACTION_MOVE`: 터치한 후 이동하는 순간의 이벤트
- 좌표 값
  - `getX()`: 이벤트가 발생한 뷰 내에서의 *x* 좌표
  - `getY()`: 이벤트가 발생한 뷰 내에서의 *y* 좌표
  - `getRawX()`: 화면에서의 *x* 좌표
  - `getRawY()`: 화면에서의 *y* 좌표
  - 좌표 값은 픽셀 단위

### 6.2.2. 키 이벤트
- 소프트 키보드 이외의 키 이벤트를 처리
- 안드로이드의 소프트 키보드는 키 이벤트로 처리할 수 없음
  > 하드웨어 키보드가 제공되는 스마트폰은 키 이벤트로 처리 가능  

- 뒤로가기 버튼, 메뉴 버튼, 검색 버튼 등의 이벤트 처리 가능
- 홈 버튼, 전원 버튼, 오버뷰 버튼은 이벤트 처리 불가능
- 키 이벤트가 발생할 때 호출되는 콜백 함수를 액티비티 내에서 정의하여 이벤트 처리
  ```Java
  // 키가 눌린 순간의 이벤트
  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    return super.onKeyDown(keyCode, evnet);
  }

  // 키를 떼는 순간의 이벤트
  @Override
  public boolean onKeyUp(int keyCode, KeyEvent event) {
    return super.onKeyUp(keyCode, evnet);
  }

  // 키를 오래 누르는 순간의 이벤트
  @Override
  public boolean onKeyLongPress(int keyCode, KeyEvent event) {
    return super.onKeyLongPress(keyCode, evnet);
  }
  ```
- 뒤로가기 버튼 이벤트 처리
  ```Java
  @Override
  public void onBackPressed() {
    super.onBackPressed();
  }
  ```
