package com.example.demo.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;

public interface ExpediaService {

    ResponseEntity<String> jsonResponseChecker(JSONObject jsonResponse);

    JSONArray fillEnhancedPackageArray(JSONArray packageArray);

}
