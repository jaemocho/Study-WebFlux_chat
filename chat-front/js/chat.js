//로그인 시스템 대신 임시 방편 
let username = prompt("아이디를 입력하세요");
let roomNum = prompt("채팅방번호를 입력하세요");

document.querySelector("#username").innerHTML = username;

//flux stream 연결 SSE연결하기
const eventSource = new EventSource(`http://localhost:8080/chat/roomNum/${roomNum}`);

eventSource.onmessage = (event) => {
	//console.log(1,event);
	const data = JSON.parse(event.data);
	//console.log(2,data);
	if(data.sender === username){ //로그인한 유저가 보낸 메시지
		//파란박스 
		initMyMessage(data);
	}else{
		//회색박스
		initYourMessage(data);
	}

	
}

function getSendMsgBox(data){
	return `<div class="sent_msg">
	<p>${data.msg}</p>
	<span class="time_date"> ${data.createAt} / ${data.sender}</span>
  </div>`;
}

function receiveMsgBox(data){
	return `<div class="received_withd_msg">
	<p>${data.msg}</p>
	<span class="time_date"> ${data.createAt} / ${data.sender}</span>
  </div>`;
}

//최초 초기화될 때 3건이 있으면 3건을 다가져온다.
//addMessage() 함수 호출 시 DB에 insert가 되고, 그 데이터가 자동으로 흘러들어온다(SSE)
//파란박스만들기 
function initMyMessage(data){
	let chatBox = document.querySelector("#chat-box");
	
	let sendBox = document.createElement("div");
	sendBox.className ="outgoing_msg";
	sendBox.innerHTML =getSendMsgBox(data);
	chatBox.append(sendBox);

	//스크롤 밑으로
	document.documentElement.scrollTop = document.body.scrollHeight;
	
}

//회색박스 만들기
function initYourMessage(data){
	let chatBox = document.querySelector("#chat-box");
	
	let receivedBox = document.createElement("div");
	receivedBox.className ="received_msg";
	receivedBox.innerHTML =receiveMsgBox(data);
	chatBox.append(receivedBox);
	
	//스크롤 밑으로
	document.documentElement.scrollTop = document.body.scrollHeight;
}

// AJAX로 채팅 메시지를 전송
async function addMessage(){
	// let chatBox = document.querySelector("#chat-box");
	let msgInput = document.querySelector("#chat-outgoing-msg");
	
	//JS 생성 
	let chat={
		sender:username,
		// receiver:"ssar",
		roomNum:roomNum,
		msg:msgInput.value
	};

	//return 을 받기위해 await, await에서 block 그래서 함수를 async 로 변경(다른것들이 모두 동작 안하기 때문에 )
	fetch("http://localhost:8080/chat", {
		method:"post", //http post 메서드(새로운 데이터를 write)
		body:JSON.stringify(chat), // JS -> JSON 으로 변경
		headers:{
			"Content-Type":"application/json; charset=utf-8"
		}

	});
	
	msgInput.value = "";
}

document.querySelector("#chat-send").addEventListener("click",()=>{
	//alert("클릭됨");
	addMessage();

});


document.querySelector("#chat-outgoing-msg").addEventListener("keydown",(e)=>{
	if(e.keyCode === 13) {

		addMessage();
	}
	//console.log(e.keyCode);
});