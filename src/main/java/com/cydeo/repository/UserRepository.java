package com.cydeo.repository;

import com.cydeo.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {

    // find users by username
    User findByUserName(String username);

    @Transactional
    void deleteByUserName(String username);

    List<User> findByRole_DescriptionIgnoreCase(String description);

}
