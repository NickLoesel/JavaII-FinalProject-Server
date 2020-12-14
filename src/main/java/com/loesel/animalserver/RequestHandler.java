/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loesel.animalserver;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.System.in;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nick
 */
public class RequestHandler implements Runnable {

    private Socket socket;
    String id;
    String species;
    String gender;
    int age;
    boolean fixed;
    int legs;
    Double weight;
    String dateAdded;
    String lastFeedingTime;
    
   private Connection buildConnection() throws SQLException {
    String databaseUrl = "localhost";
    String databasePort = "3306";
    String databaseName = "animal_database";
    String userName ="root";
    String password = "password";
 
    String connectionString = "jdbc:mysql://" + databaseUrl + ":" 
                    + databasePort + "/" + databaseName + "?"
                    + "user=" + userName + "&"
                    + "password=" + password + "&"
                    + "useSSL=false" + "&"
                    + "allowPublicKeyRetrieval=true";
    return DriverManager.getConnection(connectionString);
}

    public RequestHandler(Socket socket) {
        if (null == socket) {
            throw new IllegalArgumentException("Socket cannot be null.");
        }
        this.socket = socket;
    }

    @Override
    public void run() {
        try(
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        ) {

            InetAddress inetAddress = socket.getInetAddress();
            String clientAddress = inetAddress.getHostAddress();
            System.out.println("Connection from " + clientAddress);
            Connection conn = buildConnection();
            String name = inputStream.readUTF();
            
            try (Connection connection = buildConnection()) {
        if (connection.isValid(2)) {
            CallableStatement callableStatement
                = conn.prepareCall("CALL sp_get_an_Animal(?);");
            callableStatement.setString(1, name);
            ResultSet resultSet = callableStatement.executeQuery();
            if(resultSet.next()){
                id = resultSet.getString("Animal_id");
                species = resultSet.getString("Animal_species");
                gender = resultSet.getString("Animal_gender");
                age = resultSet.getInt("Animal_age");
                legs = resultSet.getInt("Animal_legs");
                fixed = resultSet.getBoolean("Animal_fixed");
                weight = resultSet.getDouble("Animal_weight");
                dateAdded = resultSet.getString("Animal_date_added");
                lastFeedingTime = resultSet.getString("Animal_last_feeding_time");
            }
            callableStatement.close();
                conn.close();
        }
    } catch(Exception exception) {
        System.out.println("Exception message: " + exception.getMessage());
        if (exception instanceof SQLException) {
            SQLException sqlException = (SQLException) exception;
            System.out.println("Error Code: " + sqlException.getErrorCode());
            System.out.println("SQL State: " + sqlException.getSQLState());
        }
    }
            
            
                
            System.out.println("\tAnimal name: "+ name);
            outputStream.writeUTF(id); // id
            outputStream.writeUTF(name); // name
            outputStream.writeUTF(species); // specees
            outputStream.writeUTF(gender); //gender
            outputStream.writeInt(age); // age
            outputStream.writeBoolean(fixed); // fixed
            outputStream.writeInt(legs); // legs
            outputStream.writeDouble(weight); // weight
            outputStream.writeUTF(dateAdded); // date added
            outputStream.writeUTF(lastFeedingTime); // last feeding time
            outputStream.flush();

            
            
        } catch (SocketTimeoutException ste) {
            System.out.println("\tSocket connection timed out: "
                    + ste.getMessage());
        } catch (IOException ioe) {
            System.out.println("\tIO Error: " + ioe.getMessage());
        } catch (Exception ex) {
            System.out.println("\tERROR: " + ex.getMessage());
        }
    }
    
}
