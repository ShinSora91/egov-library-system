package egovframework.example.loan.service;

import java.util.List;

import egovframework.example.loan.domain.LoanVO;

public interface LoanService {
    
    //대출 신청
    //반환값 : 성공 여부 메시지 ("SUCCESS", "MAX_LOAN", "DUPLICATE", "NO_STOCK")
    String requestLoan(String memberId, int bookNo);

    //반납 처리
    void returnLoan(int loanNo);

    //내 대출 목록
    List<LoanVO> getMyLoanList(LoanVO loanVO);

    //내 대출 총 개수
    int getMyLoanListToCnt(LoanVO loanVO);

    //관리자 전체 대출 목록
    List<LoanVO> getAllLoanList(LoanVO loanVO);

    //관리자 전체 대출 총 개수
    int getAllLoanListToCnt(LoanVO loanVO);

}
