package org.apollo.log.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "access_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessLog {

    @Id
    private String id;

    private String userName;
    private String userRole;
    private LocalDateTime loginTimestamp;
    private LocalDateTime logoutTimestamp;
    private String ipAddress;
    private String accessStatus;
}
