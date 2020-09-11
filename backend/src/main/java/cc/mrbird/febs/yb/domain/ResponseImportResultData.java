package cc.mrbird.febs.yb.domain;

import lombok.Data;

/**
 * @author lijiang
 * @createDate 2020/8/24
 */
@Data
public class ResponseImportResultData {
    private int success;
    private String message;
    private String fileName;
}
