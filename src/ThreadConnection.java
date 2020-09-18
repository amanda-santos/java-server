/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.file.Files;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Amanda
 */

public class ThreadConnection implements Runnable {

    private final Socket socket;
    private boolean connected;

    public ThreadConnection(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        connected = true;
        // imprime na tela o IP do cliente
        System.out.println(socket.getInetAddress());
        while (connected) {
            try {
                // cria uma requisição a partir do InputStream do cliente
                HTTPRequest request = HTTPRequest.readRequest(socket.getInputStream());
                // se a conexão esta marcada para se manter viva então seta keepAlive e o timeout
                if (request.isKeepAlive()) {
                    socket.setKeepAlive(true);
                    socket.setSoTimeout(request.getTimeLimit());
                } else {
                    // se não seta um valor menor suficiente para uma requisição
                    socket.setSoTimeout(300);
                }

                //se o caminho foi igual a / então deve pegar o /index.html
                if (request.getResource().equals("/")) {
                    request.setResource("index.html");
                }
                //abre o arquivo pelo caminho
                File file = new File(request.getResource().replaceFirst("/", ""));

                HTTPResponse response;

                //se o arquivo existir então criamos a reposta de sucesso, com status 200
                if (file.exists()) {
                    response = new HTTPResponse(request.getProtocol(), 200, "OK");
                } else {
                    //se o arquivo não existe então criamos a reposta de erro, com status 404
                    response = new HTTPResponse(request.getProtocol(), 404, "Not Found");
                    file = new File("404.html");
                }
                // lê todo o conteúdo do arquivo para bytes e gera o conteudo de response
                response.setResponseContent(Files.readAllBytes(file.toPath()));
                // converte o formato para o GMT espeficicado pelo protocolo HTTP
                String formattedDate = Util.formatDateGMT(new Date());
                // cabeçalho padrão da response HTTP/1.1
                response.setHeader("Location", "http://localhost:8000/");
                response.setHeader("Date", formattedDate);
                response.setHeader("Server", "MeuServidor/1.0");
                response.setHeader("Content-Type", "text/html");
                response.setHeader("Content-Length", response.getResponseSize());
                // cria o canal de response utilizando o outputStream
                response.setOutput(socket.getOutputStream());
                response.send();
            } catch (IOException ex) {
                // quando o tempo limite terminar encerra a thread
                if (ex instanceof SocketTimeoutException) {
                    try {
                        connected = false;
                        socket.close();
                    } catch (IOException ex1) {
                        Logger.getLogger(ThreadConnection.class.getName()).log(Level.SEVERE, null, ex1);
                    }
                }
            }

        }
    }

}

