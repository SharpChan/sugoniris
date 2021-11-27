package com.sugon.iris.sugonservice.service.GaussDbService;


import java.sql.Connection;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.SQLException;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;

public interface GaussDbService {

    void copyFromFile(Connection connection, String filePath, String tableName)
            throws SQLException, IOException;
}
