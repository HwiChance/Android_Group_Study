# 8장 DBMS을 이용한 데이터 영속화
## 목록
[8.1.]() SQLite을 이용한 영속화
  - [8.1.1.]() SQLiteDatabase 클래스
  - [8.1.2.]() SQLiteOpenHelper 클래스
  - [8.1.3.]() insert(), query(), update(), delete() 함수 이용

[8.2.]() Realm을 이용한 데이터 영속화
  - [8.2.1.]() Realm 소개
  - [8.2.2.]() Realm 사용 설정

## 8.1. SQLite을 이용한 영속화
#### SQLite란?
- SQLite는 오픈소스로 만들어진 관계형 데이터베이스
- 복잡하고 구조화된 애플리케이션 데이터를 저장하고 관리
- 프로세스가 아닌 라이브러리를 이용하므로 DB는 앱의 일부로 통합됨
- SQLite 데이터는 파일 형태로 `data/data/[package_name]/databases`에 저장됨
#### 로컬 데이터베이스를 사용하는 이유?
- 서버에서 받은 데이터를 일정 정도 스마트폰 내부에 저장하여 네트워크 연결이 끊기는 상황에 대응하기 위함

### 8.1.1. SQLiteDatabase 클래스
- DB에 데이터를 저장, 호출, 수정, 삭제하는 모든 SQL 질의문은 `SQLiteDatabase`클래스를 이용하여 수행함
  ```Java
  // SQLite 데이터베이스 생성 메소드
  public abstract SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory)
  // 객체 생성 예시
  SQLiteDatabase db = openOrCreateDatabase("memodb", MODE_PRIVATE, null);
  ```
  - `openOrCreateDatabase`: `Context`클래스의 추상 메소드
  - `name`: 파일명
  - `mode`: 파일 생성 모드
  - `factory`: query의 결과 반환될 커서를 담을 객체
- SQL 수행 메소드
  ```Java
  // select SQL 수행 메소드
  Cursor rawQuery(String sql, String[] selectionArgs)
  // select SQL을 제외한 나머지 SQL 수행 메소드
  void execSQL(String sql, Object[] bindArgs)

  // 사용 예시
  Cursor cursor = db.rawQuery("select title, content from tb_memo order by _id desc limit 1", null);
  db.execSQL("insert into tb_memo (title, content) values (?,?)", new String[]{title, content});
  ```
  - `rawQuery`: query에 따라 선택된 행들 중 첫번째 행을 가리키는 커서를 반환하는 메소드
    - `moveToNext()`: 커서가 다음 행을 가리키도록 하는 메소드
    - `moveToFirst()`: 커서가 첫 번째 행을 가리키도록 하는 메소드
    - `moveToLast()`: 커서가 마지막 행을 가리키도록 하는 메소드
    - `moveToPrevious()`: 커서가 이전 행을 가리키도록 하는 메소드
    - `getString(int columnIdx)`: 커서가 가리키는 행에서 `columnIdx`번째 열의 데이터를 가져오는 메소드
  - `execSQL`: query를 실행하는 메소드
  - query 안에 '?'가 존재할 경우 각 메소드의 두번째 인자로 주어진 데이터가 순서대로 대입됨

### 8.1.2. SQLiteOpenHelper 클래스
- DB 관리만을 목적으로 하는 추상 클래스
- 데이터 저장이나 획득 등의 코드는 `SQLiteDatabase`클래스를 이용
- 테이블 생성이나 스키마 변경 등의 작업은 `SQLiteOpenHelper`클래스를 이용
- `select`이나 `insert` 작업을 수행할 때 테이블 생성 여부를 판단하지 않아도 되는 이점이 있음
  ```Java
  public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
      super(context, "memodb", null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //...
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      //...
    }
  }
  ```
  - `onCreate()`: 앱이 설치된 후 `SQLiteOpenHelper`가 최초로 이용되는 순간 한 번 호출되는 메소드로 일반적으로 테이블을 생성하는 코드가 여기에 작성됨
  - `onUpgrade()`: `SQLiteOpenHelper`의 생성자에 전달되는 데이터베이스 버전이 변경될 때마다 호출되는 메소드로 테이블의 스키마를 변경하기 위한 용도로 사용됨
  ```Java
  DBHelper helper = new DBHelper(this);
  SQLiteDatabase db = helper.getWritableDatabase();
  // 또는
  SQLiteDatabase db = helper.getReaderableDatabase();
  ```
  - `getWritableDatabase()`: 읽기 또는 쓰기용으로 DB 생성(또는 열기)
  - `getReaderableDatabase()`: 디스크가 꽉차있지 않거나 DB가 읽기 전용으로 열려야 하는것이 아니라면 `getWritableDatabase()`와 같은 DB 오브젝트를 반환

