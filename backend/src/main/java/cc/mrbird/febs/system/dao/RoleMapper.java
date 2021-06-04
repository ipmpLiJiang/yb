package cc.mrbird.febs.system.dao;

import cc.mrbird.febs.system.domain.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper extends BaseMapper<Role> {
	
	List<Role> findUserRole(String userName);

	List<Role> findRoleConfList(@Param("configureType") Integer configureType);
}