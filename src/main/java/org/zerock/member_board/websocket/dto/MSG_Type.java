package org.zerock.member_board.websocket.dto;

public enum MSG_Type{

    SUB_RES,  //서버->클라, 구독에 대한 결과 브로드캐스트
    CHAT_REQ, //클라->서버, 채팅 요청
    CHAT_RES, //서버->클라, 채팅 요청에 대한 것을 모든 유저에게 브로드캐스트
    JOIN_REQ, //클라->서버, 요청 수락
    ROOM_OPEN,//서버->클라, 방이 열림을 알림
    OPEN_RES, //자식 소켓으로 열어서 입장했음을 알린다
    ROOM_CHAT,//자식<->서버, 채팅창 대화 내용
    USER_OUT,
    SUB_REQ
}