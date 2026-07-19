package org.apollo.log.api.repository;

import org.apollo.log.api.model.AccessLog;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AccessLogRepository extends MongoRepository<AccessLog, String> {
    List<AccessLog> findByUserName(String userName);
}
