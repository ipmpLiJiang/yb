package cc.mrbird.febs.com.dao;

import cc.mrbird.febs.com.entity.ComFile;
import cc.mrbird.febs.com.entity.InUploadFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 附件 Mapper 接口
 * </p>
 *
 * @author viki
 * @since 2020-08-06
 */
public interface ComFileMapper extends BaseMapper<ComFile> {
    void updateComFile(ComFile comFile);

    IPage<ComFile> findComFile(Page page, @Param("comFile") ComFile comFile);

    List<ComFile> findAppealResultComFile(@Param("inUploadFile") InUploadFile inUploadFile);

    List<ComFile> findAppealResultSumComFile(@Param("inUploadFile") InUploadFile inUploadFile);

}
