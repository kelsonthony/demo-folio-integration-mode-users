package com.kelsonthony.demofoliointegrationmodeusers.domain.repository;

import com.kelsonthony.demofoliointegrationmodeusers.domain.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
