package egovframework.example.loan.web;

import java.util.List;

import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import egovframework.example.loan.domain.LoanVO;
import egovframework.example.loan.service.LoanService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;


@Controller
@RequestMapping("/loan")
@RequiredArgsConstructor
public class LoanController {

    @Resource(name = "propertiesService")
    private EgovPropertyService propertiesService;

    private final LoanService loanService;

    //대출 신청 처리
    @PostMapping("request")
    public String requestLoan(@RequestParam int bookNo,
                              RedirectAttributes redirectAttributes) {
        String memberId = SecurityContextHolder.getContext().getAuthentication().getName();
        String result = loanService.requestLoan(memberId, bookNo);

        switch (result) {
            case "SUCCESS":
                redirectAttributes.addFlashAttribute("successMsg", "대출이 신청되었습니다.");
                break;
            case "MAX_LOAN":
                redirectAttributes.addFlashAttribute("errorMsg", "대출 가능 권수(5권)를 초과했습니다.");
                break;
            case "DUPLICATE":
                redirectAttributes.addFlashAttribute("errorMsg", "이미 대출 중인 도서입니다");
                break;
            case "NO_STOCK":
                redirectAttributes.addFlashAttribute("errorMsg", "대출 가능한 재고가 없습니다.");
                break;
        }

        return "redirect:/book/detail/" + bookNo;
        
    }

    //반납 처리
    @PostMapping("/return/{loanNo}")
    public String returnLoan(@PathVariable int loanNo, 
                             RedirectAttributes redirectAttributes) {
        loanService.returnLoan(loanNo);
        redirectAttributes.addFlashAttribute("successMsg", "반납이 완료되었습니다.");
        return "redirect:/loan/myList";
    }   
    
    //내 대출 목록
    @GetMapping("/myList")
    public String myLoanList(@ModelAttribute LoanVO loanVO, Model model) {
        String memberId = SecurityContextHolder.getContext()
                            .getAuthentication().getName();
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

        model.addAttribute("loanList", loanList);
        model.addAttribute("paginationInfo", paginationInfo);
        model.addAttribute("loanVO", loanVO);

        return "loan/myList";
    }
    
}
