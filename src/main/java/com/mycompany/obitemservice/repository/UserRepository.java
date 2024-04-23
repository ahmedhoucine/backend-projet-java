package com.mycompany.obitemservice.repository;

import com.mycompany.obitemservice.model.UserModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<UserModel, String> {
    UserModel findByUsername(String username);
}
