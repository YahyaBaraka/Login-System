package org.example.jwt.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmissionDTO {
    private Long submissionId;
    private Long userId;
    private String language;
}
