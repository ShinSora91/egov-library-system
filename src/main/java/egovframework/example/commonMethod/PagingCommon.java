package egovframework.example.commonMethod;

import org.egovframe.rte.fdl.property.EgovPropertyService;

import egovframework.example.book.domain.BookVO;
import egovframework.example.member.domain.MemberVO;
import egovframework.example.loan.domain.LoanVO;

public class PagingCommon {

    public static void setPaging(BookVO vo, EgovPropertyService propertiesService) {
        if (vo.getPageIndex() <= 0) {
            vo.setPageIndex(1);
        }

        if (vo.getPageUnit() <= 0) {
            vo.setPageUnit(propertiesService.getInt("pageUnit"));
        }

        if (vo.getPageSize() <= 0) {
            vo.setPageSize(propertiesService.getInt("pageSize"));
        }

        vo.setRecordCountPerPage(vo.getPageUnit());
    }

    public static void setPaging(MemberVO vo, EgovPropertyService propertiesService) {
        if (vo.getPageIndex() <= 0) {
            vo.setPageIndex(1);
        }

        if (vo.getPageUnit() <= 0) {
            vo.setPageUnit(propertiesService.getInt("pageUnit"));
        }

        if (vo.getPageSize() <= 0) {
            vo.setPageSize(propertiesService.getInt("pageSize"));
        }

        vo.setRecordCountPerPage(vo.getPageUnit());
    }

    public static void setPaging(LoanVO vo, EgovPropertyService propertiesService) {
        if (vo.getPageIndex() <= 0) {
            vo.setPageIndex(1);
        }

        if (vo.getPageUnit() <= 0) {
            vo.setPageUnit(propertiesService.getInt("pageUnit"));
        }

        if (vo.getPageSize() <= 0) {
            vo.setPageSize(propertiesService.getInt("pageSize"));
        }

        vo.setRecordCountPerPage(vo.getPageUnit());
    }
}