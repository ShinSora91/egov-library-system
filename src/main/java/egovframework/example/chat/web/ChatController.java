package egovframework.example.chat.web;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.example.chat.domain.ChatVO;
import egovframework.example.chat.service.ChatService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    //채팅 페이지
    @GetMapping("")
    public String chatPage() {
        return "chat/chat";
    }
    
    //채팅 API
    @PostMapping("/send")
    @ResponseBody
    public ResponseEntity<String> send(@RequestBody ChatVO chatVO) {
        String response = chatService.chat(chatVO.getMessage());
        return ResponseEntity.ok(response);
    }   
}
