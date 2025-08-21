package org.springboot.repository;

import org.springboot.entities.UserInfo;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;


@Repository
public interface UserRepo extends CrudRepository<UserInfo, Long> {

    public UserInfo findByUsername(String username);
}