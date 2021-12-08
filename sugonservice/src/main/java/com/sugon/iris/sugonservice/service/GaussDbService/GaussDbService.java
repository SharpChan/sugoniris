package com.sugon.iris.sugonservice.service.GaussDbService;


import java.sql.Connection;
import java.io.IOException;
import java.sql.SQLException;

public interface GaussDbService {

    void copyFromFile(Connection connection, String filePath, String tableName)
            throws SQLException, IOException;
}
