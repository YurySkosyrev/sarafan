package com.sarafan.repo;

import com.sarafan.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailRepo extends JpaRepository<User, String> {
}
