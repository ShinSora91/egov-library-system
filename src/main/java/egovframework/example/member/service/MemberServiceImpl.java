package egovframework.example.member.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import egovframework.example.member.domain.MemberVO;
import egovframework.example.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{
    
    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder; //SecurityConfig에서 Bean으로 등록한 것

    @Override
    public void registerMember(MemberVO memberVO) {
        //비밀번호를 BCrypt로 암호화해서 덮어씌움
        //ex) "1234" -> "$2a$10$xyz..." 형태로 변환
        String encodedPw = passwordEncoder.encode(memberVO.getMemberPw());
        memberVO.setMemberPw(encodedPw);
        memberMapper.insertMember(memberVO); //암호화된 상태로 DB 저장
    }

    @Override
    public boolean isIdDuplicate(String memberId) {
        //Mapper가 COUNT(*) 결과를 int로 반환 -> 0보다 크면 중복
        return memberMapper.checkMemberId(memberId) > 0;
    }
    
    @Override
    public MemberVO getMemberById(String memberId) {
        return memberMapper.selectMemberById(memberId);
    }

}
