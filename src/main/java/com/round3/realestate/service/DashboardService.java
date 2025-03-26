package com.round3.realestate.service;

import com.round3.realestate.entity.Employment;
import com.round3.realestate.entity.Mortgage;
import com.round3.realestate.entity.User;
import com.round3.realestate.payload.DashboardResponse;
import com.round3.realestate.payload.MortgageResponse;
import com.round3.realestate.repository.EmploymentRepository;
import com.round3.realestate.repository.MortgageRepository;
import com.round3.realestate.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DashboardService {

    private UserRepository userRepository;
    private EmploymentRepository employmentRepository;
    private MortgageRepository mortgageRepository;

    @Autowired
    public DashboardService(UserRepository userRepository, EmploymentRepository employmentRepository, MortgageRepository mortgageRepository) {
        this.userRepository = userRepository;
        this.employmentRepository = employmentRepository;
        this.mortgageRepository = mortgageRepository;
    }

    public DashboardResponse getUserDashboard(String username) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Employment employment = employmentRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Employment data not found"));

        List<Mortgage> mortgages = mortgageRepository.findByUser(user);


        DashboardResponse dashboardResponse = new DashboardResponse();
        dashboardResponse.setEmploymentStatus(employment.getContractType() != null ? "Employed" : "Unemployed");
        dashboardResponse.setContractType(employment.getContractType() != null ? employment.getContractType().name() : "N/A");
        dashboardResponse.setNetMonthlySalary(employment.getNetMonthlySalary());

        List<MortgageResponse> mortgageResponses = mortgages.stream()
                .map(mortgage -> new MortgageResponse(
                        mortgage.getId(),
                        mortgage.getTotalCost(),
                        mortgage.getMonthlyPayment(),
                        mortgage.getTermYears(),
                        mortgage.getApproved()))
                .toList();

        dashboardResponse.setMortgages(mortgageResponses);

        return dashboardResponse;
    }
}
