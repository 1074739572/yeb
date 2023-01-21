package com.xxxx.server.mapper;

import com.xxxx.server.pojo.Admin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxxx.server.pojo.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Bing
 * @since 2022-11-09
 */
public interface AdminMapper extends BaseMapper<Admin> {
    List<Admin> getAllAdmins(@Param("id") Integer id,@Param("keywords") String keywords);
}
