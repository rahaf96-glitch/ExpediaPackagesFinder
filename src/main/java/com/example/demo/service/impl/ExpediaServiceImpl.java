package com.example.demo.service.impl;

import com.example.demo.service.ExpediaService;
import lombok.AllArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ExpediaServiceImpl implements ExpediaService {


    @Override
    public ResponseEntity<String> jsonResponseChecker(JSONObject jsonResponse) {
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

    @Override
    public JSONArray fillEnhancedPackageArray(JSONArray packageArray) {
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

    private String extractHotelPrice(JSONObject packageObject) {
        return packageObject.has("packagePricingInfo") && packageObject.getJSONObject("packagePricingInfo").has("formattedTotalPriceValue") ?
                packageObject.getJSONObject("packagePricingInfo").getString("formattedTotalPriceValue") :
                "0$";
    }

    private  String extractHotelImageUrl(JSONObject packageObject) {
        return packageObject.has("hotelInfo") && packageObject.getJSONObject("hotelInfo").has("hotelImageUrl") ?
                packageObject.getJSONObject("hotelInfo").getString("hotelImageUrl") :
                "https://via.placeholder.com/400";
    }

    private  double extractHotelStarRating(JSONObject packageObject) {
        return packageObject.has("hotelInfo") && packageObject.getJSONObject("hotelInfo").has("hotelStarRating") ?
                packageObject.getJSONObject("hotelInfo").getDouble("hotelStarRating") :
                0.0;
    }
}
