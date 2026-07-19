package org.apollo.log.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessLogDTO {

    private String id;
    private String userName;
    private String userRole;
    private LocalDateTime loginTimestamp;
    private LocalDateTime logoutTimestamp;
    private String ipAddress;
    private String accessStatus;
}
