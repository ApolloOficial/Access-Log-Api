package org.apollo.log.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.apollo.log.api.enums.AccessStatusEnum;

import java.time.LocalDateTime;

public record AccessLogCreateDTO(
        @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
                message = "Session id must be a UUID") String sessionId,
        @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
                message = "Employee id must be a UUID") String employeeId,
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email")
        @Size(max = 120, message = "Email must be at most 120 characters") String email,
        @Size(max = 30, message = "Role must be at most 30 characters") String userRole,
        LocalDateTime loginTimestamp,
        @Size(max = 45, message = "IP address must be at most 45 characters") String ipAddress,
        @NotNull(message = "Access status is required") AccessStatusEnum accessStatus) {
}
