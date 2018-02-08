package com.myreactorhome.deviceservice.repositories;

import com.myreactorhome.deviceservice.models.Hub;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HubRepository extends MongoRepository<Hub, String> {
}
