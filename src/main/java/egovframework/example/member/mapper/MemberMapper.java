package egovframework.example.member.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.egovframe.rte.psl.dataaccess.mapper.EgovMapper;

import egovframework.example.member.domain.MemberVO;

@EgovMapper
public interface MemberMapper {
    
    //회원가입
    void insertMember(MemberVO memberVO);

    //아이디 중복 체크
    int checkMemberId(@Param("memberId") String memberId);

    //로그인용 회원 조회
    MemberVO selectMemberById(@Param("memberId") String memberId);

    //회원 정보 수정
    void updateMember(MemberVO memberVO);

    //비밀번호 변경
    void updatePassword(MemberVO memberVO);

    //회원 목록 조회
    List<MemberVO> selectMemberList(MemberVO memberVO);

    //회원 총 개수 조회
    int selectMemberListToCnt(MemberVO memberVO);
}
