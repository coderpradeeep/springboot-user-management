package org.example.repository;

import org.example.entities.UserInfo;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;


@Repository
public interface UserRepo extends CrudRepository<UserInfo, Long> {

    public UserInfo findByUsername(String username);
}