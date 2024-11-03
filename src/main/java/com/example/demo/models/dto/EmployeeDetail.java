package com.example.demo.models.dto;

import lombok.Data;

@Data
public class EmployeeDetail {
    private Long employeeNo;
    private String employeeName;
    private String job;
    private String managerName;
    private String hireDate;
    private Double salary;
    private Double commission;
    private String departmentName;
    private String imageUrl;
}