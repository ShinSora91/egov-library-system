package egovframework.example.config.security;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import egovframework.example.member.domain.MemberVO;
import egovframework.example.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService{
    
    private final MemberMapper memberMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("loadUserByUsername 호출: " + username);
        MemberVO member = memberMapper.selectMemberById(username);
        System.out.println("조회된 회원: " + member);

        if (member == null) {
            throw new UsernameNotFoundException("존재하지 않는 아이디입니다: " + username);
        }
        if ("Y".equals(member.getWithdrawYn())) {
            throw new UsernameNotFoundException("탈퇴한 회원입니다: " + username);
        }

        return User.builder()
                .username(member.getMemberId())
                .password(member.getMemberPw())
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_" + member.getMemberRole())))
                .build();
    }

}
