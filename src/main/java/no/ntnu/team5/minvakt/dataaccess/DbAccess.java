package no.ntnu.team5.minvakt.dataaccess;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Harald Floor Wilhelmsen on 10.01.2017.
 */
@Transactional
public class DbAccess {

    @Autowired
    SessionFactory sessionFactory;


}
