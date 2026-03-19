package egovframework.example.member.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.example.member.domain.MemberVO;
import egovframework.example.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;



@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    //로그인 페이지
    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) 
                                String error, Model model) {

        if (error != null) {
            model.addAttribute("errorMsg", "아이디 또는 비밀번호가 올바르지 않습니다.");
        }
        return "member/login";
    }

    //회원가입 페이지
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("memberVO", new MemberVO());
        return "member/register";
    }
    
    //회원가입 처리
    @PostMapping("/register")
    public String register(@ModelAttribute MemberVO memberVO,
                            RedirectAttributes redirectAttributes) {
        //아이디 중복 체크
        if (memberService.isIdDuplicate(memberVO.getMemberId())) {
            redirectAttributes.addFlashAttribute("errorMsg", "이미 사용 중인 아이디입니다.");
            return "redirect:/member/register";
        }

        memberService.registerMember(memberVO);
        redirectAttributes.addFlashAttribute("errorMsg", "회원가입이 완료되었습니다.");
        return "redirect:/member/login";
    }

    //아이디 중복 체크 (AJAX)
    @GetMapping("/checkId")
    @ResponseBody
    public boolean checkId(@RequestParam String memberId) {
        return memberService.isIdDuplicate(memberId);
    }
    
    
}
