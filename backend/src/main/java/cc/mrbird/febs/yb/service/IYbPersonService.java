package cc.mrbird.febs.yb.service;

import cc.mrbird.febs.yb.entity.YbPerson;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author viki
 * @since 2020-07-21
 */
public interface IYbPersonService extends IService<YbPerson> {

        IPage<YbPerson> findYbPersons(QueryRequest request, YbPerson ybPerson);

        IPage<YbPerson> findYbPersonList(QueryRequest request, YbPerson ybPerson);

        void createYbPerson(YbPerson ybPerson);

        void updateYbPerson(YbPerson ybPerson);

        void deleteYbPersons(String[]Ids);
        }
