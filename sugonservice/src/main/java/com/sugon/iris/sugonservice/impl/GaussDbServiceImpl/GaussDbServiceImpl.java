package com.sugon.iris.sugonservice.impl.GaussDbServiceImpl;

import com.sugon.iris.sugonservice.service.GaussDbService.GaussDbService;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.io.FileInputStream;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;

public class GaussDbServiceImpl implements GaussDbService {
    @Override
    public void copyFromFile(Connection connection, String filePath, String tableName) throws SQLException, IOException {


        FileInputStream fileInputStream = null;

        try {
            CopyManager copyManager = new CopyManager((BaseConnection)connection);
            fileInputStream = new FileInputStream(filePath);
            copyManager.copyIn("COPY " + tableName + " FROM STDIN", fileInputStream);
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
