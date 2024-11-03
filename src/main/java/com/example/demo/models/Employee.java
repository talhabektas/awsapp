
package com.example.demo.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import java.util.Date;

@Entity
@Data
@Table(name = "employee")

public class Employee {
    @Id
    private Long empno;
    private String ename;
    private String job;
    private Long mgr;
    private Date hiredate;
    private Double sal;
    private Double comm;
    private Integer deptno;
    private String img;
}