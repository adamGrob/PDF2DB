package com.PDF2DB.db;

import com.PDF2DB.db.DBContract;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class TestConnection {


    public static void main(String[] args) {
        try {
            Class.forName("org.postgresql.Driver");
            Connection c = DriverManager.getConnection(
                    DBContract.HOST,
                    DBContract.USERNAME,
                    DBContract.PASSWORD);

            System.out.println("DB connected");

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }

}