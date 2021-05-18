package cc.mrbird.febs.yb.service;

import cc.mrbird.febs.yb.entity.YbDept;
import cc.mrbird.febs.yb.entity.YbDeptHis;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import cc.mrbird.febs.common.domain.QueryRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author viki
 * @since 2020-07-21
 */
public interface IYbDeptService extends IService<YbDept> {

        IPage<YbDept> findYbDepts(QueryRequest request, YbDept ybDept);

        IPage<YbDept> findYbDeptList(QueryRequest request, YbDept ybDept);

        void createYbDept(YbDept ybDept);

        void updateYbDept(YbDept ybDept);

        void deleteYbDepts(String[]Ids);

        List<YbDept> findDeptList(YbDept ybDept,int type);

        void deleteBatchDepts();

        boolean createBatchDepts(List<YbDeptHis> list);

        List<String> findDeptCodeList(String value);
        }
