package com.example.demo.controller;

import com.example.demo.Services.EmployeeServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
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

    @PostMapping
    public String addEmployee(@RequestParam("empno") Long empno,
                              @RequestParam("ename") String ename,
                              @RequestParam("image") MultipartFile image) {
        Employee employee = new Employee();
        employee.setEmpno(empno);
        employee.setEname(ename);

        if (!image.isEmpty()) {
            String fileName = s3Service.uploadFile(image);
            String imageUrl = s3Service.getFileUrl(fileName);
            employee.setImg(imageUrl);
        }

        employeeService.saveEmployee(employee);
        return "redirect:/employees";
    }

     @PostMapping("/delete/{empno}")
    public String deleteEmployee(@PathVariable Long empno) {
        employeeService.deleteEmployee(empno);
        return "redirect:/employees";
    }
}