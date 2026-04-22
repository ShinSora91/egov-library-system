package egovframework.example.book.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.web.bind.annotation.*;

import egovframework.example.book.domain.BookVO;
import egovframework.example.book.service.BookService;
import egovframework.example.commonMethod.PagingCommon;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;

@Tag(name = "도서 목록 API", description = "도서 목록 조회용 API")
@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookAPIController {

    @Resource(name = "propertiesService")
    private EgovPropertyService propertiesService;

    private final BookService bookService;

    //도서 목록 (페이징 + 검색 포함)
    @Operation(summary = "도서 목록 조회", description = "페이징 및 검색 포함")
    @GetMapping
    public Map<String, Object> getBookList(@ModelAttribute BookVO bookVO) {

        //페이징 설정
        PagingCommon.setPaging(bookVO, propertiesService);

        PaginationInfo paginationInfo = new PaginationInfo();
        paginationInfo.setCurrentPageNo(bookVO.getPageIndex());
        paginationInfo.setRecordCountPerPage(bookVO.getPageUnit());
        paginationInfo.setPageSize(bookVO.getPageSize());

        bookVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
        bookVO.setLastIndex(paginationInfo.getLastRecordIndex());
        bookVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

        //전체 개수
        int totalCount = bookService.getBookListToCnt(bookVO);
        paginationInfo.setTotalRecordCount(totalCount);

        //리스트 조회
        List<BookVO> bookList = bookService.getBookList(bookVO);

        //JSON 응답 구성
        Map<String, Object> result = new HashMap<>();
        result.put("list", bookList);
        result.put("paginationInfo", paginationInfo);
        result.put("totalCount", totalCount);
        result.put("searchVO", bookVO);

        return result;
    }

    //도서 상세 조회
    @Operation(summary = "도서 상세 조회")
    @GetMapping("/{bookNo}")
    public BookVO getBookDetail(@PathVariable int bookNo) {
        return bookService.getBookByNo(bookNo);
    }
}