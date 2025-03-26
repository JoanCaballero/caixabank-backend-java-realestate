package com.round3.realestate.service;

import com.round3.realestate.entity.enumerations.ContractType;
import com.round3.realestate.entity.Employment;
import com.round3.realestate.entity.User;
import com.round3.realestate.repository.EmploymentRepository;
import com.round3.realestate.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class EmploymentService {

    private EmploymentRepository employmentRepository;
    private UserRepository userRepository;

    @Autowired
    public EmploymentService(EmploymentRepository employmentRepository, UserRepository userRepository) {
        this.employmentRepository = employmentRepository;
        this.userRepository = userRepository;
    }

    public Employment updateEmployment(String username, ContractType contractType, BigDecimal grossSalary) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        BigDecimal netMonthlySalary = calculateNetSalary(grossSalary);

        Employment employment = employmentRepository.findByUser(user)
                .orElse(new Employment());

        employment.setUser(user);
        employment.setContractType(contractType);
        employment.setGrossSalary(grossSalary);
        employment.setNetMonthlySalary(netMonthlySalary);

        return employmentRepository.save(employment);
    }

    private BigDecimal calculateNetSalary(BigDecimal grossSalary) {
        BigDecimal netSalary = BigDecimal.ZERO;
        BigDecimal remainingSalary = grossSalary;

        if (remainingSalary.compareTo(BigDecimal.valueOf(12450)) > 0) {
            netSalary = netSalary.add(BigDecimal.valueOf(12450).multiply(BigDecimal.valueOf(0.81)));
            remainingSalary = remainingSalary.subtract(BigDecimal.valueOf(12450));
        } else {
            netSalary = netSalary.add(remainingSalary.multiply(BigDecimal.valueOf(0.81)));
            return netSalary.divide(BigDecimal.valueOf(12), RoundingMode.HALF_UP);
        }

        if (remainingSalary.compareTo(BigDecimal.valueOf(20199).subtract(BigDecimal.valueOf(12450))) > 0) {
            netSalary = netSalary.add(BigDecimal.valueOf(20199).subtract(BigDecimal.valueOf(12450)).multiply(BigDecimal.valueOf(0.76)));
            remainingSalary = remainingSalary.subtract(BigDecimal.valueOf(20199).subtract(BigDecimal.valueOf(12450)));
        } else {
            netSalary = netSalary.add(remainingSalary.multiply(BigDecimal.valueOf(0.76)));
            return netSalary.divide(BigDecimal.valueOf(12), RoundingMode.HALF_UP);
        }

        if (remainingSalary.compareTo(BigDecimal.valueOf(35199).subtract(BigDecimal.valueOf(20199))) > 0) {
            netSalary = netSalary.add(BigDecimal.valueOf(35199).subtract(BigDecimal.valueOf(20199)).multiply(BigDecimal.valueOf(0.70)));
            remainingSalary = remainingSalary.subtract(BigDecimal.valueOf(35199).subtract(BigDecimal.valueOf(20199)));
        } else {
            netSalary = netSalary.add(remainingSalary.multiply(BigDecimal.valueOf(0.70)));
            return netSalary.divide(BigDecimal.valueOf(12), RoundingMode.HALF_UP);
        }

        if (remainingSalary.compareTo(BigDecimal.valueOf(59999).subtract(BigDecimal.valueOf(35199))) > 0) {
            netSalary = netSalary.add(BigDecimal.valueOf(59999).subtract(BigDecimal.valueOf(35199)).multiply(BigDecimal.valueOf(0.63)));
            remainingSalary = remainingSalary.subtract(BigDecimal.valueOf(59999).subtract(BigDecimal.valueOf(35199)));
        } else {
            netSalary = netSalary.add(remainingSalary.multiply(BigDecimal.valueOf(0.63)));
            return netSalary.divide(BigDecimal.valueOf(12), RoundingMode.HALF_UP);
        }

        if (remainingSalary.compareTo(BigDecimal.valueOf(299999).subtract(BigDecimal.valueOf(59999))) > 0) {
            netSalary = netSalary.add(BigDecimal.valueOf(299999).subtract(BigDecimal.valueOf(59999)).multiply(BigDecimal.valueOf(0.55)));
            remainingSalary = remainingSalary.subtract(BigDecimal.valueOf(299999).subtract(BigDecimal.valueOf(59999)));
        } else {
            netSalary = netSalary.add(remainingSalary.multiply(BigDecimal.valueOf(0.55)));
            return netSalary.divide(BigDecimal.valueOf(12), RoundingMode.HALF_UP);
        }

        netSalary = netSalary.add(remainingSalary.multiply(BigDecimal.valueOf(0.50)));

        return netSalary.divide(BigDecimal.valueOf(12), RoundingMode.HALF_UP);
    }
}
