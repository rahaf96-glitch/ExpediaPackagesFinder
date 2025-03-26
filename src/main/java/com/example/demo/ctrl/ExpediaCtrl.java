package com.example.demo.ctrl;

import com.example.demo.service.ExpediaService;
import lombok.AllArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
@AllArgsConstructor
@RestController
public class ExpediaCtrl {

    private static final String EXPEDIA_API_URL = "https://offersvc.expedia.com/offers/v2/getOffers?scenario=deal-finder&page=foo&uid=test&productType=Package&clientId=test";
    private final ExpediaService expediaService;

    @GetMapping("/api/packages")
    @CrossOrigin(origins = "https://expediapackagesfinder-4.onrender.com")
    public ResponseEntity<?> getPackages(@RequestParam String origin, @RequestParam String destination) {
        String url = EXPEDIA_API_URL + "&originCity=" + origin + "&destinationCity=" + destination;
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);
        JSONObject jsonResponse = new JSONObject(response);
        ResponseEntity<String> enhancedPackageArray = expediaService.jsonResponseChecker(jsonResponse);
        if (enhancedPackageArray != null) return enhancedPackageArray;
        return ResponseEntity.ok(new JSONArray().toString());
    }

}
