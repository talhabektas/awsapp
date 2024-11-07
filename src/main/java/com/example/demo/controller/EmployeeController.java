package com.example.demo.controller;

import com.example.demo.Services.EmployeeServices;
import com.example.demo.models.dto.EmployeeDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.demo.models.Employee;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.demo.Services.S3services;
@Controller
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeServices employeeService;
    private final S3services s3Service;

    @GetMapping("/sync-s3-images")
    public String syncS3Images() {
        employeeService.initializeEmployeesWithS3Images();
        return "redirect:/employees";
    }

    @GetMapping
    public String listEmployees(Model model) {
        model.addAttribute("employee", employeeService.getAllEmployeeDetails());
        return "employees";
    }
//    @GetMapping("/edit/{empno}")
//    public String editEmployee(@PathVariable Long empno, Model model) {
//        Employee employee = employeeService.getEmployeeById(empno);
//        model.addAttribute("employee", employee);
//        return "employee-edit";
//    }
//
//    @PostMapping("/edit/{empno}")
//    public String updateEmployee(@PathVariable Long empno,
//                                 @RequestParam("ename") String ename,
//                                 @RequestParam("job") String job,
//                                 @RequestParam("managerName") String managerName,
//                                 @RequestParam("hireDate") String hireDate,
//                                 @RequestParam("salary") Double salary,
//                                 @RequestParam("commission") Double commission,
//                                 @RequestParam("departmentName") String departmentName,
//                                 @RequestParam("image") MultipartFile image) {
//
//        Employee employee = employeeService.getEmployeeById(empno);
//        employee.setEname(ename);
//        employee.setJob(job);
//        employee.setSal(salary);
//        employee.setComm(commission);
//
//        try {
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            employee.setHiredate(dateFormat.parse(hireDate));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        if (!image.isEmpty()) {
//            String fileName = s3Service.uploadFile(image);
//            String imageUrl = s3Service.getFileUrl(fileName);
//            employee.setImg(imageUrl);
//        }
//
//        employeeService.updateEmployee(employee, managerName, departmentName);
//        return "redirect:/employees";
//    }
@GetMapping("/edit/{empno}")
public String showEditForm(@PathVariable Long empno, Model model) {
    EmployeeDetail employee = employeeService.getEmployeeDetailById(empno);
    model.addAttribute("employee", employee);
    return "edit-employee";
}

    @PostMapping("/update/{empno}")
    public String updateEmployee(@PathVariable Long empno,
                                 @RequestParam("ename") String ename,
                                 @RequestParam("job") String job,
                                 @RequestParam("managerName") String managerName,
                                 @RequestParam("hireDate") String hireDate,
                                 @RequestParam("salary") Double salary,
                                 @RequestParam("commission") Double commission,
                                 @RequestParam("departmentName") String departmentName,
                                 @RequestParam(value = "image", required = false) MultipartFile image) {

        Employee employee = employeeService.getEmployeeById(empno);
        employee.setEname(ename);
        employee.setJob(job);
        employee.setSal(salary);
        employee.setComm(commission);

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            employee.setHiredate(dateFormat.parse(hireDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (image != null && !image.isEmpty()) {
            String fileName = s3Service.uploadFile(image);
            String imageUrl = s3Service.getFileUrl(fileName);
            employee.setImg(imageUrl);
        }

        employeeService.saveEmployeeWithDetails(employee, managerName, departmentName);
        return "redirect:/employees";
    }

    @PostMapping
    public String addEmployee(@RequestParam("empno") Long empno,
                              @RequestParam("ename") String ename,
                              @RequestParam("job") String job,
                              @RequestParam("managerName") String managerName,
                              @RequestParam("hireDate") String hireDate,
                              @RequestParam("salary") Double salary,
                              @RequestParam("commission") Double commission,
                              @RequestParam("departmentName") String departmentName,
                              @RequestParam("image") MultipartFile image) {

        Employee employee = new Employee();
        employee.setEmpno(empno);
        employee.setEname(ename);
        employee.setJob(job);
        employee.setSal(salary);
        employee.setComm(commission);

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            employee.setHiredate(dateFormat.parse(hireDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (!image.isEmpty()) {
            String fileName = s3Service.uploadFile(image);
            String imageUrl = s3Service.getFileUrl(fileName);
            employee.setImg(imageUrl);
        }

        employeeService.saveEmployeeWithDetails(employee, managerName, departmentName);
        return "redirect:/employees";
    }

    @PostMapping("/delete/{empno}")
    public String deleteEmployee(@PathVariable Long empno) {
        employeeService.deleteEmployee(empno);
        return "redirect:/employees";
    }
}