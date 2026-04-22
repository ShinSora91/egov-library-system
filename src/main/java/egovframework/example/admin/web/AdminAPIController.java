package egovframework.example.admin.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import egovframework.example.book.domain.BookVO;
import egovframework.example.book.service.BookService;
import egovframework.example.commonMethod.PagingCommon;
import egovframework.example.loan.domain.LoanVO;
import egovframework.example.loan.service.LoanService;
import egovframework.example.member.domain.MemberVO;
import egovframework.example.member.service.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;


@Tag(name = "관리자 API", description = "관리자 관련 API")
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminAPIController {

    private final BookService bookService;
    private final MemberService memberService;
    private final LoanService loanService;

    @Resource(name = "propertiesService")
    private EgovPropertyService propertiesService;

    //회원 목록 조회
    @Operation(summary = "회원 목록 조회")
    @GetMapping("/members")
    public ResponseEntity<?> memberList(@ModelAttribute MemberVO memberVO) {

        PagingCommon.setPaging(memberVO, propertiesService);

        PaginationInfo paginationInfo = new PaginationInfo();
        paginationInfo.setCurrentPageNo(memberVO.getPageIndex());
        paginationInfo.setRecordCountPerPage(memberVO.getPageUnit());
        paginationInfo.setPageSize(memberVO.getPageSize());

        memberVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
        memberVO.setLastIndex(paginationInfo.getLastRecordIndex());
        memberVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

        int totalCount = memberService.getMemberListToCnt(memberVO);
        paginationInfo.setTotalRecordCount(totalCount);

        List<MemberVO> list = memberService.getMemberList(memberVO);

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("paginationInfo", paginationInfo);
        result.put("totalCount", totalCount);

        return ResponseEntity.ok(result);
    }

    //도서 목록 조회
    @Operation(summary = "도서 목록 조회")
    @GetMapping("/books")
    public ResponseEntity<?> bookList(@ModelAttribute BookVO bookVO) {

        PagingCommon.setPaging(bookVO, propertiesService);

        PaginationInfo paginationInfo = new PaginationInfo();
        paginationInfo.setCurrentPageNo(bookVO.getPageIndex());
        paginationInfo.setRecordCountPerPage(bookVO.getPageUnit());
        paginationInfo.setPageSize(bookVO.getPageSize());

        bookVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
        bookVO.setLastIndex(paginationInfo.getLastRecordIndex());
        bookVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

        int totalCount = bookService.getBookListToCnt(bookVO);
        paginationInfo.setTotalRecordCount(totalCount);

        List<BookVO> list = bookService.getBookList(bookVO);

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("paginationInfo", paginationInfo);
        result.put("totalCount", totalCount);

        return ResponseEntity.ok(result);
    }

    //도서 등록
    @Operation(summary = "도서 등록")
    @PostMapping("/books")
    public ResponseEntity<?> registerBook(@RequestBody BookVO bookVO) {
        bookService.registerBook(bookVO);
        return ResponseEntity.ok("등록 완료");
    }

    //도서 수정
    @Operation(summary = "도서 수정")
    @PutMapping("/books")
    public ResponseEntity<?> updateBook(@RequestBody BookVO bookVO) {
        bookService.updateBook(bookVO);
        bookService.updateBookStock(bookVO);
        return ResponseEntity.ok("수정 완료");
    }

    //도서 삭제
    @Operation(summary = "도서 삭제")
    @DeleteMapping("/books/{bookNo}")
    public ResponseEntity<?> deleteBook(@PathVariable int bookNo) {
        bookService.deleteBook(bookNo);
        return ResponseEntity.ok("삭제 완료");
    }

    //전체 대출 목록 조회
    @Operation(summary = "전체 대출 목록")
    @GetMapping("/loans")
    public ResponseEntity<?> loanList(@ModelAttribute LoanVO loanVO) {

        PagingCommon.setPaging(loanVO, propertiesService);

        PaginationInfo paginationInfo = new PaginationInfo();
        paginationInfo.setCurrentPageNo(loanVO.getPageIndex());
        paginationInfo.setRecordCountPerPage(loanVO.getPageUnit());
        paginationInfo.setPageSize(loanVO.getPageSize());

        loanVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
        loanVO.setLastIndex(paginationInfo.getLastRecordIndex());
        loanVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

        int totalCount = loanService.getAllLoanListToCnt(loanVO);
        paginationInfo.setTotalRecordCount(totalCount);

        List<LoanVO> list = loanService.getAllLoanList(loanVO);

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("paginationInfo", paginationInfo);
        result.put("totalCount", totalCount);

        return ResponseEntity.ok(result);
    }

    //회원 권한 변경
    @Operation(summary = "회원 권한 변경")
    @PostMapping("/members/role")
    public ResponseEntity<?> updateRole(@RequestParam String memberId,
                                        @RequestParam String memberRole) {

        memberService.updateMemberRole(memberId, memberRole);
        return ResponseEntity.ok("권한 변경 완료");
    }
}