### 8.1.3. insert(), query(), update(), delete() 함수 이용
- `rawQuery()`나 `execSQL()`과 달리 SQL문을 만들기 위한 정보만 매개변수로 주면 자동으로 SQL문을 만들어 실행하는 메소드
- Insert
  ```Java
  long insert(String table, String nullColumnHack, ContentValues values)
  ```
- Update
  ```Java
  int update(String table, ContentValues values, String whereClause, String[] whereArgs)
  ```
- Delete
  ```Java
  int delete(String table, String whereClause, String[] whereArgs)
  ```
- Query
  ```Java
  Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit)
  ```
- ContentValues 클래스: key-value 형식의 객체
  ```Java
  ContentValues values = new ContentValues();
  values.put(key, value);
  ```

## 8.2. Realm을 이용한 데이터 영속화
### 8.2.1. Realm 소개
- [https://realm.io](https://realm.io)에서 오픈소스로 만들어지고 있는 로컬 DB
- `SQLite`와의 가장 큰 차이점은 ORM을 제공한다는 것
  > ORM: Object-Relational Mappings의 약어로 DB 이용 시 SQL을 사용하는 대신 OOP의 객체를 해석해 데이터를 저장하거나 획득해주는 방식을 말함

- `SQLite`보다 훨씬 빠름
### 8.2.2. Realm 사용 설정
- 표준 라이브러리에서 제공하지 않으므로 플러그인 방식으로 이용해야 함
- 따라서 프로젝트 수준의 `Gradle` 파일에 `dependency`를 설정해야 함
  ```Java
  // 프로젝트명으로 된 Gradle 파일
  dependencies {
    //...
    classpath "io.realm:realm-gradle-plugin:3.5.0"
  }
  ```
- 모듈에 플러그인 적용
  ```Java
  // 모듈명으로 된 Gradle 파일
  apply plugin: 'realm-android'
  ```
### 8.2.3. Realm 사용
- `Realm`이 관리할 VO(Value-Object) 클래스 생성
  ```Java
  public class MemoVO extends RealmObject {
    public String title;
    public String content;
    // private으로 선언하고 getter/setter 함수를 제공해도 상관 없음
  }
  ```
- `Realm` 객체 획득
  ```Java
  Realm.init(this);
  Realm mRealm = Realm.getDefaultInstance();
  ```
- `Realm` 객체를 이용하여 데이터 저장
  ```Java
  mRealm.executeTransaction(new Realm.Transaction() {
    @Override
    public void execute(Realm realm) {
      // realm과 연결된 VO객체 생성
      MemoVO vo = realm.createObject(MemoVO.class);
      // 데이터 저장
      vo.title = title;
      vo.content = content;
    }
  });
  ```
- `Realm` 객체를 이용하여 데이터 획득
  ```Java
  // title이 hello인 데이터 중 첫 번째 데이터를 가져오는 코드
  MemoVO vo = mRealm.where(MemoVO.class).equalTo("title", "hello").findFirst();
  ```
  - `where()` 뒤에 데이터 획득 조건을 명시할 수 있음
    - `equalTo`, `between()`, `in()`, `lessThan()` 등 다양한 메소드 존재
  - `mRealm.where(MemoVO.class)`에 의해 반환된 `RealmQuery`객체에서 데이터를 획득하기 위한 메소드
    - `findAll()`
    - `findAllSorted(String fieldName)`
    - `findAllSorted(String[] fieldNames, Sort[] sortOrders)`
    - `findAllSorted(String fieldName, Sort sortOrder)`
    - `findAllSorted(String fieldName1, Sort sortOrder1, String fieldName2, Sort sortOrder2)`
    - `findFirst`
  - 데이터가 여러건이면 `RealmResults`타입으로 획득
    ```Java
    RealmResults<MemoVO> results = mRealm.where(MemoVO.class).equalTo("title", "Tiger").findAll();
    ```
- `Realm` 객체를 이용하여 데이터 삭제
  ```Java
  MemoVO vo = mRealm.where(MemoVO.class).equalTo("title", "hello").findFirst();
  vo.deleteFromRealm();
  ```
  - 특정 타입의 모든 데이터를 삭제하려면 `delete()`함수 사용
    ```Java
    mRealm.delete(MemoVO.class);
    ```
