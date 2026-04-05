package egovframework.example.book.service;

import java.util.List;

import org.springframework.stereotype.Service;

import egovframework.example.book.domain.BookVO;
import egovframework.example.book.mapper.BookMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService{
    
    private final BookMapper bookMapper;
    
    @Override
    public void registerBook(BookVO bookVO) {
        bookMapper.insertBook(bookVO);

        //수량만큼 tb_book_item에 등록
        for (int i=0; i<bookVO.getBookNo(); i++) {
            bookMapper.insertBookItem(bookVO.getBookNo());
        }
    }

    @Override
    public void updateBook(BookVO bookVO) {
        bookMapper.updateBook(bookVO);
    }
        
    @Override
    public void deleteBook(int bookNo) {
        bookMapper.deleteBook(bookNo);
    }

    @Override
    public BookVO getBookByNo(int bookNo) {
        return bookMapper.selectBookByNo(bookNo);
    }

    @Override
    public List<BookVO> getBookList(BookVO bookVO) {
        return bookMapper.selectBookList(bookVO);   
    }

    @Override
    public int getBookListToCnt(BookVO bookVO) {
        return bookMapper.selectBookListToCnt(bookVO);      
    }

    @Override
    public void updateBookStock(BookVO bookVO) {
        //기존 아이템 전체 삭제
        bookMapper.deleteBookItems(bookVO.getBookNo());

        //새 수량만큼 재등록
        for (int i=0; i<bookVO.getBookStock(); i++) {
            bookMapper.insertBookItem(bookVO.getBookNo());
        }
    }
    
}
