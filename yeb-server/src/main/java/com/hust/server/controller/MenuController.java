package com.hust.server.controller;


import com.hust.server.pojo.Menu;
import com.hust.server.service.IMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhoubin
 * @since 2021-12-27
 */
@Api(value = "菜单信息",tags = "菜单信息")
@RestController
@RequestMapping("/system/config")
public class MenuController {

    @Autowired
    private IMenuService menuService;

    @ApiOperation(value = "通过用户ID获取菜单列表")
    @GetMapping("/menu")
    public List<Menu> getMenuByAdminId(){

        return menuService.getMenuByAdminId();
    }

}
