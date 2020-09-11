package cc.mrbird.febs.com.service;

import cc.mrbird.febs.com.entity.ComFile;
import cc.mrbird.febs.com.entity.InUploadFile;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 附件 服务类
 * </p>
 *
 * @author viki
 * @since 2020-08-06
 */
public interface IComFileService extends IService<ComFile> {

    IPage<ComFile> findComFiles(QueryRequest request, ComFile comFile);

    IPage<ComFile> findComFileList(QueryRequest request, ComFile comFile);

    void createComFile(ComFile comFile);

    void updateComFile(ComFile comFile);

    void deleteComFiles(String[] Ids);

    int deleteComFile(String Id);

    List<ComFile> findListComFile(String Id);

    ComFile findComFileById(String Id);

    List<ComFile> findAppealResultComFiles(InUploadFile inUploadFile);
}
