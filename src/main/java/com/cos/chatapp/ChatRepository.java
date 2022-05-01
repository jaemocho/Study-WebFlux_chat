package com.cos.chatapp;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;

import reactor.core.publisher.Flux;

public interface ChatRepository extends ReactiveMongoRepository<Chat, String>{ // collection은 chat, id는 string type

	@Tailable //커서를 안닫고 계속 유지한다. 찾고나서 닫지 않고 유지, 데이터가 flux 로 흘러들어온다 
	@Query("{sender:?0,receiver:?1}") // sender, receiver를 찾는 mongodb 문법
	Flux<Chat> mFindBySender(String sender, String receiver);  //Flux(흐름) response를 유지하면서 데이터를 계속 흘려보낼 수 있다.
	
	@Tailable //커서를 안닫고 계속 유지한다. 찾고나서 닫지 않고 유지, 데이터가 flux 로 흘러들어온다 
	@Query("{roomNum:?0}") // 같은 방에서 채팅하는 사람들을 뽑는다 
	Flux<Chat> mFindByRoomNum(Integer roomNum);  //Flux(흐름) response를 유지하면서 데이터를 계속 흘려보낼 수 있다.
}
