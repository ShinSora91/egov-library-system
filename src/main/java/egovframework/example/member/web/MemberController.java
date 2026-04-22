package egovframework.example.member.web;

import java.util.List;

import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.example.loan.domain.LoanVO;
import egovframework.example.loan.service.LoanService;
import egovframework.example.member.domain.MemberVO;
import egovframework.example.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
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

    @Resource(name = "propertiesService")
    private EgovPropertyService propertiesService;
    
    private final MemberService memberService;
    private final LoanService loanService;

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
    @Operation(hidden = true)
    @GetMapping("/checkId")
    @ResponseBody
    public boolean checkId(@RequestParam String memberId) {
        return memberService.isIdDuplicate(memberId);
    }
    
    //내 정보 조회 페이지
    @GetMapping("/myPage")
    public String myPage(@ModelAttribute LoanVO loanVO, Model model) {
        //현재 로그인한 사용자 아이디 가져오기
        String memberId = SecurityContextHolder.getContext().getAuthentication().getName();
        MemberVO member = memberService.getMemberById(memberId);

        //페이징  설정
        loanVO.setMemberId(memberId);
        loanVO.setPageUnit(propertiesService.getInt("pageUnit"));
        loanVO.setPageSize(propertiesService.getInt("pageSize"));

        PaginationInfo paginationInfo = new PaginationInfo();
        paginationInfo.setCurrentPageNo(loanVO.getPageIndex());
        paginationInfo.setRecordCountPerPage(loanVO.getPageUnit());
        paginationInfo.setPageSize(loanVO.getPageSize());

        loanVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
        loanVO.setLastIndex(paginationInfo.getLastRecordIndex());
        loanVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
        
        int toCnt = loanService.getMyLoanListToCnt(loanVO);
        paginationInfo.setTotalRecordCount(toCnt);

        List<LoanVO> loanList = loanService.getMyLoanList(loanVO);

        model.addAttribute("memberVO", member);
        model.addAttribute("loanList", loanList);
        model.addAttribute("paginationInfo", paginationInfo);
        model.addAttribute("loanVO", loanVO);
        return "member/myPage";
    }
    
    //회원 정보 수정 처리
    @PostMapping("/update")
    public String update(@ModelAttribute MemberVO memberVO, 
                          RedirectAttributes redirectAttributes) {
        String memberId = SecurityContextHolder.getContext().getAuthentication().getName();
        memberVO.setMemberId(memberId);
        memberService.updateMember(memberVO);
        redirectAttributes.addFlashAttribute("successMsg", "회원 정보가 수정되었습니다.");        
        return "redirect:/member/myPage";
    }
    
    //비밀번호 변경 처리
    @PostMapping("/updatePassword")
    public String updatePassword(@RequestParam String currentPw,
                                 @RequestParam String newPw,
                                 @RequestParam String newPwConfirm,
                                 RedirectAttributes redirectAttributes) {
        //새 비밀번호 확인
        if (!newPw.equals(newPwConfirm)) {
            redirectAttributes.addFlashAttribute("pwErrorMsg", "새 비밀번호가 일치하지 않습니다.");
            return "redirect:/member/myPage";
        }

        String memberId = SecurityContextHolder.getContext().getAuthentication().getName();

        boolean result = memberService.updatePassword(memberId, currentPw, newPw);
        if (!result) {
            redirectAttributes.addFlashAttribute("pwErrorMsg", "현재 비밀번호가 올바르지 않습니다.");
            return "redirect:/member/myPage";
        }
        redirectAttributes.addFlashAttribute("pwSuccessMsg", "비밀번호가 변경되었습니다.");
        return "redirect:/member/myPage";
    }
    
}
