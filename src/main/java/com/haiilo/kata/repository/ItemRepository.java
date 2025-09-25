package com.haiilo.kata.repository;

import com.haiilo.kata.domainobject.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, String>
{
}
