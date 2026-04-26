package egovframework.example.chat.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatVO {
    private String message; //사용자 입력
    private String response; //답변
}
