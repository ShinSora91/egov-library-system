package egovframework.example.book.web;

import java.util.List;

import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import egovframework.example.book.domain.BookVO;
import egovframework.example.book.service.BookService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {
    
    @Resource(name = "propertiesService")
    private EgovPropertyService propertiesService;
    private final BookService bookService;

    //도서 목록 조회
    @GetMapping("/list")
    public String bookList(@ModelAttribute BookVO bookVO, Model model) {
        
        //전자정부 페이징 설정
        bookVO.setPageUnit(propertiesService.getInt("pageUnit"));
        bookVO.setPageSize(propertiesService.getInt("pageSize"));

        //PaginationInfo 설정
        PaginationInfo paginationInfo = new PaginationInfo();
        paginationInfo.setCurrentPageNo(bookVO.getPageIndex());
        paginationInfo.setRecordCountPerPage(bookVO.getPageUnit());
        paginationInfo.setPageSize(bookVO.getPageSize());

        bookVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
        bookVO.setLastIndex(paginationInfo.getLastRecordIndex());
        bookVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

        //전체 도서 수 조회
        int toCnt = bookService.getBookListToCnt(bookVO);
        paginationInfo.setTotalRecordCount(toCnt);

        List<BookVO> bookList = bookService.getBookList(bookVO);

        model.addAttribute("bookList", bookList);
        model.addAttribute("paginationInfo", paginationInfo);
        model.addAttribute("bookVO", bookVO);

        return "book/list";
    }
    
}
