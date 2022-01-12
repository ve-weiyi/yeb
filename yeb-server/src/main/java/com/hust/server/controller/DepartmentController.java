package com.hust.server.controller;


import com.hust.server.pojo.Department;
import com.hust.server.pojo.RespBean;
import com.hust.server.service.IDepartmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhoubin
 * @since 2021-12-27
 */
@RestController
@RequestMapping("/system/basic/department")
@Api(value = "部门管理", tags = "部门管理")
public class DepartmentController {

    @Autowired
    IDepartmentService departmentService;

    @ApiOperation("获取所有部门")
    @GetMapping("/list")
    public List<Department> getAllInFo(){

        return departmentService.getAllInFo();
    }

    @ApiOperation("添加部门")
    @PostMapping("/add")
    public RespBean addDept(@RequestBody Department department){

        return departmentService.addDept(department);
    }

    @ApiOperation("删除部门")
    @DeleteMapping("/delete")
    public RespBean deleteDept(@RequestParam("id") Integer id){
        return departmentService.deleteDept(id);
    }
}