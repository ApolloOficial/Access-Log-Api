package org.apollo.log.api.service;

import lombok.RequiredArgsConstructor;
import org.apollo.log.api.dto.AccessLogCreateDTO;
import org.apollo.log.api.dto.AccessLogDTO;
import org.apollo.log.api.enums.AccessStatusEnum;
import org.apollo.log.api.exception.BusinessRuleException;
import org.apollo.log.api.exception.ResourceNotFoundException;
import org.apollo.log.api.model.AccessLog;
import org.apollo.log.api.repository.AccessLogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AccessLogService {

    private final AccessLogRepository accessLogRepository;
    private final MongoTemplate mongoTemplate;

    public Page<AccessLogDTO> findAll(String email, AccessStatusEnum status, LocalDateTime from,
                                      LocalDateTime to, Pageable pageable) {
        List<Criteria> filters = new ArrayList<>();
        if (email != null && !email.isBlank()) {
            filters.add(Criteria.where("email").regex(Pattern.quote(email.trim()), "i"));
        }
        if (status != null) {
            filters.add(Criteria.where("accessStatus").is(status));
        }
        if (from != null) {
            filters.add(Criteria.where("loginTimestamp").gte(from));
        }
        if (to != null) {
            filters.add(Criteria.where("loginTimestamp").lte(to));
        }
        Query query = filters.isEmpty()
                ? new Query()
                : new Query(new Criteria().andOperator(filters.toArray(new Criteria[0])));

        long total = mongoTemplate.count(query, AccessLog.class);
        List<AccessLogDTO> content = mongoTemplate.find(query.with(pageable), AccessLog.class)
                .stream().map(this::toDTO).toList();
        return new PageImpl<>(content, pageable, total);
    }

    public AccessLogDTO findById(String id) {
        return toDTO(findLog(id));
    }

    public AccessLogDTO create(AccessLogCreateDTO dto) {
        AccessLog accessLog = new AccessLog();
        accessLog.setSessionId(dto.sessionId() != null ? dto.sessionId().toLowerCase() : UUID.randomUUID().toString());
        accessLog.setEmployeeId(dto.employeeId() != null ? dto.employeeId().toLowerCase() : null);
        accessLog.setEmail(dto.email().trim().toLowerCase());
        accessLog.setUserRole(dto.userRole());
        accessLog.setLoginTimestamp(dto.loginTimestamp() != null ? dto.loginTimestamp() : LocalDateTime.now());
        accessLog.setIpAddress(dto.ipAddress());
        accessLog.setAccessStatus(dto.accessStatus());
        // Mesmas regras do PostgreSQL: login com sucesso exige funcionario cadastrado.
        if (accessLog.getAccessStatus() == AccessStatusEnum.SUCESSO && accessLog.getEmployeeId() == null) {
            throw new BusinessRuleException("A successful login requires the employee id");
        }
        return toDTO(accessLogRepository.save(accessLog));
    }

    /** Registra o logout de uma sessao (ck_access_period: logout nunca antes do login). */
    public AccessLogDTO registerLogout(String id) {
        AccessLog accessLog = findLog(id);
        if (accessLog.getLogoutTimestamp() != null) {
            throw new BusinessRuleException("Logout already registered for this session");
        }
        if (accessLog.getAccessStatus() == AccessStatusEnum.FALHA) {
            throw new BusinessRuleException("A failed login has no session to end");
        }
        LocalDateTime now = LocalDateTime.now();
        accessLog.setLogoutTimestamp(now.isBefore(accessLog.getLoginTimestamp()) ? accessLog.getLoginTimestamp() : now);
        return toDTO(accessLogRepository.save(accessLog));
    }

    public void delete(String id) {
        if (!accessLogRepository.existsById(id)) {
            throw new ResourceNotFoundException("Access log not found: " + id);
        }
        accessLogRepository.deleteById(id);
    }

    private AccessLog findLog(String id) {
        return accessLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Access log not found: " + id));
    }

    private AccessLogDTO toDTO(AccessLog log) {
        return new AccessLogDTO(log.getId(), log.getSessionId(), log.getEmployeeId(), log.getEmail(),
                log.getUserRole(), log.getLoginTimestamp(), log.getLogoutTimestamp(), log.getIpAddress(),
                log.getAccessStatus());
    }
}
