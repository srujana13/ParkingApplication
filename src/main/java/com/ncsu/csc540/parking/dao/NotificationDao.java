package com.ncsu.csc540.parking.dao;


import com.ncsu.csc540.parking.domain.Citation;
import com.ncsu.csc540.parking.domain.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;


@Repository
@Transactional
public class NotificationDao extends JdbcDaoSupport {
    @Autowired
    public NotificationDao(DataSource dataSource) {
        this.setDataSource(dataSource);
    }

    public void save(Notification notification) {

            String sql = "INSERT INTO NOTIFICATIONS (N_ID, MESSAGE, ISSUED_TO)"
                    + " VALUES (?,?,?)";
            getJdbcTemplate().update(sql,notification.getNId(), notification.getMessage(),
                    notification.getIssuedTo());
        }

    }
