package com.round3.realestate.controller;

import com.round3.realestate.entity.enumerations.ContractType;
import com.round3.realestate.entity.Employment;
import com.round3.realestate.payload.EmploymentRequest;
import com.round3.realestate.service.EmploymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/employment")
public class EmploymentController {

    private EmploymentService employmentService;

    @Autowired
    public EmploymentController(EmploymentService employmentService) {
        this.employmentService = employmentService;
    }

    @PostMapping
    public ResponseEntity<?> updateEmployment(@RequestBody EmploymentRequest request, Principal principal) {
        ContractType contractType = request.getContractType();
        BigDecimal grossSalary = request.getGrossSalary();

        Employment employment = employmentService.updateEmployment(principal.getName(), contractType, grossSalary);

        return ResponseEntity.ok(Map.of(
                "message", "Employment data updated successfully",
                "netMonthlySalary", employment.getNetMonthlySalary()
        ));
    }
}