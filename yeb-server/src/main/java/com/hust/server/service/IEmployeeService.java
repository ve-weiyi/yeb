package com.hust.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hust.server.pojo.Employee;
import com.hust.server.pojo.RespBean;
import com.hust.server.pojo.RespPageBean;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LHC
 * @since 2021-06-25
 */
public interface IEmployeeService extends IService<Employee> {

    /**
     * 分页获取员工列表
     * @param currentPage
     * @param size
     * @param employee
     * @return
     */
    RespPageBean getEmployeeList(Integer currentPage, Integer size, Employee employee, String startDate, String endDate);

    /**
     * 获取工号
     * @return
     */
    RespBean getMaxWorkID();

    /**
     * 获取工号 方法2
     * @return
     */
    RespBean getMaxWorkID2();

    /**
     * 添加员工
     * @param employee
     */
    RespBean addEmp(Employee employee);

    /**
     * 查询员工
     * @param id
     * @return
     */
    List<Employee> getEmployee(Integer id);

    /**
     * 导出员工表格
     * @param id
     */
    void getEmployee(Integer id, HttpServletResponse response);

    /**
     * 获取员工添加所需要的各种id
     * @param employeeMap
     * @return
     */
    Employee getIdSelectNationByName(Map<String,String> employeeMap);

    /**
     * 获取所有工资账套
     * @param currentPage
     * @param pageSize
     * @return
     */
    RespPageBean getAllEmpSalary(Integer currentPage, Integer pageSize);

    /**
     * 更新员工账套信息
     * @param eid
     * @param eSid
     */
    Boolean updateEmpEsid(Integer eid, Integer eSid);

    /**
     * 获取所有员工账套
     *
     * @param currentPage
     * @param size
     * @return
     */
    RespPageBean getEmployeeWithSalary(Integer currentPage, Integer size);
}
