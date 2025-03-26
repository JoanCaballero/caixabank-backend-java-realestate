package com.round3.realestate.service;

import com.round3.realestate.entity.*;
import com.round3.realestate.entity.enumerations.Availability;
import com.round3.realestate.entity.enumerations.ContractType;
import com.round3.realestate.repository.EmploymentRepository;
import com.round3.realestate.repository.MortgageRepository;
import com.round3.realestate.repository.PropertyRepository;
import com.round3.realestate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class MortgageService {

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final PropertyRepository propertyRepository;
    @Autowired
    private final EmploymentRepository employmentRepository;
    @Autowired
    private final MortgageRepository mortgageRepository;


    private static final BigDecimal VAT_AND_FEES_PERCENTAGE = BigDecimal.valueOf(0.15);
    private static final BigDecimal INDEFINITE_THRESHOLD = BigDecimal.valueOf(0.30);
    private static final BigDecimal TEMPORARY_THRESHOLD = BigDecimal.valueOf(0.15);
    private static final BigDecimal INTEREST_RATE = BigDecimal.valueOf(0.02);
    private static final double TOLERANCE = 1e-10;

    public Mortgage requestMortgage(String username, Long propertyId, Integer termYears) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Employment employment = employmentRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Employment data not found"));

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        BigDecimal totalCost = property.getPrice().add(property.getPrice().multiply(VAT_AND_FEES_PERCENTAGE));

        BigDecimal monthlyPayment = calculateMonthlyPayment(totalCost, termYears);

        BigDecimal maxPaymentThreshold = (employment.getContractType() == ContractType.INDEFINITE)
                ? employment.getNetMonthlySalary().multiply(INDEFINITE_THRESHOLD)
                : employment.getNetMonthlySalary().multiply(TEMPORARY_THRESHOLD);

        if (monthlyPayment.compareTo(maxPaymentThreshold) > 0) {
            throw new RuntimeException("Mortgage request rejected. Monthly payment exceeds allowed threshold.");
        }

        Mortgage mortgage = new Mortgage();
        mortgage.setUser(user);
        mortgage.setProperty(property);
        mortgage.setTotalCost(totalCost);
        mortgage.setMonthlyPayment(monthlyPayment);
        mortgage.setApproved(true);
        mortgage.setTermYears(termYears);

        mortgage = mortgageRepository.save(mortgage);

        property.setAvailability(Availability.UNAVAILABLE);
        propertyRepository.save(property);

        return mortgage;
    }

    private BigDecimal calculateMonthlyPayment(BigDecimal totalCost, Integer termYears) {
        int months = termYears * 12;
        double monthlyRate = INTEREST_RATE.doubleValue()/12;
        double denominator = 1-Math.pow(1+monthlyRate, -months);
        if(Math.abs(denominator) < TOLERANCE){
            throw new RuntimeException("Mortgage request rejected. The terms of the loan result in an unfeasible monthly payment calculation.");
        }
        double monthlyPayment = totalCost.doubleValue() * monthlyRate / denominator;
        return BigDecimal.valueOf(monthlyPayment).setScale(2,RoundingMode.HALF_UP);
    }
}
