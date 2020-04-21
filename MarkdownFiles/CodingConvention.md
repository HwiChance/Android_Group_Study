# Android Coding Convention
안드로이드 코드 작성 규칙을 정리한 문서
## 목록
[1.](#1-자바-언어-규칙) 자바 언어 규칙
[2.](#2-자바-스타일-규칙) 자바 스타일 규칙

## 1. 자바 언어 규칙
안드로이드는 아래에 설명되는 추가 규칙을 포함하는 표준 자바 코딩 규칙을 따름
#### 예외를 무시하지 말라
```java
void setServerPort(String value) {
  try {
    serverPort = Integer.parseInt(value);
  } catch (NumberFormatException e) { }
}
```
- 위와 같이 예외 처리를 하지 않으면 추후에 문제가 생길 수 있음
- 예외를 무시하지 않기
  - 메서드 호출자에게 예외를 반환
    ```java
    void setServerPort(String value) throws NumberFormatException {
      serverPort = Integer.parseInt(value);
    }
    ```
  - 추상화 계층에 적절한 새 예외를 반환
    ```java
    void setServerPort(String value) throws ConfigurationException {
      try {
        serverPort = Integer.parseInt(value);
      } catch (NumberFormatException e) {
        throw new ConfigurationException("Port " + value + " is not valid.");
      }
    }
    ```
  - `catch {}`에서 오류를 처리
    ```java
    void setServerPort(String value) {
      try {
        serverPort = Integer.parseInt(value);
      } catch (NumberFormatException e) {
        serverPort = 80;
      }
    }
    ```
  - 예외를 캐치하고 `RuntimeException`의 새 인스턴스를 반환
    ```java
    void setServerPort(String value) {
      try {
        serverPort = Integer.parseInt(value);
      } catch (NumberFormatException e) {
        throw new RuntimeException("Port " + value + " is not valid, ", e);
      }
    }
    ```
    - 이 방법은 위험하므로 이 오류가 발생했을 때의 올바른 조치가 비정상 종료라고 확신하는 경우에만 사용
  - 예외를 무시하는 대신 합당한 이유를 주석으로 남김
    ```java
    void setServerPort(String value) {
      try {
        serverPort = Integer.parseInt(value);
      } catch (NumberFormatException e) {
        // Method is documented to just ignore invalid user input.
        // serverPort will just be unchanged.
      }
    }
    ```

#### 일반 예외를 캐치하지 말라
```java
try {
  someComplicatedIOFunction();        // may throw IOException
  someComplicatedParsingFunction();   // may throw ParsingException
  someComplicatedSecurityFunction();  // may throw SecurityException
  // phew, made it all the way
} catch (Exception e) {               // I'll just catch all exceptions
  handleError();                      // with one generic handler!
}
```
- 거의 모든 경우에 일반적인 `Exception` 또는 `Throwable`을 캐치하는 것은 부적절함
- 전혀 예상치 못했던 예외가 앱 수준의 오류 처리에 휘말리게 될 수 있기 때문
```java
try {
  ...
} catch (ClassNotFoundException | NoSuchMethodException e) {
  ...
}
```
- 위와 같은 방식으로 각 예외를 다중 캐치 블록의 일부로 따로 캐치하는 것이 좋음

#### Finalizer를 사용하지 말라
- Finalizer: 객체가 가비지로 수집될 때 코드 청크를 실행할 수 있는 방법 중 하나
- Finalizer가 필요한 경우 `close()` 메소드 또는 유사 메소드를 정의하고 객체 내에서 종료 상태를 따로 관리하는 것이 좋음

#### Import문을 축약하지 말라
```java
import foo.*;   // import문의 개수가 줄어들 수 있음
import foo.Bar; // 어떤 클래스가 사용되고 있는지 명확히 알려주며 코드를 더 쉽게 읽을 수 있음
```

## 2. 자바 스타일 규칙
#### Javadoc 표준 주석을 사용하라
```java
/*
 * Copyright 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.internal.foo;

import android.os.Blah;
import android.view.Yada;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Does X and Y and provides an abstraction for Z.
 */

public class Foo {
  //...
}
```
모든 파일은 저작권 고지 - `package` - `import` - `class / interface` 순으로 작성되어야 함
```Java
/** Returns the correctly rounded positive square root of a double value. */

static double sqrt(double a) {
  //...
}
```
```Java
/**
 * Constructs a new String by converting the specified array of
 * bytes using the platform's default character encoding.
 */
public String(byte[] bytes) {
  //...
}
```
모든 class 및 사소하지 않은 public 메소드에는 class와 메소드의 역할을 설명하는 문장이 포함된 Javadoc 주석이 이었야 하며 이 주석은 3인칭 서술형 동사로 시작해야 함

#### 메소드는 짧게 작성하라
메소드가 40행 정도를 초가하는 경우에는 프로그램의 구조가 손상되지 않는 선에서 이를 분할할 수 있는지 생각해봐야 함

#### 표준 위치에 필드 정의
필드는 파일 상단이나 필드를 사용하는 메소드 바로 앞에 정의해야 함

#### 변수 스코프를 제한하라
- 변수를 사용되기 직전에 선언함으로써 가독성을 높이고 예상치 못한 위치에서 사용되는 것을 방지해야 함
- 변수를 선언과 동시에 초기화할 수 있는 위치에 선언해야 함
- `try-catch`구문은 예외
- 루프 구문 자체에 루프 변수를 선언
```Java
for (int i = 0; i < n; i++) {
  doSomething(i);
}

for (Iterator i = c.iterator(); i.hasNext(); ) {
  doSomethingElse(i.next());
}
```

#### import 구문 규칙
- import 순서
1. Android
2. 타사(`com`, `junit`, `net`, `org`)
3. `java` 및 `javax`
- 각 그룹 내에서 알파벳 순서여야 하며 소문자 앞에 대문자가 위치
- 각 주요 그룹 간에 빈 행으로 구분

#### 들여쓰기에 tab 대신 공백 사용
- Google은 블록 및 never 탭에 (4)개의 공백 들여쓰기를 사용함
- Google은 함수 호출 및 할당을 비롯한 행 래핑에 (8)개의 공백 들여쓰기를 사용함
  ```java
  // 권장
  Instrument i =
          someLongExpression(that, wouldNotFit, on, one, line);

  // 비권장
  Instrument i =
      someLongExpression(that, wouldNotFit, on, one, line);
  ```

#### 필드 이름 지정 규칙
- public 또는 static이 아닌 필드 이름은 `m`으로 시작
- static 필드 이름은 `s`로 시작
- 다른 필드는 소문자로 시작
- public static final 필드(상수)는 모두 대문자로 쓰고 단어 사이에는 밑줄로 처리
```java
public class MyClass {
  public static final int SOME_CONSTANT = 42;
  public int publicField;
  private static MyClass sSingleton;
  int mPackagePrivate;
  private int mPrivate;
  protected int mProtected;
}
```

#### 표준 중괄호 스타일 사용
중괄호를 별도의 행이 아닌 그 앞에 있는 코드와 같은 행에 둠
```java
class MyClass {
  int func() {
    if (something) {
      // ...
    } else if (somethingElse) {
      // ...
    } else {
      // ...
    }
  }
}
```
다음 형식은 허용됨
```java
if (condition) {
  body();
}

또는

if(condition) body();
```
다음 형식은 허용되지 않음
```java
if (condition)
  body();
```

#### 행 길이 제한
URL, package, import 문을 제외한 코드의 각 행은 최대 100자를 초과할 수 없음

#### 표준 자바 주석 사용
- Annotation은 알파벳 순서로 한 줄에 하나씩 선언
- `@Deprecated`: 당장은 사용 가능하더라도 향후 제거 예정인 메소드에 사용하는 Annotation으로 이 Annotation을 사용하려면 대신 사용할 수 있는 방법에 대해서 Javadoc으로 설명해야 함
- `@Override`:상위 클래스의 메소드를 재정의 할 때나 인터페이스를 구현할 때 무조건 빼지 않고 기술해야 함
- `@SuppressWarnings`: Warning 메시지를 제거할 수 없을 때 사용되는 Annotation으로 이 Annotation을 사용할 때는 TODO 주석으로 그 이유를 설명해야 함

#### 약어를 단어로 취급
|좋음|나쁨|
|--|--|
|XmlHttpRequest|XMLHTTPRequest|
|getCustomerId|getCustomerID|
|class Html|class HTML|
|String url|String URL|
|long id|long ID|

#### TODO 주석 사용
일시적이거나 단기 솔루션이거나 충분히 훌륭하지만 완벽하지는 않은 코드에는 `TODO` 주석을 사용
```Java
// TODO: Remove this code after the UrlTable2 has been checked in.
```
주석에는 모두 대문자인 TODO 문자열과 그 뒤에 콜론을 포함해야 함

#### Docs
- [AOSP 자바 코드 스타일 가이드](https://source.android.com/setup/contribute/code-style#javatests-style-rules)
- [Google 자바 스타일 가이드](https://google.github.io/styleguide/javaguide.html#s7-javadoc)
- [Javadoc 주석 작성 방법](https://www.oracle.com/technetwork/java/javase/documentation/index-137868.html)
