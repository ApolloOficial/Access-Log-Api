package org.apollo.log.api.controller;

import lombok.RequiredArgsConstructor;
import org.apollo.log.api.dto.AccessLogDTO;
import org.apollo.log.api.service.AccessLogService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/access-logs")
@RequiredArgsConstructor
public class AccessLogController {

    private final AccessLogService accessLogService;

    @GetMapping
    public List<AccessLogDTO> findAll() {
        return accessLogService.findAll();
    }

    @GetMapping("/{id}")
    public AccessLogDTO findById(@PathVariable String id) {
        return accessLogService.findById(id);
    }

    @PostMapping
    public AccessLogDTO create(@RequestBody AccessLogDTO dto) {
        return accessLogService.create(dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        accessLogService.delete(id);
    }
}
