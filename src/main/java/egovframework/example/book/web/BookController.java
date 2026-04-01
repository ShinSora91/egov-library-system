package egovframework.example.book.web;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import egovframework.example.book.domain.BookVO;
import egovframework.example.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {
    
    private final BookService bookService;

    //도서 목록 조회
    @GetMapping("/list")
    public String bookList(@ModelAttribute BookVO bookVO, Model model) {
        List<BookVO> bookList = bookService.getBookList(bookVO);
        model.addAttribute("bookList", bookList);
        return "book/list";
    }

    //도서 등록 페이지(관리자)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("bookVO", new BookVO());
        return "book/register";
    }

    //도서 등록 처리(관리자)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    public String register(@ModelAttribute BookVO bookVO,
                           RedirectAttributes redirectAttributes) {
        bookService.registerBook(bookVO);
        redirectAttributes.addFlashAttribute("successMsg", "도서가 등록되었습니다.");
        return "redirect:/book/list";
    }

    //도서 수정 페이지(관리자)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/update/{bookNo}")
    public String updatePage(@PathVariable int bookNo, Model model) {
        BookVO bookVO = bookService.getBookByNo(bookNo);
        model.addAttribute("bookVO", bookVO);
        return "book/update";
    }
    
    //도서 수정 처리(관리자)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/update")
    public String update(@ModelAttribute BookVO bookVO,
                          RedirectAttributes redirectAttributes) {
        bookService.updateBook(bookVO);
        redirectAttributes.addFlashAttribute("successMsg", "도서 정보가 수정되었습니다.");
        return "redirect:/book/list";
    }

    //도서 삭제 처리(관리자)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete/{bookNo}")
    public String delete(@PathVariable int bookNo,
                         RedirectAttributes redirectAttributes) {
        bookService.deleteBook(bookNo);
        redirectAttributes.addFlashAttribute("successMsg", "도서가 삭제되었습니다.");
        return "redirect:/book/list";
    }
    
}
