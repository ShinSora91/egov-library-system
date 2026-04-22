package egovframework.example.loan.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import egovframework.example.commonMethod.PagingCommon;
import egovframework.example.loan.domain.LoanVO;
import egovframework.example.loan.service.LoanService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;


@Tag(name = "대출/반납 API", description = "대출 및 반납 관련 API")
@RestController
@RequestMapping("/api/loan")
@RequiredArgsConstructor
public class LoanAPIController {

    private final LoanService loanService;

    @Resource(name = "propertiesService")
    private EgovPropertyService propertiesService;

    //대출 신청
    @Operation(summary = "도서 대출 신청")
    @PostMapping("/request")
    public ResponseEntity<?> requestLoan(@RequestParam int bookNo) {

        String memberId = SecurityContextHolder.getContext().getAuthentication().getName();

        String result = loanService.requestLoan(memberId, bookNo);

        switch (result) {
            case "SUCCESS":
                return ResponseEntity.ok("대출 완료");
            case "MAX_LOAN":
                return ResponseEntity.badRequest().body("대출 가능 권수 초과");
            case "DUPLICATE":
                return ResponseEntity.badRequest().body("이미 대출 중");
            case "NO_STOCK":
                return ResponseEntity.badRequest().body("재고 없음");
            default:
                return ResponseEntity.badRequest().body("오류 발생");
        }
    }

    //반납 처리
    @Operation(summary = "도서 반납")
    @PostMapping("/return/{loanNo}")
    public ResponseEntity<?> returnLoan(@PathVariable int loanNo) {

        loanService.returnLoan(loanNo);
        return ResponseEntity.ok("반납 완료");
    }

    //내 대출 목록
    @Operation(summary = "내 대출 목록 조회")
    @GetMapping("/my")
    public ResponseEntity<?> myLoans(@ModelAttribute LoanVO loanVO) {

        String memberId = SecurityContextHolder.getContext().getAuthentication().getName();
        loanVO.setMemberId(memberId);

        //페이징 설정
        PagingCommon.setPaging(loanVO, propertiesService);

        PaginationInfo paginationInfo = new PaginationInfo();
        paginationInfo.setCurrentPageNo(loanVO.getPageIndex());
        paginationInfo.setRecordCountPerPage(loanVO.getPageUnit());
        paginationInfo.setPageSize(loanVO.getPageSize());

        loanVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
        loanVO.setLastIndex(paginationInfo.getLastRecordIndex());
        loanVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

        int totalCount = loanService.getMyLoanListToCnt(loanVO);
        paginationInfo.setTotalRecordCount(totalCount);

        List<LoanVO> loanList = loanService.getMyLoanList(loanVO);

        Map<String, Object> result = new HashMap<>();
        result.put("list", loanList);
        result.put("paginationInfo", paginationInfo);
        result.put("totalCount", totalCount);

        return ResponseEntity.ok(result);
    }
}