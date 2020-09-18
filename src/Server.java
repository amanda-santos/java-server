/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Amanda
 */

public class Server {

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(8000);
        ExecutorService pool = Executors.newFixedThreadPool(20);

        while (true) {
            pool.execute(new ThreadConnection(server.accept()));
        }
    }
}