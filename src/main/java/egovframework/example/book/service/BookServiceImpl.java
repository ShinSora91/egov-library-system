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
    
}
