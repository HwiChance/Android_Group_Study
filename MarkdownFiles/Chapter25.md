# 25장 HTTP 통신
## 목록
[25.1.](#251-java-api를-이용한-http-통신) Java API를 이용한 HTTP 통신

[25.2](#252-volley-api를-이용한-http-통신) Volley API를 이용한 HTTP 통신

[25.3](#253-retrofit2) Retrofit2
- [25.3.1.](#2531-retrofit-이용-준비-및-구조) Retrofit 이용 준비 및 구조
- [25.3.2.](#2532-retrofit을-이용한-http-통신) Retrofit을 이용한 HTTP 통신
- [25.3.3.](#2533-retrofit-어노테이션) Retrofit 어노테이션

## 25.1. Java API를 이용한 HTTP 통신
- 네트워킹을 위한 권한 설정
  ```xml
  <uses-permission android:name="android.permission.INTERNET"/>
  ```
  - 외부 서버와 데이터를 주고 받을때 필요한 퍼미션
  - HTTP 통신뿐 아니라 소켓 연결 때도 사용
- API Level 28 (Android 9)부터 보안 강화 목적으로 앱에서의 HTTP 통신이 기본으로 금지되었고 HTTPS를 사용해야 함
  - API Level 28 이상에서 HTTP 사용하기
  ```xml
  <!-- ./res/xml -->
  <?xml version="1.0" encoding="utf-8"?>
  <network-security-config>
    <domain-config cleartextTrafficPermittted="true">
      <domain includeSubdomains="true">xxx.xxx.xxx.xxx</domain>
    </domain-config>
  </network-security-config>
  ```
  - `<domain-config`> 내부에 `<domain>` 태그로 HTTP 통신을 허용한 도메인이나 IP 주소를 명시
  ```xml
  <!-- AndroidManifest.xml -->
  <application
    ...
    android:networkSecurityConfig="@xml/network_security_config">
  ```
  - xml 파일을 `AndroidManifest`에 등록
  - 앱이 동작하면서 동적으로 도메인을 결정하는 경우
    ```xml
    <application
      ...
      android:usesCleartextTraffic="true">
    ```

#### 프로그램 작성 방법
- 사용되는 패키지
  - `java.net`: `URL` 표현 및 서버 연결 담당
  - `java.io`: 연결된 서버와 데이터를 주고받는 I/O 담당
- 웹 서버와 연결
  ```java
  URL text = new URL(url);
  HttpURLConnection http = (HttpURLConnection) text.openConnection();
  ```
  - `URL` 클래스: 서버의 URL 정보를 표현
  - `HttpURLConnection` 클래스: 실제 HTTP 연결을 요청
    - `setConnectTimeput()`: 연결 타임아웃 시간 지정
    - `setReadTimeout()`: 읽기 타임아웃 시간 지정
    - `setUseCaches()`: 캐시 사용 여부 지정
    - `setDoInput()`: 데이터를 읽을 것인지 지정
    - `setDoOutput()`: 데이터를 `Request Body`에 쓸 것인지 지정
    - `setRequestProperty()`: 요청 헤더 지정
    - `setRequestMethod()`: HTTP 요청 Method 지정
- 서버에 전송하는 데이터  
  - 일반 문자열, `JSON` 문자열, 웹의 `Query` 문자열 등 여러 가지 형태
  - `Query` 문자열이 가장 일반적으로 사용됨
    - Key와 Value를 `=`로 연결하고 여러 데이터를 `&`로 연결하는 형태
    - ex) `no=10&name=kkang`
- HTTP Method
  ![http_method](./img/http_method.png)
  - `GET` 방식: 웹의 URL 뒤에 `?`를 구분자로 `Query` 문자열을 추가하여 전송하는 방식
  - `POST` 방식: HTTP `Request Body`에 넣어 정식 스트림으로 전송하는 방식
    - `setRequestMethod()` 함수를 이용하여 `POST` 방식을 사용하겠다고 선언해야 함
    - `IO` 객체를 이용해 전송 코드를 작성해야 함
- `POST` 방식을 이용하여 데이터를 서버에 전송
  ```java
  PrintWriter writer = new PrintWriter(new OutputStreamWriter(http.getOutputStream(), "UTF-8"));
  writer.write(postData);
  writer.flush();
  ```
  - `getOutputStream()`을 통해 `OutputStream` 객체를 얻는 것은 필수
  - 그 이외에 `OutputStreamWriter`, `PrintWriter` 등은 개발자의 선택
  - 문자열 데이터의 경우 `java.io` 패키지의 `Writer` 클래스 이용
  - 이미지와 같은 바이트 데이터의 경우 `java.io` 패키지의 `OutputStream` 클래스 이용
- 서버로부터 데이터 받기
  - 문자열 데이터의 경우 `Reader` 클래스 이용
    ```java
    BufferReader in = new BufferedReader(new InputStreamReader(http.getInputStream(), "UTF-8"));
    StringBuffer sb = new StringBuffer();
    String inputLine;
    while((inputLine = in.readLine()) != null) {
      sb.append(intputLine);
    }
    response = sb.toString();
    ```
    - `http.getInputStream()`: 서버로부터 넘어오는 데이터 수신
    - `InputStreamReader`: 바이트 스트림을 문자열 스트림으로 변형해주는 클래스
    - `BufferedReader`: 데이터를 한 줄씩 읽는 클래스
  - 바이트 데이터의 경우 `InputStream` 클래스 이용
    ```java
    Bitmap bitmap = BitmapFactory.decodeStream(http.getInputStream());
    ```

## 25.2 Volley API를 이용한 HTTP 통신
- HTTP 연동 코드를 조금 더 간편하게 작성할 목적으로 개발된 API
  - HTTP 연동 시 반복되는 코드를 대행해줌
  - 스레드-핸들러 혹은 `AsyncTask`, 화면 회전 시 서버 요청 취소, 이미지 내려받기에서 캐시 적용 등
- `dependecy` 설정
  `implementation 'com.android.volley:volley:1.1.1'`
- `Volley` 핵심 클래스
  - `RequestQueue`: 서버 요청자. 다른 `Request` 클래스들의 정보대로 서버에 요청을 보내는 역할
  - `StringRequest`: 문자열을 결과로 받는 요청 정보
  - `ImageRequest`: 이미지를 결과로 받는 요청 정보
  - `JsonObjectReqeust`: `JSONObeject`를 결과로 받는 요청 정보
  - `JsonArrayRequest`: `JSONArray`를 결과로 받는 요청 정보
- 기본 구조
  ```java
  StringRequest stringRequest = new StringRequest(...);

  RequestQueue queue = Volley.newRequestQueue(this);
  queue.add(stringRequest);
  ```
  - `Request` 클래스들을 이용하여 서버 요청 정보와 결과 처리 방법을 담은 객체 생성
  - `add`: `Request` 객체를 `RequestQueue`에 넘겨 서버 요청을 발생시킴
  - `Volley` API 내부적으로 스레드-핸들러 구조로 동작하므로 개발자가 ANR을 고려할 필요가 없음
    > ANR: Application Not Responding의 약자로 액티비티가 사용자 이벤트에 5초 이내에 반응하지 못하는 상황을 말함

- `StringRequest` 예시
  ```java
  StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                              new Response.Listener<String>() {
    @Override
    public void onResponse(String response) {
      // 결과 처리
    }
  }, new Response.ErrorListener() {
    public void onErrorListener(VolleyError error) {
      // 에러 처리
    }
  });
  ```
  - `StringRequest(HttpMethod, ServerURL, ResultCallback, ErrorCallback)`
  - `onResponse(String response)`: 서버로부터 수신한 문자열이 매개변수로 전달됨
- 서버에 데이터 전송
  ```java
  StringRequest stringRequest = new StringRequest(...) {
    protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
      Map<String, String> params = new HashMap<String, String>();
      params.put("a1", "kkang");
      return params;
    }
  };
  ```
  - `getParams()`: 서버에 전송한 데이터를 반환
  - 반환된 데이터는 자동으로 웹의 `Query` 문자열 형식으로 만들어짐
- `Volley`는 `JSON` 데이터를 자동으로 파싱해줌
  ```Java
  JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                                                    new Response.Listener<JSONObject>(){
      @Override
      public void onResponse(JSONObject response) {
        // 서버 응답 후 사후처리
        // JSONObject에서 데이터 획득
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        // 에러 처리
      }
    }
  );
  ```
  - `onResponse()` 속 매개변수로 파싱된 `JSON` 데이터가 전달됨
- 이미지 데이터 수신
  ```java
  ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
    @Override
    public void onResponse(Bitmap response) {
      // 결과 처리
    }
  }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
    // maxWidth, maxHeight 등의 사이즈 정보 설정 가능, 0은 설정 안하겠다는 의미
    @Override
    public void onErrorResponse(VolleyError error) {

    }
  });
  ```
  - `Volley`는 서버로부터 받은 이미지를 화면에 출력하기 위한 `NetworkImageView` 클래스를 제공
    ```xml
    <com.android.volley.toolbox.NetworkImageView
    ...
    />
    ```
    - 뷰 클래스 이므로 레이아웃 XML 파일에 등록해야 함
    ```java
    ImageLoader imageLoader = new ImageLoader(queue, new ImageLoader.ImageCache() {
      private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);

      public void putBitmap(String url, Bitmap bitmap) {
        mCache.put(url, bitmap);
      }
      public Bitmap getBitmap(String url) {
        return mCache.get(url);
      }
    });
    // 이미지 요청
    imageView = (NetworkImageView) findViewById(R.id.lab2_image);
    imageView.setImageUrl(url, imageLoader);
    ```
    - `setImageUrl()`: 서버 요청 발생
    - 서버로부터 받은 이미지를 `imageView`에 출력
    - `putBitmap, getBitmap` 함수를 이용하여 이미지 캐싱 진행
    - `LruCache` 안드로이드에서 제공하는 캐싱 클래스

## 25.3 Retrofit2
- 작성하기 쉽고 성능 좋은 HTTP 통신 라이브러리
- `AsyncTask`, `Volley`와 비교해서 가장 빠른 성능을 가짐
### 25.3.1. Retrofit 이용 준비 및 구조
![retrofit_structure](./img/retrofit_structure.png)
- 개발자가 네트워킹을 위해 직접 작성해야 하는 것은 `Interface`뿐
  - `Interface`에는 네트워킹 시 호출해야 할 함수들을 등록
  - `Interface`를 `Retrofit`에 전달하면 `Retrofit`에서 `Interface`의 함수를 구현해 실제 네트워킹을 진행해 주는 `Call` 객체를 반환하는 `Service` 객체를 반환
    - 여기서 `Service` 객체는 안드로이드의 `Service` 컴포넌트가 아님
  - 실제 네트워킹이 필요한 순간 `Service`의 함수를 호출하여 `Call` 객체를 받고 `Call` 객체에게 일을 시키는 구조
- `Call` 객체
  - 실제 서버 연동을 실행하는 객체
  - `Retrofit`이 자동으로 만들어 줌
- `Convertor`
  - `JSON`, `XML`을 자동으로 파싱해 개발자가 만든 `VO` 객체로 변환해주는 역할
  - 여러 외부 라이브러리가 존재
    - `Gson Convertor`를 주로 사용함
- `dependecy` 설정
  ```java
  // Retrofit
  implementation 'com.squareup.retrofit2:retrofit:<version>'
  // Gson
  implementation 'com.google.code.gson:gson:<version>'
  // Gson Converter
  implementation 'com.squareup.retrofit2:converter-gson:<version>'
  ```
- 권한 설정
  ```xml
  <uses-permission android:name="android.permission.INTERNET"/>
  ```

### 25.3.2. Retrofit을 이용한 HTTP 통신
#### Model 정의
- `Model`
  ```json
  // json data
  {
    "id":1,
    "movieTitle":"movie"
  }
  ```
  ```java
  // model
  public class ItemModel {
    public long id;
    @SerializedName("movieTitle")
    public String title;
  }
  ```
  - 서버 연동을 위한 데이터 추상화 클래스
  - 컨버터가 파싱한 `JSON` 혹은 `XML` 데이터를 담을 `VO` 클래스
  - 컨버터가 변수명과 파싱된 데이터의 키값을 매핑해서 데이터를 저장함
  - 변수명과 키값이  다를 경우 `@Serializable` 어노테이션을 통해 키값을 명시할 수 있음
- `Model` 구조적 작성
  ```json
  {
    "articles":[{
        "id":1,
        "movieTitle":"movie"
      },{
        "id":2,
        "movieTitle":"music"
    }],
    "total":123
  }
  ```
  - 위 `JSON`의 경우 `articles` 배열 속 데이터들을 저장할 `Model` 클래스와 `articles`, `total`을 저장할 `Model` 클래스 두가지가 필요함
  ```java
  public class PageListModel {
    public long total;
    public List<ItemModel> articles;
  }
  ```
- `JSON`, `XML` 데이터로 `Model`을 만들어주는 [웹사이트](http://www.jsonschema2pojo.org/)도 있음

#### Retrofit 객체 생성
- `Retrofit` 객체 생성은 `Builder` 패턴을 이용
  ```java
  Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://~~~~~")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
  ```
  - `baseUrl`: 서버 URL 설정
  - `addConverterFactory()`: 컨버터 지정

#### Service 인터페이스
- 서버 네트워킹을 위한 함수를 인터페이스의 추상 함수로 등록
  ```java
  public interface RetrofitService {
    @GET("/v2/everything")
    Call<PageListModel> getList(@Query("q") String q);
  }
  ```
  - `@GET("/v2/everything")`: `GET` 방식을 이용하며 `baseUrl` 뒤에 `Path`를 `/v2/everything`로 설정한다는 의미
  - `@Query("q") string q`: 서버 연동 시 전달하는 `Query` 문자열을 지정하는 부분

#### Call 객체 획득
- `Call` 객체를 반환하는 `Retrofit Service` 객체를 생성
  ```java
  networkService = retrofit.create(RetrofitService.class);
  ```
  - `Retrofit` 객체의 `create()`함수에 `Service` 인터페이스를 전달하여 `Retrofit Service` 객체 생성
- `Service` 객체를 이용해 인터페이스 함수를 호출하여 `Call` 객체 획득
  ```java
  Call<PageListModel> call = networkService.getList(QUERY);
  ```
  - `Call` 객체의 제네릭 정보는 획득하고자 하는 데이터의 타입(= `Model` 클래스)

#### 네트워킹 시도
```java
call.enqueue(new Callback<PageListModel> {
  @Override
  public void onResponse(Call<PageListModel> call, Response<PageListModel> response) {
    if(response.isSuccessful()) {
      // response code에 따라 성공 여부 결정
    }
  }
  @Override
  public void onFailure(Call<PageListModel> call, Throwable t) {
    // 네트워크 오류나 예기치못한 에러가 발생한 경우
  }
})
```
- `Callback`을 이용한 비동기 방식
- `call.excute()` 함수를 통해 동기 방식의 네트워킹이 가능함
  - 이 경우 `ANR`을 대비해야 함
### 25.3.3. Retrofit 어노테이션
- `@GET`, `@POST`, `@PUT`, `@DELETE`, `@HEAD`
  - `HTTP Method`를 지정
  - 매개변수: `baseUrl`에 추가될 문자열
- `@Path`
  ```java
  @GET("/group/{id}/users")
  Call<List<UserModel>> groupList(@Path("id") String id);
  ```
  - `URL`의 일부분이 동적 데이터에 의해 결정되는 경우 사용
  - `URL`에서 동적 값이 들어갈 부분을 중괄호로 명시하고 해당 위치에 들어갈 데이터를 `@Path` 어노테이션으로 명시하는 개념
- `@Query`
  ```java
  @GET("/user")
  Call<List<UserModel>> groupList(@Query("name") String name);
  ```
  - `Query`문자열로 지정해야 하는 데이터 명시
- `@QueryMap`
  ```java
  @GET("/user")
  Call<List<UserModel>> groupList(@QueryMap Map<String, String> options);
  ```
  - `Map` 객체로 `Query` 문자열 지정
- `@Body`
  ```java
  @POST("/users")
  Call<UserModel> groupList(@Body UserModel user);
  ```
  - 객체를 `request body`에 포함
  - `POST` 방식에서 사용
  - `Model`을 통해 `JSON` 문자열로 만들어 전송
  - `Model`의 객체 변수에 값이 없다면 해당 데이터는 전송되지 않음
    - 자바 기본 데이터 타입으로 선언된 변수는 디폴트 값이 대입되므로 명시적으로 값을 주지 않더라도 전송됨
- `@FormUrlEncoded`, `@Field`
  ```java
  @FormUrlEncoded
  @POST("/user")
  Call<UserModel> groupList4(@Field("first_name") String first);
  ```
  - `Model` 클래스를 이용하지 않고 개별 매개변수를 `request body`로 전송하고자 할 때 이용
  - `POST` 방식에서 사용
  - 배열이나 리스트 데이터도 이 방식으로 전송
- `@Headers`
  ```java
  @Headers({
    "Accept: application/vnd.github.v3.full+json",
    "Cache-Control:max-age=640000"
  })
  @GET("/list")
  Call<List<UserModel>> groupList();
  ```
  - HTTP 요청 헤더 지정
- `@Url`
  ```java
  @GET
  Call<UserModel> list(@Url String url);
  ```
  - `baseUrl`과 상관 없는 특정 URL 지정
