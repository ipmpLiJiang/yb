package cc.mrbird.febs.yb.entity;

import lombok.Data;

import java.util.List;

/**
 * @author lijiang
 * @createDate 2020/7/24
 */
@Data
public class YbReconsiderVerifyUpdateState {
    private int updateState;
    private int state;
    private String ids;
    private List<String> listString;
    private int acceptState;
}

