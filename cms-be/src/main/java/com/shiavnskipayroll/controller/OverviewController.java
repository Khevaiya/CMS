package com.shiavnskipayroll.controller;

import com.shiavnskipayroll.service.OverviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/overview")
public class OverviewController {

    @Autowired
    private OverviewService overviewService;

    @GetMapping
    public List<Map<String, Object>> getOverview() {
        // Fetch the dynamic overview data from the service
        return overviewService.getOverviewData();
    }
}
