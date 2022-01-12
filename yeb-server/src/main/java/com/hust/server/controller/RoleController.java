package com.hust.server.controller;


import com.hust.server.config.security.JwtTokenUtil;
import com.hust.server.pojo.Admin;
import com.hust.server.pojo.Role;
import com.hust.server.service.IAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhoubin
 * @since 2021-12-27
 */
@Api(tags = "角色查询")
@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private IAdminService adminService;

    @ApiOperation(value = "根据用户id查询角色列表")
    @GetMapping("/search")
    public List<Role> getRolesByAdminId(Integer adminId) {

        return adminService.getRoles(adminId);
    }


    @ApiOperation(value = "查询当前登录用户的权限")
    @GetMapping("/roles")
    public List<Role> getRolesByAdminId(Principal principal) {
        if (null == principal) {
            return null;
        }
        String username = principal.getName();
        Admin user = adminService.getAdminByUserName(username);
        user.setPassword(null);
        return adminService.getRoles(user.getId());
    }
}
