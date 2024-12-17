package com.shiavnskipayroll.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultantProjectDetailsResponseDTO {
    private String id; // MongoDB document ID
    private String position;
    private String duration;
    private String compensationType;
    private String commission;
    private String tds;
    private String bonusAmount;
    private String remark;
    private String consultantId;
    private String projectId;
    
}
