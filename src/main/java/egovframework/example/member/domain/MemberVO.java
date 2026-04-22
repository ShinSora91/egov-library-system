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
    private String memberAddrDetail;
    private String memberTel;
    private String registDate;
    private String withdrawYn = "N";
    private String withdrawDate;
    private String memberRole = "USER";

    //페이징
    private int pageIndex = 1;
    private int pageUnit = 10;
    private int pageSize = 10;
    private int firstIndex = 0;
    private int lastIndex = 0;
    private int recordCountPerPage = 0;
    private String searchKeyword;
    private String searchCondition;
}
