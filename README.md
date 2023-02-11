# 실습을 위한 개발 환경 세팅
* https://github.com/slipp/web-application-server 프로젝트를 자신의 계정으로 Fork한다. Github 우측 상단의 Fork 버튼을 클릭하면 자신의 계정으로 Fork된다.
* Fork한 프로젝트를 eclipse 또는 터미널에서 clone 한다.
* Fork한 프로젝트를 eclipse로 import한 후에 Maven 빌드 도구를 활용해 eclipse 프로젝트로 변환한다.(mvn eclipse:clean eclipse:eclipse)
* 빌드가 성공하면 반드시 refresh(fn + f5)를 실행해야 한다.

# 웹 서버 시작 및 테스트
* webserver.WebServer 는 사용자의 요청을 받아 RequestHandler에 작업을 위임하는 클래스이다.
* 사용자 요청에 대한 모든 처리는 RequestHandler 클래스의 run() 메서드가 담당한다.
* WebServer를 실행한 후 브라우저에서 http://localhost:8080으로 접속해 "Hello World" 메시지가 출력되는지 확인한다.

# 각 요구사항별 학습 내용 정리
* 구현 단계에서는 각 요구사항을 구현하는데 집중한다. 
* 구현을 완료한 후 구현 과정에서 새롭게 알게된 내용, 궁금한 내용을 기록한다.
* 각 요구사항을 구현하는 것이 중요한 것이 아니라 구현 과정을 통해 학습한 내용을 인식하는 것이 배움에 중요하다. 

### 요구사항 1 - http://localhost:8080/index.html로 접속시 응답
* request header는 객체로 관리하기 위해 클래스를 만들어 주었다.
  * header의 구조는 HTTP 프로토콜이 규격인 이상 변하지 않을 확률이 높으며 header에 정보를 추가하기 위해선 key : value라는 고정된 형식으로 추가되기 때문에 자바의 map으로 관리하면 될 것 같다.
  * 또한 header를 만들며 나오는 모든 에러는 종류에 상관없이 header가 만들어지지 않았음을 의미하기 때문에 RuntimeException으로 해결해주었다. RuntimeException으로 처리될 경우 사용자는 응답 시간을 기다리다가 자동적으로 설정한 시간에 맞춰 conneciton을 끊게 된다.

### 요구사항 2 - get 방식으로 회원가입
* User는 header의 쿼리스트링을 통해 만들 수 있다.
  * 여기서 User를 만드는 객체는 User 스스로이다. Header로 부터 message를 받아 처리한다.
    * DDD적으로 설계하기 위한 연습이라고 생각한다.

### 요구사항 3 - post 방식으로 회원가입
* POST 방식으로 Body를 전달할때, 왜 Content-Length가 필요한가?
  * Content-Length : message의 크기를 바이트 단위로 나타낸다. 이는 인코딩에 상관없이 크기를 표현할 수 있다.

### 요구사항 4 - redirect 방식으로 이동
* 301 redirection은 페이지가 영구적으로 옮겨졌을때 사용
  * 이전 페이지의 정보를 보관하지 않음
  * 새로운 URL, 사용하지 않는 페이지를 이동시키는데 사용
* 302 redirection은 페이지가 일시적으로 옮겨졌을때 사용
  * 이전 페이지의 정보를 가지고 있을 수 있음
  * 기존 URL에 컨텐츠 URL을 추가할때 사용하면 유용

### 요구사항 5 - cookie
* 

### 요구사항 6 - stylesheet 적용
* 

### heroku 서버에 배포 후
* 