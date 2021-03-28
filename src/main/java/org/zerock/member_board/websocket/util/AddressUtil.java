package org.zerock.member_board.websocket.util;


public class AddressUtil {

    static String roomSendAdd = "/cli/room/"; //뒤에는 방 ID
    static String broadSendAdd = "/cli/broad/";

    public static String getRoomSendAdd()
    {
        return roomSendAdd;
    }

    public static String getBroadSendAdd()
    {
        return broadSendAdd;
    }
}
