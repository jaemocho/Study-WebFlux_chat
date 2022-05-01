package com.cos.chatapp;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "chat")
public class Chat {

	@Id
	private String id; 	//mongo db에서느 Bson 타입으로 id를 잡아주는데 여기선 string 으로 사용
	private String msg;
	private String sender;
	private String receiver; //귓속말
	private Integer roomNum; //방 번호 -- 마지막 강의에서 추가 
	
	private LocalDateTime createAt;
}
