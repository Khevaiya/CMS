package com.shiavnskipayroll.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class ClientMasterResponseDTO {
    private String id;

    private String clientName;
    private String clientContactEmailId1;
    private String clientContactEmailId2;
    private String clientContactPhone1;
    private String clientContactPhone2;
    private String clientAddress;
    private String state;
    private String city;
    private String poc1Email;
    private String poc1ContactNo;
    private String poc2Email;
    private String poc2ContactNo;
    private String gstNumber;
    private String panNumber;
    private String companyWebsite;
    private Boolean isActive;
    private String clientSowSignedUrl; // URL or path to the file
    private Double tdsPercentage;
    private Double gstPercentage;
    private String createdBy;
    private String createdDate;
    private String lastUpdatedBy;
    private String lastUpdatedDate;
    //-------------------------
    private List<String> listOfProjectIds;
}
