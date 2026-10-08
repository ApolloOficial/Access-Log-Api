package org.apollo.log.api.service;

import org.apollo.log.api.dto.AccessLogCreateDTO;
import org.apollo.log.api.dto.AccessLogDTO;
import org.apollo.log.api.enums.AccessStatusEnum;
import org.apollo.log.api.exception.BusinessRuleException;
import org.apollo.log.api.exception.ResourceNotFoundException;
import org.apollo.log.api.model.AccessLog;
import org.apollo.log.api.repository.AccessLogRepository;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AccessLogServiceTest {

    private static final String EMPLOYEE = "11111111-1111-1111-1111-111111111111";

    private final AccessLogRepository repository = mock(AccessLogRepository.class);
    private final MongoTemplate mongoTemplate = mock(MongoTemplate.class);
    private final AccessLogService service = new AccessLogService(repository, mongoTemplate);

    @Test
    void createFillsSessionIdAndLoginTimestamp() {
        when(repository.save(any(AccessLog.class))).thenAnswer(inv -> inv.getArgument(0));

        AccessLogDTO result = service.create(new AccessLogCreateDTO(
                null, EMPLOYEE, " Renata.Moraes@ambarenergia.demo ", "GERENTE", null, "10.0.0.1",
                AccessStatusEnum.SUCESSO));

        assertNotNull(result.sessionId());
        assertNotNull(result.loginTimestamp());
        assertEquals("renata.moraes@ambarenergia.demo", result.email());
    }

    @Test
    void successfulLoginWithoutEmployeeIsRejected() {
        assertThrows(BusinessRuleException.class, () -> service.create(new AccessLogCreateDTO(
                null, null, "ninguem@exemplo.com", null, null, null, AccessStatusEnum.SUCESSO)));
        verify(repository, never()).save(any());
    }

    @Test
    void failedLoginWithoutEmployeeIsAccepted() {
        when(repository.save(any(AccessLog.class))).thenAnswer(inv -> inv.getArgument(0));

        AccessLogDTO result = service.create(new AccessLogCreateDTO(
                null, null, "ninguem@exemplo.com", null, null, null, AccessStatusEnum.FALHA));

        assertEquals(AccessStatusEnum.FALHA, result.accessStatus());
    }

    @Test
    void logoutTwiceIsRejected() {
        AccessLog log = new AccessLog();
        log.setId("1");
        log.setAccessStatus(AccessStatusEnum.SUCESSO);
        log.setLoginTimestamp(LocalDateTime.now().minusHours(1));
        log.setLogoutTimestamp(LocalDateTime.now());
        when(repository.findById("1")).thenReturn(Optional.of(log));

        assertThrows(BusinessRuleException.class, () -> service.registerLogout("1"));
    }

    @Test
    void logoutSetsTimestamp() {
        AccessLog log = new AccessLog();
        log.setId("1");
        log.setAccessStatus(AccessStatusEnum.SUCESSO);
        log.setLoginTimestamp(LocalDateTime.now().minusHours(1));
        when(repository.findById("1")).thenReturn(Optional.of(log));
        when(repository.save(any(AccessLog.class))).thenAnswer(inv -> inv.getArgument(0));

        assertNotNull(service.registerLogout("1").logoutTimestamp());
    }

    @Test
    void findByIdMissingThrowsNotFound() {
        when(repository.findById("x")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findById("x"));
    }
}
