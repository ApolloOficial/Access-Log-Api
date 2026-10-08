package org.apollo.log.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apollo.log.api.enums.AccessStatusEnum;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

// Espelha a tabela access_log do PostgreSQL (employee_id, attempted_email, session_id, ...).
@Document(collection = "access_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessLog {

    @Id
    private String id;

    // UUID da sessao (mesmo conceito do session_id do PostgreSQL); unico.
    @Indexed(unique = true)
    private String sessionId;

    // UUID do funcionario no PostgreSQL; nulo quando o e-mail digitado nao existe.
    private String employeeId;

    @Indexed
    private String email;
    private String userRole;

    @Indexed
    private LocalDateTime loginTimestamp;
    private LocalDateTime logoutTimestamp;
    private String ipAddress;
    private AccessStatusEnum accessStatus;
}
