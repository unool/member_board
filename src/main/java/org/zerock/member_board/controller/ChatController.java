package org.zerock.member_board.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zerock.member_board.dto.ChatRoomDTO;
import org.zerock.member_board.service.util.MemberHandler;


@RequestMapping("/chat")
@Controller
public class ChatController {

    @GetMapping("/chatroom")
    public String chatroom(ChatRoomDTO chatRoomDTO, Model model){ //새창을 띄울때로 바로 Param을 보내는게 안됨.

        String myEmail = MemberHandler.GetMemberEmail();
        chatRoomDTO.setMyEmail(myEmail);
        model.addAttribute("dto", chatRoomDTO);
        return "/chat/chatroom";
    }
}
