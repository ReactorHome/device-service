package com.myreactorhome.deviceservice.repositories;

import com.myreactorhome.deviceservice.models.Hub;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface HubRepository extends MongoRepository<Hub, String> {
    Hub findByGroupId(Integer groupId);
    List<Hub> findByGroupIdIn(List<Integer> groupIds);
    Optional<Hub> findByHardwareId(String hardware_id);
    Optional<Hub> findById(String id);
}
