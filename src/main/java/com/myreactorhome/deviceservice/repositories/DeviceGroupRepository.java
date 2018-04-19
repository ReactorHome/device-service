package com.myreactorhome.deviceservice.repositories;

import com.myreactorhome.deviceservice.models.DeviceGroup;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface DeviceGroupRepository extends MongoRepository<DeviceGroup, String> {
    Optional<DeviceGroup> findById(String id);
}
