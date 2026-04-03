package egovframework.example.member.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MemberVO {
    private String memberId;
    private String memberPw;
    private String memberName;
    private String memberAddr;
    private String memberTel;
    private String registDate;
    private String withdrawYn = "N";
    private String withdrawDate;
    private String memberRole = "USER";
}
