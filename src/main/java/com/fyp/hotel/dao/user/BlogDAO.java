package com.fyp.hotel.dao.user;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BlogDAO {

    @Autowired
    private SessionFactory sessionFactory;


}
