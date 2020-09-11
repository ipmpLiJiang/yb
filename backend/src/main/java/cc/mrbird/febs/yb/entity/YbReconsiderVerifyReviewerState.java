package cc.mrbird.febs.yb.entity;

import lombok.Data;

/**
 * @author lijiang
 * @createDate 2020/7/24
 */
    @Data
    public class YbReconsiderVerifyReviewerState {
        private Long id;
        private String doctorCode;
        private String doctorName;
        private String deptCode;
        private String deptName;
    }

