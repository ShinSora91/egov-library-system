package egovframework.example.loan.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.egovframe.rte.psl.dataaccess.mapper.EgovMapper;

import egovframework.example.loan.domain.LoanVO;

@EgovMapper
public interface LoanMapper {
    
    //대출 신청
    void insertLoan(LoanVO loanVO);

    //반납 처리
    void updateLoanReturn(@Param("loanNo") int loanNo);

    //대출 가능한 아이템 번호 조회 (AVAILABLE인 item_no 1건)
    Integer selectAvailableItemNo(@Param("bookNo") int bookNo);

    //내 대출 목록
    List<LoanVO> selectMyLoanList(LoanVO loanVO);

    //내 대출 총 개수
    int selectMyLoanListToCnt(LoanVO loanVO);

    //현재 대출 중인 권수 (5권 제한 체크용)
    int selectMyActiveLoanCnt(@Param("memberId") String memberId);

    //이미 대출 중인지 체크 (같은 책 중복 대출 방지)
    int selectDuplicateLoanCnt(@Param("memberId") String memberId, @Param("bookNo") int bookNo);
    
    //관리자 전체 대출 목록
    List<LoanVO> selectAllLoanList(LoanVO loanVO);

    //관리자 전체 대출 총 개수
    int selectAllLoanListToCnt(LoanVO loanVO);

    //대출 시 소장 도서 상태 변경 (AVAILABLE → LOANED)
    void updateItemStatusToLoaned(@Param("itemNo") int itemNo);

    //반납 시 소장 도서 상태 변경 (LOANED → AVAILABLE)
    void updateItemStatusByLoanNo(@Param("loanNo") int loanNo);
}
