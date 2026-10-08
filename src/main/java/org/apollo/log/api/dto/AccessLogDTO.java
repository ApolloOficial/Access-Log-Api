package org.apollo.log.api.dto;

import org.apollo.log.api.enums.AccessStatusEnum;

import java.time.LocalDateTime;

public record AccessLogDTO(String id, String sessionId, String employeeId, String email, String userRole,
                           LocalDateTime loginTimestamp, LocalDateTime logoutTimestamp, String ipAddress,
                           AccessStatusEnum accessStatus) {
}
