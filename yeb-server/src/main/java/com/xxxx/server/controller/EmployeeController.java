package com.xxxx.server.controller;


import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.xxxx.server.pojo.*;
import com.xxxx.server.service.*;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.formula.WorkbookDependentFormula;
import org.apache.poi.ss.formula.functions.Na;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Bing
 * @since 2022-11-09
 */
@RestController
@RequestMapping("/basic/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private PoliticsStatusService politicsStatusService;

    @Autowired
    private JoblevelService joblevelService;

    @Autowired
    private NationService nationService;

    @Autowired
    private PositionService positionService;

    @Autowired
    private DepartmentService departmentService;

    @ApiOperation(value = "获取所有员工(分页)")
    @GetMapping("/")
    public RespPageBean getEmployee(@RequestParam(defaultValue="1")Integer currenPage,
                                    @RequestParam(defaultValue = "10")Integer size,
                                    Employee employee,
                                    LocalDate[] beginDateScope) {
        return employeeService.getEmployeeByPage(currenPage,size,employee,beginDateScope);
    }

    @ApiOperation(value = "获取所有政治面貌")
    @GetMapping("/politics")
    public List<PoliticsStatus> getAllpoliticsStatus(){
        return politicsStatusService.list();
    }

    @ApiOperation(value = "获取所有职称")
    @GetMapping("/joblevels")
    public List<Joblevel> getAllJobLevel(){
        return joblevelService.list();
    }

    @ApiOperation(value = "获取所有民族")
    @GetMapping("/nations")
    public List<Nation> getAllnations(){
        return nationService.list();
    }

    @ApiOperation(value = "获取所有职位")
    @GetMapping("/position")
    public List<Position> getAllPositions(){
        return positionService.list();
    }

    @ApiOperation(value = "获取所有部门")
    @GetMapping("/dep")
    public List<Department> getAllDepartments(){
        return departmentService.getAllDepartment();
    }

    @ApiOperation(value = "获取工号")
    @GetMapping("/maxworkId")
    public RespBean maxWorkId(){
        return employeeService.maxWorkId();
    }

    @ApiOperation(value = "添加员工")
    @PostMapping ("/addEmp")
    public RespBean addEmp(@RequestBody Employee employee){
        return employeeService.addEmp(employee);
    }

    @ApiOperation(value = "更新员工")
    @PutMapping("/updataEmp")
    public RespBean updataEmp(@RequestBody Employee employee){
        if (employeeService.updateById(employee)){
            return RespBean.success("更新成功!");
        }
        return  RespBean.error("更新错误!");
    }

    @ApiOperation(value = "删除员工")
    @DeleteMapping("/{id}")
    public RespBean deleteEmp(@PathVariable Integer id){
        if (employeeService.removeById(id)){
            return RespBean.success("删除成功!");
        }
        return RespBean.error("删除失败!");
    }

    @ApiOperation(value = "导出员工数据")
    @GetMapping(value = "/export",produces = "application/octet-stream")
    public void exportEmployee(HttpServletResponse response){
        List<Employee> employees = employeeService.getEmployee(null);
        ExportParams params = new ExportParams("员工表","员工表", ExcelType.HSSF);
        Workbook workbook = ExcelExportUtil.exportExcel(params,Employee.class,employees);
        ServletOutputStream outputStream = null;
        try{
            //流形式
            response.setHeader("content-type","application/octet-stream");
            //防止中文乱码
            response.setHeader("content-disposition",
                    "attachment;filename"+
                            URLEncoder.encode("员工表.xls","utf-8"));
            outputStream = response.getOutputStream();
            workbook.write(outputStream);
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if (null!=outputStream){
                try {
                    outputStream.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    @ApiOperation(value = "导入员工数据")
    @PostMapping("/import")
    public RespBean importEmployee(MultipartFile file){
        ImportParams params = new ImportParams();
        params.setTitleRows(1);
        List<Nation> nations = nationService.list();
        List<PoliticsStatus> politicsStatuses = politicsStatusService.list();
        List<Department> departments = departmentService.list();
        List<Joblevel> joblevels = joblevelService.list();
        List<Position> positions = positionService.list();
        try {
            List<Employee> list = ExcelImportUtil.importExcel(file.getInputStream(),Employee.class,params);
            list.forEach(employee -> {
                employee.setNationId(nations.get(nations.
                        indexOf(new Nation(employee.getNation().getName()))).getId());
                employee.setPoliticId(politicsStatuses.get(politicsStatuses.
                        indexOf(new PoliticsStatus(employee.getPoliticsStatus().getName()))).getId());
                employee.setDepartmentId(departments.get(departments.
                        indexOf(new Department(employee.getDepartment().getName()))).getId());
                employee.setJobLevelId(joblevels.get(joblevels.
                        indexOf(new Joblevel(employee.getJoblevel().getName()))).getId());
                employee.setPosId(positions.get(positions.
                        indexOf(new Position(employee.getPosition().getName()))).getId());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.error("导入错误!");
    }
}

