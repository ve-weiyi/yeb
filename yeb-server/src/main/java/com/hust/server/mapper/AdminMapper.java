package com.hust.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hust.server.pojo.Admin;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhoubin
 * @since 2021-12-27
 */
public interface AdminMapper extends BaseMapper<Admin> {

    /**
     * 获取所有操作员
     * @param keywords
     * @return
     */
    List<Admin> getAllAdmins(@Param("id") Integer id, @Param("keywords") String keywords);

    /**
     * 获取管理员
     * @param username
     */
    Admin getAdmin(@Param("username") String username);
}
