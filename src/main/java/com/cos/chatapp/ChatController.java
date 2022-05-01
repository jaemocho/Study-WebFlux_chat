package com.cos.chatapp;

import java.time.LocalDateTime;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RequiredArgsConstructor //final 인거 생성자 (의존성 주입)
@RestController // 데이터 리턴서버
public class ChatController {

	private final ChatRepository chatRepository;
	
	//귓솏말 할 때 사용하면 된다. 
	@CrossOrigin // java script 요청을 받게 해줌
	@GetMapping(value="/sender/{sender}/receiver/{receiver}", produces=MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Chat> getMsg(@PathVariable String sender, @PathVariable String receiver) {
		return chatRepository.mFindBySender(sender, receiver)
				.subscribeOn(Schedulers.boundedElastic());
	}
	
	
	//방번호 
	@CrossOrigin // java script 요청을 받게 해줌
	@GetMapping(value="/chat/roomNum/{roomNum}", produces=MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Chat> findByRoomNum(@PathVariable Integer roomNum) {
		return chatRepository.mFindByRoomNum(roomNum)
				.subscribeOn(Schedulers.boundedElastic());
	}
	
	
	@CrossOrigin // java script 요청을 받게 해줌
	@PostMapping("/chat")
	public Mono<Chat> setMsg(@RequestBody Chat chat){ //save 한 데이터를 보고 싶어서 return type을 Mono로 void 도 가능 
		chat.setCreateAt(LocalDateTime.now());
		return chatRepository.save(chat); //Object를 리턴하면 자동으로 JSON 으로 변환
	}
}
