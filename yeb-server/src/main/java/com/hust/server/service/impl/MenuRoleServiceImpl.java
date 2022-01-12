package com.hust.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hust.server.mapper.MenuRoleMapper;
import com.hust.server.pojo.MenuRole;
import com.hust.server.pojo.RespBean;
import com.hust.server.service.IMenuRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhoubin
 * @since 2021-12-27
 */
@Service
public class MenuRoleServiceImpl extends ServiceImpl<MenuRoleMapper, MenuRole> implements IMenuRoleService {

    @Autowired
    private MenuRoleMapper menuRoleMapper;
    /**
     * 更新角色菜单
     * @Transactional 事务的一个注解，保证完整完成
     * @param rid
     * @param mids
     * @return
     */
    @Override
    @Transactional
    public RespBean updateMenuRole(Integer rid, Integer[] mids) {
        //先删除该角色所有的菜单
        menuRoleMapper.delete(new QueryWrapper<MenuRole>().eq("rid",rid));
        //添加
        //没有传菜单id
        if (null==mids||0==mids.length){
            return RespBean.success("更新成功！");
        }
        //传了菜单id，批量更新
        Integer result = menuRoleMapper.insertRecord(rid,mids);
        if (mids.length==result){
            return RespBean.success("更新成功!");
        }
        return RespBean.error("更新失败!");
    }
}
