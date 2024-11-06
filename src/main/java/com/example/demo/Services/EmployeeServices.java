package com.example.demo.Services;

import com.example.demo.models.Department;
import com.example.demo.models.Employee;
import com.example.demo.models.dto.EmployeeDetail;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.DepartmentRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
public class EmployeeServices {
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final S3services s3Service;


    public void initializeEmployeesWithS3Images() {
        List<String> s3ImageUrls = s3Service.getAllImageUrls();
        List<Employee> employees = employeeRepository.findAll();

        for (int i = 0; i < Math.min(employees.size(), s3ImageUrls.size()); i++) {
            Employee emp = employees.get(i);
            emp.setImg(s3ImageUrls.get(i));
            employeeRepository.save(emp);
        }
    }
    public List<EmployeeDetail> getAllEmployeeDetails() {
        List<Employee> employees = employeeRepository.findAll();
        List<EmployeeDetail> details = new ArrayList<>();

        for (Employee emp : employees) {
            EmployeeDetail dto = new EmployeeDetail();

            dto.setEmployeeNo(emp.getEmpno());
            dto.setEmployeeName(emp.getEname() != null ? emp.getEname() : "");
            dto.setJob(emp.getJob() != null ? emp.getJob() : "");

            dto.setHireDate(emp.getHiredate() != null ? emp.getHiredate().toString() : "");

            dto.setSalary(emp.getSal() != null ? emp.getSal() : 0.0);

            dto.setCommission(emp.getComm() != null ? emp.getComm() : 0.0);

            dto.setImageUrl(emp.getImg() != null ? emp.getImg() : "");

            if (emp.getMgr() != null) {
                Employee manager = employeeRepository.findById(emp.getMgr()).orElse(null);
                dto.setManagerName(manager != null ? manager.getEname() : "");
            } else {
                dto.setManagerName("");
            }

            if (emp.getDeptno() != null) {
                Department dept = departmentRepository.findById(emp.getDeptno()).orElse(null);
                dto.setDepartmentName(dept != null ? dept.getDname() : "");
            } else {
                dto.setDepartmentName("");
            }

            details.add(dto);
        }

        return details;
    }

    public void saveEmployeeWithDetails(Employee employee, String managerName, String departmentName) {
        if (managerName != null && !managerName.trim().isEmpty()) {
            Employee manager = employeeRepository.findByEname(managerName);
            if (manager != null) {
                employee.setMgr(manager.getEmpno());
            }
        }

        if (departmentName != null && !departmentName.trim().isEmpty()) {
            Department department = departmentRepository.findByDname(departmentName);
            if (department != null) {
                employee.setDeptno(department.getDeptno());
            }
        }

        employeeRepository.save(employee);
    }
    public void deleteEmployee(Long empno) {
        employeeRepository.deleteById(empno);
    }
}