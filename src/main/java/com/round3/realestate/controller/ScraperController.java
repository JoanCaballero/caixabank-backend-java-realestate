package com.round3.realestate.controller;

import com.round3.realestate.entity.enumerations.Availability;
import com.round3.realestate.entity.Property;
import com.round3.realestate.repository.PropertyRepository;
import com.round3.realestate.service.ScraperService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/scrape")
public class ScraperController {
    private ScraperService scraperService;
    private PropertyRepository propertyRepository;

    @Autowired
    public ScraperController(ScraperService scraperService, PropertyRepository propertyRepository) {
        this.scraperService = scraperService;
        this.propertyRepository = propertyRepository;
    }

    @PostMapping
    public ResponseEntity<?> scrape(@RequestBody Map<String, Object> payload) {
        String url = (String) payload.get("url");
        boolean store = (boolean) payload.getOrDefault("store", false);

        if (url == null || url.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "URL is required"));
        }

        Map<String, Object> propertyData = scraperService.scrapeProperty(url);

        if (store && !propertyData.containsKey("error")) {
            try {
                Property property = new Property();
                property.setName(String.valueOf(propertyData.get("name")));
                property.setTitle(String.valueOf(propertyData.get("title")));
                property.setLocation(String.valueOf(propertyData.get("location")));
                property.setPrice(new BigDecimal(String.valueOf(propertyData.get("price"))));
                property.setSize((Integer) propertyData.getOrDefault("size", 0));
                property.setRooms((Integer) propertyData.getOrDefault("rooms", 0));
                property.setAvailability(Availability.AVAILABLE);

                propertyRepository.save(property);
            } catch (Exception e){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Failed to store property data", "details", e.getMessage()));
            }
        }

        propertyData.put("stored", store);
        return ResponseEntity.ok(propertyData);
    }
}
