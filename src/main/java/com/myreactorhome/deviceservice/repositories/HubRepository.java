package com.myreactorhome.deviceservice.repositories;

import com.myreactorhome.deviceservice.models.Hub;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface HubRepository extends MongoRepository<Hub, String> {
    Hub findByGroupId(Integer groupId);
    List<Hub> findByGroupIdIn(List<Integer> groupIds);
    Hub findByHardwareId(String hardware_id);
}
