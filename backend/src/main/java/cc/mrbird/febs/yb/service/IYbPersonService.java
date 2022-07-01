package cc.mrbird.febs.yb.service;

import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.yb.entity.YbPerson;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author viki
 * @since 2020-07-21
 */
public interface IYbPersonService extends IService<YbPerson> {

    IPage<YbPerson> findYbPersons(QueryRequest request, YbPerson ybPerson);

    IPage<YbPerson> findYbPersonList(QueryRequest request, YbPerson ybPerson);

    void createYbPerson(YbPerson ybPerson) throws Exception;

    void updateYbPerson(YbPerson ybPerson) throws Exception;

    List<YbPerson> findPersonList(YbPerson ybPerson,int type);

    List<YbPerson> findPersonList(ArrayList<String> personCodeList);

    List<YbPerson> findPersonResultList(String applyDateStr,Integer areaType);

    List<YbPerson> findPersonWarnLists(String applyDateStr,Integer areaType, Integer acceptState, Integer typeno, Integer sourceType);

    List<String> findPersonCodeList(String value);

    void deleteYbPersons(String[] Ids) throws Exception;

    String importUserRoles(Integer type) throws Exception;

    boolean importPerson(User logUser) throws Exception;

    YbPerson findByName(String personCode);

    List<YbPerson> findDrgPersonWarnLists(String applyDateStr,Integer areaType, Integer state);

    List<YbPerson> findChsPersonWarnLists(String applyDateStr,Integer areaType, Integer state);
}
