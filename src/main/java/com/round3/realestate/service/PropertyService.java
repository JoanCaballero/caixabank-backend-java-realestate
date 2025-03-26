package com.round3.realestate.service;

import com.round3.realestate.entity.Property;
import com.round3.realestate.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PropertyService {

    @Autowired
    private final PropertyRepository propertyRepository;


    public Property save(Property property){
        return propertyRepository.save(property);
    }

    public  Property findById(Long propertyId){
        return propertyRepository.findById(propertyId).orElseThrow(()-> new RuntimeException("Property not found"));
    }
}
