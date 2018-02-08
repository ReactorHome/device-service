package com.myreactorhome.deviceservice.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface BaseReactorRepository<T, ID extends Serializable> extends MongoRepository<T, ID> {
    T findByHardwareIdIs(String hardwareId);
}
