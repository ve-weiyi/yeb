package com.hust.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hust.server.mapper.DepartmentMapper;
import com.hust.server.pojo.Department;
import com.hust.server.pojo.RespBean;
import com.hust.server.service.IDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhoubin
 * @since 2021-12-27
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements IDepartmentService {
    @Autowired
    DepartmentMapper departmentMapper;

    /**
     * 获取所有部门
     * @return
     */
    @Override
    public List<Department> getAllInFo() {
        return departmentMapper.getAllInFo(-1);
    }

    /**
     * 添加部门
     * @param department
     * @return
     */
    @Override
    public RespBean addDept(Department department) {
        department.setEnabled(true);
        departmentMapper.addDept(department);
        if (department.getResult() == 1){
            return RespBean.success("添加成功！",department);
        }
        return RespBean.error("添加失败！");
    }

    /**
     * 删除部门
     * @param id
     * @return
     */
    @Override
    public RespBean deleteDept(Integer id) {
        Department department = new Department();
        department.setId(id);
        departmentMapper.deleteDept(department);
        if (department.getResult() == -2) return RespBean.error("该部门下还有子部门，删除失败！");
        if (department.getResult() == -1) return RespBean.error("该部门下还有员工，删除失败！");
        if (department.getResult() == 1) return RespBean.success("删除成功！");
        return RespBean.error("删除失败！");
    }
}
