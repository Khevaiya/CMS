package com.shiavnskipayroll.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultantProjectDetailsRequestDTO {
    private String position;
    private String duration;
    private String compensationType;
    private String commission;  // Can be null
    private String tds;        // Can be null
    private String bonusAmount; // Can be null
    private String remark;
    private String consultantId;
    private String projectId;//chnage

}
