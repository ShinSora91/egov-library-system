package egovframework.example.book.service;

import java.util.List;
import egovframework.example.book.domain.BookVO;

public interface BookService {
    
    //도서 등록
    void registerBook(BookVO bookVO);

    //도서 수정
    void updateBook(BookVO bookVO);

    //도서 삭제
    void deleteBook(int bookNo);

    //도서 단건 조회
    BookVO getBookByNo(int bookNo);

    //도서 목록 조회
    List<BookVO> getBookList(BookVO bookVO);

    //도서 총 개수
    int getBookListToCnt(BookVO bookVO);
}
