package egovframework.example.loan.domain;

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
public class LoanVO {
    private int loanNo;
    private String memberId;
    private int itemNo;
    private String loanStatus; //LOAN, RETURN
    private String loanDate;
    private String returnDueDate;
    private String returnDate;

    //조회용 (JOIN)
    private int bookNo;
    private String bookTitle;
    private String bookAuthor;
    private String bookPublisher;
    private String memberName;

    //페이징, 검색
    private int pageIndex = 1;
    private int pageUnit = 10;
    private int pageSize = 10;
    private int firstIndex = 0;
    private int lastIndex = 0;
    private int recordCountPerPage = 0;
    private String searchKeyword;
    private String searchCondition;
}
