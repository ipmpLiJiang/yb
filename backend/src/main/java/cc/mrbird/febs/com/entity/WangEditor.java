package cc.mrbird.febs.com.entity;

import lombok.Data;

/**
 * @author lijiang
 * @createDate 2020/8/7
 */
@Data
public class WangEditor {
    private Integer errno; //错误代码，0 表示没有错误。
    private String[] data; //已上传的图片路径


    public WangEditor(String[] data) {
        super();
        this.errno = 0;
        this.data = data;
    }
}
