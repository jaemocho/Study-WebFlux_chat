# Study-WebFlux_chat
chat program

채팅 서버

프론트 연결-채팅하는 프로그램

# STS 생성 lib
Spring Reactive Web(WebFlux)

Spring Boot DevTools

Spring Data Reactive MongoDB

Lombok

java version 은 11 권장



# 몽고DB 설치 URL 
https://www.mongodb.com/try/download/community

path에 아래 내용 추가 
C:\Program Files\MongoDB\Server\5.0\bin

cmd 창에서 mongo 하면 db login 

> show dbs;  //db list 조회

admin   0.000GB

chatdb  0.000GB

config  0.000GB

local   0.000GB

> use chatdb; //chatdb로 변경

switched to db chatdb

> show collections //collection 조회

chat

> db.chat.find(); // data 조회

{ "_id" : ObjectId("626e1bfde43e7c2ae0a0882f"), "sender" : "ssar", "receiver" : "cos", "createAt" : ISODate("2022-05-01T05:34:53.420Z"), "_class" : "com.cos.chatapp.Chat" }

> db.chat.find().pretty(); // data 조회 정렬

{

        "_id" : ObjectId("626e1bfde43e7c2ae0a0882f"),
        
        "sender" : "ssar",
        
        "receiver" : "cos",
        
        "createAt" : ISODate("2022-05-01T05:34:53.420Z"),
        
        "_class" : "com.cos.chatapp.Chat"

}

//@Tailable option일 때는 db size를 늘려줘야 정상 동작된다(버퍼 필요)

> db.runCommand({convertToCapped: 'chat', size: 8192});  // db buffer size 변경

{ "ok" : 1 }


# RDB vs MongoDB

> RDB

   1. 데이터 중복X

   2. 데이터 변경이 일어날 때 필요한 곳만 수정(참조하는 곳은 변경 필요 X)

> MongoDB(NoSQL)
   
   1. 컬렉션이 json 형태로 생겼다. 
   
   2. 데이터 중복을 허용 (Board에 key만 들어가는 RDB와 달리 user의 정보가 중복되어 들어간다 )
   
   3. 장점 : 데이터찾을 때 퍼포먼스가 좋다
   
   4. 중복데이터들을 다 넣어줘야하기 때문에 insert할 때 느리지만
   
   5. 찾을 땐 다른 곳을 참조하지 않아 퍼포먼스가 좋다. 
   
   6. 데이터 일관성이 없다. username이 변경되었을 때 Board에 업데이트가 어렵다. 

> MongoDB Data 형태 (RDB와 다르게 Foreign Key같은걸로 연결되어 있지 않고 중복 데이터가 들어간다 - Board에 보면 user 정보가 다 있다.)
  
  user: [
   
   {id:1, username:ssar, phone:01022223333},
   
   {id:2, username:ssar, phone:01022224444},
 
   ]


  Board:[
   
   {id:1, title:제목1, content:내용1, username:ssar, phone:01022223333},

  ]

# Spring Web(servlet) vs Spring Reactive Web(SSE)

> 일반적인 스프링 (서블릿 기반)

   사용자가 request 할때마다 스레드 만들어진다
  
   요청이 완료될 때 까지 thread 점유
   
   요청이 많으면 thread도 많아지고 time slicing 간 context switching이 thread 양만큼 일어나서 성능에 영향이 있다. 
   
> 스프링 5.0 (비동기 서버)
  
  요청이 발생하면 서버가 처리할 수 있는 부분 진행 후 
  
  다른 곳(db or 다른 system)에 요청한 내용을 대기열에 넣어놓고 요청을 받을 수 있는 상태로 돌입한다. 
  
  중간중간 시간이 빌 때(요청이 없을 때) 대기열에 들어온 응답을 전달해준다. Thread 하나로 동작 

  http 대신 sse protocal 사용 

  http: 서버에 요청 -> 서버가 데이터 수집 -> 클라이언트에 응답

  sse: 서버에 요청 -> 서버가 현재 데이터 클라이언트에 응답 -> 연결 유지 
      -> 서버에 데이터가 들어올 때마다 클라이언트에 응답 



# 귓속말 기능 테스트

> 호출

 http://localhost:8080/sender/ssar/receiver/cos/

> 응답
  
  data:{"id":"626e1d0ae43e7c2ae0a08832","msg":"ddd","sender":"ssar","receiver":"cos","createAt":"2022-05-01T14:39:22.273"}
  
  데이터 input 시 추가응답

  data:{"id":"626e1d0fe43e7c2ae0a08833","msg":"안녕","sender":"ssar","receiver":"cos","createAt":"2022-05-01T14:39:27.121"}



# TEST용 Front 
 
 IDE - Visual Studio Code(chat-front 폴더)

 html/js로 구성

 Live Server 사용 

 Visual Studio Code 오른쪽 하단의 상태표시줄 같은 곳에 "Go Live" button

# HTTP vs WEB Socket vs SSE 

> http protocol

  클라이언트가 서버로 data 요청 (GET 방식) 
  
  서버는 클라이언트로 데이터 응답 
  
  connection 종료(stateless)

  채팅이 업데이트 되면 다시 데이터를 요청해서 받아야 한다. 
  
  1초마다(일정시간마다) 계속 request (polling 방식 사용)

> web socket 
  
  클라이언트가 연결 시 양방향(클라이언트<->서버) 연결 후 유지(stateful)

  다른쪽에서 보낸 채팅이 업데이트 되면 서버가 받아서 목적지에 push 

> SSE protocol

  클라이언트가 데이터(MongoDB)를 요청 
  
  @Tailable로 되어있는 실시간 MongoDB가 데이터를 서버에 전달해주고 
  
  서버가 flux(SSE protocol)로 클라이언트에 전달

  web socket 은 서버<->클라이언트 양쪽 연결 

  SSE protocol은 서버 -> 클라이언트 response 만 연결


# 테스트 

> Front (Visual Studio Code에서 Live Server 구동)
  
  http://127.0.0.1:5500/chat.html
  
  아이디 : user1, 방번호 100
  아이디 : user2, 방번호 100
  아이디 : user3, 방번호 100
  
> back-end  

  spring boot server 구동 (jar or in sts)
  
  
  
