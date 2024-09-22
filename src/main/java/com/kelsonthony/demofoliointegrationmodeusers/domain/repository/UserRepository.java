package com.kelsonthony.demofoliointegrationmodeusers.domain.repository;

import com.kelsonthony.demofoliointegrationmodeusers.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
