package egovframework.example.member.web;

import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import egovframework.example.loan.service.LoanService;
import egovframework.example.member.domain.MemberVO;
import egovframework.example.member.service.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;

@Tag(name = "회원 API", description = "회원 관련 API")
@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberAPIController {

    private final MemberService memberService;

    @Resource(name = "propertiesService")
    private EgovPropertyService propertiesService;

    //회원가입
    @Operation(summary = "회원가입")
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody MemberVO memberVO) {

        if (memberService.isIdDuplicate(memberVO.getMemberId())) {
            return ResponseEntity.badRequest().body("이미 사용 중인 아이디입니다.");
        }

        memberService.registerMember(memberVO);
        return ResponseEntity.ok("회원가입 완료");
    }

    //회원 정보 수정
    @Operation(summary = "회원 정보 수정")
    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody MemberVO memberVO) {

        String memberId = SecurityContextHolder.getContext().getAuthentication().getName();
        memberVO.setMemberId(memberId);

        memberService.updateMember(memberVO);
        return ResponseEntity.ok("수정 완료");
    }

    //아이디 중복 체크
    @Operation(summary = "아이디 중복 체크")
    @GetMapping("/check")
    public boolean check(@RequestParam String memberId) {
        return memberService.isIdDuplicate(memberId);
    }

    //내 정보 조회
    @Operation(summary = "내 정보 조회")
    @GetMapping("/me")
    public ResponseEntity<MemberVO> getMyInfo() {

        String memberId = SecurityContextHolder.getContext().getAuthentication().getName();
        MemberVO member = memberService.getMemberById(memberId);

        return ResponseEntity.ok(member);
    }

    //비밀번호 변경
    @Operation(summary = "비밀번호 변경")
    @PostMapping("/password")
    public ResponseEntity<?> updatePassword(
            @RequestParam String currentPw,
            @RequestParam String newPw,
            @RequestParam String newPwConfirm) {

        if (!newPw.equals(newPwConfirm)) {
            return ResponseEntity.badRequest().body("새 비밀번호가 일치하지 않습니다.");
        }

        String memberId = SecurityContextHolder.getContext().getAuthentication().getName();

        boolean result = memberService.updatePassword(memberId, currentPw, newPw);

        if (!result) {
            return ResponseEntity.badRequest().body("현재 비밀번호가 올바르지 않습니다.");
        }

        return ResponseEntity.ok("비밀번호 변경 완료");
    }
}