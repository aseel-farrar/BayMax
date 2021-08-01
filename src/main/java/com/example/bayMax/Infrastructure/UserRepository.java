package com.example.bayMax.Infrastructure;

import com.example.bayMax.Domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users,Long> {
    Users findUsersByUsername(String username);
}
