package egovframework.example.member.service;

import egovframework.example.member.domain.MemberVO;

public interface MemberService {

    //회원가입
    void registerMember(MemberVO memberVO);

    //아이디 중복 체크 (true=중복)
    boolean isIdDuplicate(String memberId);

    //로그인용 회원 조회
    MemberVO getMemberById(String memberId);
    
}