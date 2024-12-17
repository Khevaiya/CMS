package com.shiavnskipayroll.service;

import com.shiavnskipayroll.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Service
public class OverviewService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private InternRepository internRepository;

    @Autowired
    private ConsultantMasterRepository consultantRepository;

    public List<Map<String, Object>> getOverviewData() {
        List<Map<String, Object>> overviewList = new ArrayList<>();

        // Fetching counts from each repository and constructing the overview list
        overviewList.add(Map.of("title", "Total Employees", "count", employeeRepository.count()));
        overviewList.add(Map.of("title", "Total Projects", "count", projectRepository.count()));
        overviewList.add(Map.of("title", "Total Clients", "count", clientRepository.count()));

        overviewList.add(Map.of("title", "Total Interns", "count", internRepository.count()));
        overviewList.add(Map.of("title", "Total Consultants", "count", consultantRepository.count()));

        // Returning the list of overview items
        return overviewList;
    }
    }
