package egovframework.example.loan.service;

import java.util.List;

import org.springframework.stereotype.Service;

import egovframework.example.loan.domain.LoanVO;
import egovframework.example.loan.mapper.LoanMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService{

    private final LoanMapper loanMapper;

    @Override
    public String requestLoan(String memberId, int bookNo) {
        //5권 초과 체크
        int activeLoanCnt = loanMapper.selectMyActiveLoanCnt(memberId);
        if (activeLoanCnt >= 5) {
            return "MAX_LOAN"; //대출 가능 권수 초과
        }

        //같은 책 중복 대출 체크
        int duplicateCnt = loanMapper.selectDuplicateLoanCnt(memberId, bookNo);
        if (duplicateCnt > 0) {
            return "DUPLICATE"; //이미 대출 중인 책
        }

        //대출 가능한 아이템 조회
        Integer itemNo = loanMapper.selectAvailableItemNo(bookNo);
        if (itemNo == null) {
            return "NO_STOCK"; //재고 없음
        }

        //대출 처리
        LoanVO loanVO = new LoanVO();
        loanVO.setMemberId(memberId);
        loanVO.setItemNo((int) itemNo);
        loanMapper.insertLoan(loanVO);

        //아이템 상태 LOANED로 변경
        loanMapper.updateItemStatusToLoaned(itemNo);

        return "SUCCESS";
    }

    @Override
    public void returnLoan(int loanNo) {
        //대출 상태 RETURNED로 변경
        loanMapper.updateLoanReturn(loanNo);
        //아이템 상태 AVAILABLE로 변경
        loanMapper.updateItemStatusByLoanNo(loanNo);
    }

    @Override
    public List<LoanVO> getMyLoanList(LoanVO loanVO) {
        return loanMapper.selectMyLoanList(loanVO);
    }

    @Override
    public int getMyLoanListToCnt(LoanVO loanVO) {
        return loanMapper.selectMyLoanListToCnt(loanVO);
    }

    @Override
    public List<LoanVO> getAllLoanList(LoanVO loanVO) {
        return loanMapper.selectAllLoanList(loanVO);
    }

    @Override
    public int getAllLoanListToCnt(LoanVO loanVO) {
        return loanMapper.selectAllLoanListToCnt(loanVO);
    }
    
}
