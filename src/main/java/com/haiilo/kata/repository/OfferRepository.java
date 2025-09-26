package com.haiilo.kata.repository;

import com.haiilo.kata.domainobject.OfferDO;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfferRepository extends JpaRepository<OfferDO, Integer>
{
    List<OfferDO> findByItemIn(Set<String> items);
}
