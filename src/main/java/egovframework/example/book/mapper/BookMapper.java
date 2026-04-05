package egovframework.example.book.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.egovframe.rte.psl.dataaccess.mapper.EgovMapper;

import egovframework.example.book.domain.BookVO;

@EgovMapper
public interface BookMapper {
    
    //도서 등록
    void insertBook(BookVO bookVO);

    //도서 수정
    void updateBook(BookVO bookVO);

    //도서 삭제
    void deleteBook(@Param("bookNo") int bookNo);

    //도서 단건 조회
    BookVO selectBookByNo(@Param("bookNo") int bookNo);

    //도서 목록 조회
    List<BookVO> selectBookList(BookVO bookVO);

    //도서 총 개수 (페이징용)
    int selectBookListToCnt(BookVO bookVO);

    //소장 도서 등록 (수량만큼 반복)
    void insertBookItem(@Param("bookNo") int bookNo);

    //소장 도서 전체 삭제 (수정 시 기존 아이템 정리용)
    void deleteBookItems(@Param("bookNo") int bookNo);
}
