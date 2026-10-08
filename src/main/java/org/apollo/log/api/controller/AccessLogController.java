package org.apollo.log.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apollo.log.api.dto.AccessLogCreateDTO;
import org.apollo.log.api.dto.AccessLogDTO;
import org.apollo.log.api.enums.AccessStatusEnum;
import org.apollo.log.api.service.AccessLogService;
import org.apollo.log.api.util.PageParams;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/access-logs")
@RequiredArgsConstructor
@Tag(name = "Access Logs", description = "Registro de acessos (login/logout) em MongoDB")
@SecurityRequirement(name = "api-key")
public class AccessLogController {

    private final AccessLogService accessLogService;

    public enum SortField { loginTimestamp, logoutTimestamp, email, accessStatus }

    @GetMapping
    @Operation(summary = "List access logs (paginated) with optional filters")
    public Page<AccessLogDTO> findAll(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) AccessStatusEnum status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "loginTimestamp") SortField sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction) {
        return accessLogService.findAll(email, status, from, to,
                PageParams.of(page, size, direction, sortBy.name()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find access log by ID")
    public AccessLogDTO findById(@PathVariable String id) {
        return accessLogService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register an access (login attempt)")
    public AccessLogDTO create(@Valid @RequestBody AccessLogCreateDTO dto) {
        return accessLogService.create(dto);
    }

    @PatchMapping("/{id}/logout")
    @Operation(summary = "Register the logout of a session")
    public AccessLogDTO registerLogout(@PathVariable String id) {
        return accessLogService.registerLogout(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete an access log")
    public void delete(@PathVariable String id) {
        accessLogService.delete(id);
    }
}
