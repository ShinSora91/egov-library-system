package egovframework.example.member.service;

import egovframework.example.member.domain.MemberVO;

public interface MemberService {

    //회원가입
    void registerMember(MemberVO memberVO);

    //아이디 중복 체크 (true=중복)
    boolean isIdDuplicate(String memberId);

    //로그인용 회원 조회
    MemberVO getMemberById(String memberId);

    //회원 정보 수정
    void updateMember(MemberVO memberVO);

    //비밀번호 변경 (현재 비밀번호 확인 후 변경)
    //currentPw: 현재 비밀번호, newPw: 새 비밀번호
    boolean updatePassword(String membdrId, String currentPw, String newPw);
    
}