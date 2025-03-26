package com.round3.realestate.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class ScraperService {
    public Map<String, Object> scrapeProperty(String url) {
        Map<String, Object> propertyData = new HashMap<>();

        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                    .header("Accept-Language", "es-ES,es;q=0.9")
                    .header("Connection", "keep-alive")
                    .timeout(10000) // 10 segons
                    .get();

            String fullTitle = doc.select(".main-info__title-main").text();
            String propertyType = fullTitle.split(" ")[0];
            String location = doc.select(".main-info__title-minor").text();
            String priceText = doc.select(".info-data-price .txt-bold").text().replaceAll("[^0-9]", "");
            BigDecimal price = priceText.isEmpty() ? BigDecimal.ZERO : new BigDecimal(priceText);
            String sizeText = doc.select(".info-features span:nth-child(1)").text().replaceAll("[^0-9]", "");
            int size = sizeText.isEmpty() ? 0 : Integer.parseInt(sizeText);
            String roomsText = doc.select(".info-features span:nth-child(2)").text().replaceAll("[^0-9]", "");
            int rooms = roomsText.isEmpty() ? 0 : Integer.parseInt(roomsText);

            propertyData.put("name", propertyType);
            propertyData.put("title", fullTitle);
            propertyData.put("location", location);
            propertyData.put("price", price);
            propertyData.put("size", size);
            propertyData.put("rooms", rooms);

        } catch (IOException e) {
            propertyData.put("error", "No s'ha pogut obtenir la informació de la pàgina.");
        }
        return propertyData;
    }
}
