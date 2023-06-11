package com.laurynas.uzduotis.services;

import com.laurynas.uzduotis.properties.DatabaseProperties;
import com.laurynas.uzduotis.properties.JpaProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabaseService {
    public final DatabaseProperties databaseProperties;
    public final JpaProperties jpaProperties;

    @Autowired
    public DatabaseService(DatabaseProperties databaseProperties, JpaProperties jpaProperties) {
        this.databaseProperties = databaseProperties;
        this.jpaProperties = jpaProperties;
    }
}
