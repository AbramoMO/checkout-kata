package com.haiilo.kata.repository;

import com.haiilo.kata.domainobject.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfferRepository extends JpaRepository<Offer, String>
{
}
