package com.round3.realestate.controller;

import com.round3.realestate.entity.Mortgage;
import com.round3.realestate.payload.MortgageRequest;
import com.round3.realestate.service.MortgageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/mortgage")
public class MortgageController {

    private MortgageService mortgageService;

    @Autowired
    public MortgageController(MortgageService mortgageService) {
        this.mortgageService = mortgageService;
    }

    @PostMapping
    public ResponseEntity<?> requestMortgage(@RequestBody MortgageRequest request, Principal principal) {
        try {
            Mortgage mortgage = mortgageService.requestMortgage(
                    principal.getName(),
                    request.getPropertyId(),
                    request.getTermYears()
            );

            return ResponseEntity.ok(Map.of(
                    "message", "Mortgage request approved",
                    "mortgageId", mortgage.getId(),
                    "monthlyPayment", mortgage.getMonthlyPayment()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }
}
