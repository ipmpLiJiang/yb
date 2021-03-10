package cc.mrbird.febs.com.service;

import cc.mrbird.febs.com.entity.ComType;
import cc.mrbird.febs.system.domain.User;
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
 * @since 2021-03-05
 */
public interface IComTypeService extends IService<ComType> {

        IPage<ComType> findComTypes(QueryRequest request, ComType comType);

        IPage<ComType> findComTypeList(QueryRequest request, ComType comType);

        void createComType(ComType comType);

        void updateComType(ComType comType);

        void editComType(ComType comType, User currentUser);

        void deleteComTypes(String[]Ids);

        List<ComType> findComTypeList(ComType comType);
        }
