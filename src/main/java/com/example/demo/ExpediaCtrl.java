package com.example.demo;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ExpediaCtrl {

    private static final String EXPEDIA_API_URL = "https://offersvc.expedia.com/offers/v2/getOffers?scenario=deal-finder&page=foo&uid=test&productType=Package&clientId=test";

    @GetMapping("/api/packages")
    @CrossOrigin(origins = "https://expediapackagesfinder-4.onrender.com")
    public ResponseEntity<?> getPackages(@RequestParam String origin, @RequestParam String destination) {
        String url = EXPEDIA_API_URL + "&originCity=" + origin + "&destinationCity=" + destination;
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);
        JSONObject jsonResponse = new JSONObject(response);
        ResponseEntity<String> enhancedPackageArray = jsonResponseChecker(jsonResponse);
        if (enhancedPackageArray != null) return enhancedPackageArray;
        return ResponseEntity.ok(new JSONArray().toString());
    }

    private ResponseEntity<String> jsonResponseChecker(JSONObject jsonResponse) {
        if (jsonResponse.has("offers")) {
            JSONObject offers = jsonResponse.getJSONObject("offers");
            if (offers.has("Package")) {
                JSONArray packageArray = offers.getJSONArray("Package");
                JSONArray enhancedPackageArray = fillEnhancedPackageArray(packageArray);
                return ResponseEntity.ok(enhancedPackageArray.toString());
            }
        }
        return null;
    }

    private JSONArray fillEnhancedPackageArray(JSONArray packageArray) {
        JSONArray enhancedPackageArray = new JSONArray();
        for (int i = 0; i < packageArray.length(); i++) {
            JSONObject packageObject = packageArray.getJSONObject(i);
            String hotelImageUrl = extractHotelImageUrl(packageObject);
            String price = extractHotelPrice(packageObject);
            double hotelStarRating = extractHotelStarRating(packageObject);

            packageObject.put("hotelImageUrl", hotelImageUrl);
            packageObject.put("formattedTotalPriceValue", price);
            packageObject.put("hotelStarRating", hotelStarRating);

            enhancedPackageArray.put(packageObject);
        }
        return enhancedPackageArray;
    }

    private static String extractHotelPrice(JSONObject packageObject) {
        return packageObject.has("packagePricingInfo") && packageObject.getJSONObject("packagePricingInfo").has("formattedTotalPriceValue") ?
                packageObject.getJSONObject("packagePricingInfo").getString("formattedTotalPriceValue") :
                "0$";
    }

    private static String extractHotelImageUrl(JSONObject packageObject) {
        return packageObject.has("hotelInfo") && packageObject.getJSONObject("hotelInfo").has("hotelImageUrl") ?
                packageObject.getJSONObject("hotelInfo").getString("hotelImageUrl") :
                "https://via.placeholder.com/400";
    }

    private static double extractHotelStarRating(JSONObject packageObject) {
        return packageObject.has("hotelInfo") && packageObject.getJSONObject("hotelInfo").has("hotelStarRating") ?
                packageObject.getJSONObject("hotelInfo").getDouble("hotelStarRating") :
                0.0;
    }

}
