package egovframework.example.book.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class BookVO {
    private int bookNo;
    private String bookTitle;
    private String bookAuthor;
    private String bookPublisher;
    private String registDate;
    private String deleteYn = "N";
    private String deleteDate;

    //페이징
    private int pageIndex = 1;
    private int pageUnit = 10; //페이지당 개시물 수
    private int pageSize = 10; //페이지 네비게이션 크기
    private int firstIndex = 0;
    private int lastIndex = 0;
    private int recordCountPerPage = 0;
    private String searchKeyword;
    private String searchCondition;

}
