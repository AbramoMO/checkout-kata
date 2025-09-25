package com.haiilo.kata.domainobject;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "offers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Offer
{
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "item")
    public Item item;

    @Lob
    private String metadata;

    private Integer usageLimit;
}
