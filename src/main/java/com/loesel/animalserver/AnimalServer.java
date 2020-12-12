  /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loesel.animalserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author nickloesel
 */
public class AnimalServer {


    public static void main(String[] args) {

        int port = 5555;
        int threadCount = 100;
        int timeoutLength = 30000;

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        try{
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.printf("ServerSocket listening on port %d\n", port);

            Socket socket;
            while(true){
                socket = serverSocket.accept();
                socket.setSoTimeout(timeoutLength);
                executorService.execute(new RequestHandler(socket));
            }

        } catch (IOException ex) {
            System.out.println("CATASTROPHIC FAILURE:  Server Stopping!");
            System.out.println(ex.getMessage());
        }
    }

}
    

