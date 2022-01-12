package com.hust.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hust.server.pojo.Department;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhoubin
 * @since 2021-12-27
 */
public interface DepartmentMapper extends BaseMapper<Department> {
    /**
     * 获取所有部门
     * @return
     */
    List<Department> getAllInFo(Integer parentId);

    /**
     * 添加部门
     * @param department
     * @return
     */
    void addDept(Department department);

    /**
     * 删除部门
     * @param department
     * @return
     */
    void deleteDept(Department department);
}
