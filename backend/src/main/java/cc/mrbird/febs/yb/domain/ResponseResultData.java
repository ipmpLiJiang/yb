package cc.mrbird.febs.yb.domain;

import lombok.Data;
import org.apache.poi.ss.formula.functions.T;

/**
 * @author lijiang
 * @createDate 2020/8/20
 */
@Data
public class ResponseResultData {
    private int success;
    private String message;
}
