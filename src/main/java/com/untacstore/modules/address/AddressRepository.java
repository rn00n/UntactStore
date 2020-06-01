package com.untacstore.modules.address;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly=true)
public interface AddressRepository extends JpaRepository<Address, Long> {
}
