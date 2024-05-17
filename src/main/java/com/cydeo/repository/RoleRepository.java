package com.cydeo.repository;

import com.cydeo.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.beans.JavaBean;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {

    // find roles based on description given
    Role findByDescription(String description);

}
