package com.example.VideoRent.dao;

import com.example.VideoRent.entity.Rental;
import org.springframework.stereotype.Repository;

@Repository
public class RentalDao extends CommonDaoImpl<Rental, Long> {

    public RentalDao() {
        super(Rental.class);
    }
}
