package egovframework.example.admin.web;

import java.util.List;

import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import egovframework.example.book.domain.BookVO;
import egovframework.example.book.service.BookService;
import egovframework.example.member.domain.MemberVO;
import egovframework.example.member.service.MemberService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    @Resource(name = "propertiesService")
    private EgovPropertyService propertiesService;

    private final BookService bookService;
    private final MemberService memberService;

    //관리자 메인 (대시보드)
    @GetMapping("")
    public String adminMain(Model model) {
        BookVO bookVO = new BookVO();
        MemberVO memberVO = new MemberVO();
        model.addAttribute("totalBooks", bookService.getBookListToCnt(bookVO));
        model.addAttribute("totalMembers", memberService.getMemberListToCnt(memberVO));
        return "admin/adminMain";
    }
    
    //회원 목록 조회
    @GetMapping("/members")
    public String memberList(@ModelAttribute MemberVO memberVO, Model model) {
        
        //전자정부 페이징 설정
        memberVO.setPageUnit(propertiesService.getInt("pageUnit"));
        memberVO.setPageSize(propertiesService.getInt("pageSize"));

        //PaginationInfo 설정
        PaginationInfo paginationInfo = new PaginationInfo();
        paginationInfo.setCurrentPageNo(memberVO.getPageIndex());
        paginationInfo.setRecordCountPerPage(memberVO.getPageUnit());
        paginationInfo.setPageSize(memberVO.getPageSize());

        memberVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
        memberVO.setLastIndex(paginationInfo.getLastRecordIndex());
        memberVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

        //전체 회원 수 조회
        int toCnt = memberService.getMemberListToCnt(memberVO);
        paginationInfo.setTotalRecordCount(toCnt);

        List<MemberVO> memberList = memberService.getMemberList(memberVO);

        model.addAttribute("memberList", memberList);
        model.addAttribute("paginationInfo", paginationInfo);
        model.addAttribute("memberVO", memberVO);

        return "admin/memberList";
    }

    //도서 목록 (관리자용 - 등록/수정/삭제 포함)
    @GetMapping("/books")
    public String bookList(@ModelAttribute BookVO bookVO, Model model) {
        bookVO.setPageUnit(propertiesService.getInt("pageUnit"));
        bookVO.setPageSize(propertiesService.getInt("pageSize"));

        PaginationInfo paginationInfo = new PaginationInfo();
        paginationInfo.setCurrentPageNo(bookVO.getPageIndex());
        paginationInfo.setRecordCountPerPage(bookVO.getPageUnit());
        paginationInfo.setPageSize(bookVO.getPageSize());

        bookVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
        bookVO.setLastIndex(paginationInfo.getLastRecordIndex());
        bookVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

        int toCnt = bookService.getBookListToCnt(bookVO);
        paginationInfo.setTotalRecordCount(toCnt);

        List<BookVO> bookList = bookService.getBookList(bookVO);
        model.addAttribute("bookList", bookList);
        model.addAttribute("paginationInfo", paginationInfo);
        model.addAttribute("bookVO", bookVO);
        return "admin/bookList";
    }

    //도서 등록 페이지
    @GetMapping("/books/register")
    public String registerPage(Model model) {
        model.addAttribute("bookVO", new BookVO());
        return "admin/bookRegister";
    }

    //도서 등록 처리
    @PostMapping("/books/register")
    public String register(@ModelAttribute BookVO bookVO,
                           RedirectAttributes redirectAttributes) {
        bookService.registerBook(bookVO);
        redirectAttributes.addFlashAttribute("successMsg", "도서가 등록되었습니다.");
        return "redirect:/admin/books";
    }

    //도서 수정 페이지
    @GetMapping("/books/update/{bookNo}")
    public String updatePage(@PathVariable int bookNo,
                             @RequestParam(defaultValue = "1") int pageIndex, 
                             Model model) {
        BookVO bookVO = bookService.getBookByNo(bookNo);
        bookVO.setPageIndex(pageIndex); //현재 페이지 번호 세팅
        model.addAttribute("bookVO", bookVO);
        return "admin/bookUpdate";
    }

    //도서 수정 처리
    @PostMapping("/books/update")
    public String update(@ModelAttribute BookVO bookVO,
                         RedirectAttributes redirectAttributes) {
        bookService.updateBook(bookVO);
        bookService.updateBookStock(bookVO); //재고 업데이트
        redirectAttributes.addFlashAttribute("successMsg", "도서 정보가 수정되었습니다.");
        return "redirect:/admin/books?pageIndex=" + bookVO.getPageIndex(); //수정 후 페이지 유지
    }

    //도서 삭제 처리
    @PostMapping("/books/delete/{bookNo}")
    public String delete(@PathVariable int bookNo,
                         RedirectAttributes redirectAttributes) {
        bookService.deleteBook(bookNo);
        redirectAttributes.addFlashAttribute("successMsg", "도서가 삭제되었습니다.");
        return "redirect:/admin/books";
    }
}
