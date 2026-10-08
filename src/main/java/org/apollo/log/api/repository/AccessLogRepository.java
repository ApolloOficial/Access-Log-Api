package org.apollo.log.api.repository;

import org.apollo.log.api.model.AccessLog;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AccessLogRepository extends MongoRepository<AccessLog, String> {
    Optional<AccessLog> findBySessionId(String sessionId);
}
