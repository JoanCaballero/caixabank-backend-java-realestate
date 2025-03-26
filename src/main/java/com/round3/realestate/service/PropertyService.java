package com.round3.realestate.service;

import com.round3.realestate.entity.Property;
import com.round3.realestate.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PropertyService {

    private final PropertyRepository propertyRepository;

    @Autowired
    public PropertyService(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    public Property save(Property property){
        return propertyRepository.save(property);
    }

    public  Property findById(Long propertyId){
        return propertyRepository.findById(propertyId).orElseThrow(()-> new RuntimeException("Property not found"));
    }
}
