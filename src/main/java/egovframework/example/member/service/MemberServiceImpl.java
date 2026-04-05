package egovframework.example.member.service;

import java.util.List;

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

    @Override
    public void updateMember(MemberVO memberVO) {
        memberMapper.updateMember(memberVO);
    }

    @Override
    public boolean updatePassword(String membdrId, String currentPw, String newPw) {
        //현재 비밀번호 확인
        MemberVO member = memberMapper.selectMemberById(membdrId);

        //입력한 현재 비밀번호와 DB의 암호화된 비밀번호 비교
        if (!passwordEncoder.matches(currentPw, member.getMemberPw())) {
            return false; //현재 비밀번호 틀림
        }

        //새 비밀번호 암호화 후 변경
        member.setMemberPw(passwordEncoder.encode(newPw));
        memberMapper.updatePassword(member);
        return true;
    }

    @Override
    public List<MemberVO> getMemberList(MemberVO memberVO) {
        return memberMapper.selectMemberList(memberVO);
    }

    @Override
    public int getMemberListToCnt(MemberVO memberVO) {
        return memberMapper.selectMemberListToCnt(memberVO);
    }

}
