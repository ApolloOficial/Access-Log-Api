package org.apollo.log.api.service;

import lombok.RequiredArgsConstructor;
import org.apollo.log.api.dto.AccessLogDTO;
import org.apollo.log.api.exception.ResourceNotFoundException;
import org.apollo.log.api.model.AccessLog;
import org.apollo.log.api.repository.AccessLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccessLogService {

    private final AccessLogRepository accessLogRepository;

    public List<AccessLogDTO> findAll() {
        return accessLogRepository.findAll().stream().map(this::toDTO).toList();
    }

    public AccessLogDTO findById(String id) {
        AccessLog accessLog = accessLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Log de acesso não encontrado: " + id));
        return toDTO(accessLog);
    }

    public AccessLogDTO create(AccessLogDTO dto) {
        AccessLog accessLog = toEntity(dto);
        if (accessLog.getLoginTimestamp() == null) {
            accessLog.setLoginTimestamp(LocalDateTime.now());
        }
        return toDTO(accessLogRepository.save(accessLog));
    }

    public void delete(String id) {
        if (!accessLogRepository.existsById(id)) {
            throw new ResourceNotFoundException("Log de acesso não encontrado: " + id);
        }
        accessLogRepository.deleteById(id);
    }

    private AccessLogDTO toDTO(AccessLog accessLog) {
        return new AccessLogDTO(
                accessLog.getId(),
                accessLog.getUserName(),
                accessLog.getUserRole(),
                accessLog.getLoginTimestamp(),
                accessLog.getLogoutTimestamp(),
                accessLog.getIpAddress(),
                accessLog.getAccessStatus()
        );
    }

    private AccessLog toEntity(AccessLogDTO dto) {
        return new AccessLog(
                null,
                dto.getUserName(),
                dto.getUserRole(),
                dto.getLoginTimestamp(),
                dto.getLogoutTimestamp(),
                dto.getIpAddress(),
                dto.getAccessStatus()
        );
    }
}
