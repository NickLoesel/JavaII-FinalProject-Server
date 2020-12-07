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
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 *
 * @author nick
 */
public class RequestHandler implements Runnable {

    private Socket socket;

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
            
                 BufferedReader AnimalName
          = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputStream.writeUTF("1"); // id
            outputStream.writeUTF("dog"); // specees
            outputStream.writeUTF("Male"); //gender
            outputStream.writeUTF("Lucky"); // name
            outputStream.writeInt(2); // age
            outputStream.writeBoolean(false); // fixed
            outputStream.writeInt(4); // legs
            outputStream.writeDouble(99.9); // weight
            outputStream.writeUTF("2020-12-05"); // date added
            outputStream.writeUTF("2020-12-05 17:59:59"); // last feeding time
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
