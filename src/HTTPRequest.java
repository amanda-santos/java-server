/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Amanda
 */

public class HTTPRequest {

    private String protocol;
    private String resource;
    private String method;
    private boolean keepAlive = true;
    private int timeLimit = 3000;
    private Map<String, List<String>> headers;

    public static HTTPRequest readRequest(InputStream input) throws IOException {
        HTTPRequest request = new HTTPRequest();
        BufferedReader buffer = new BufferedReader(new InputStreamReader(input));
        
        System.out.println("Request: ");
        // lê a primeira linha que contém as informaçoes da requisição
        String requestLine = buffer.readLine();
        System.out.println(requestLine);
        
        // quebra a string pelo espaço em branco
        String[] reqData = requestLine.split(" ");
        
        // recebe o método
        request.setMethod(reqData[0]);
        
        // recebe o caminho do arquivo
        request.setResource(reqData[1]);
        
        // recebe o protocolo
        request.setProtocol(reqData[2]);
        
        String headerData = buffer.readLine();
        
        // enquanto a linha não for nula e não for vazia
        while (headerData != null && !headerData.isEmpty()) {
            System.out.println(headerData);
            String[] headerLine = headerData.split(":");
            request.setHeader(headerLine[0], headerLine[1].trim().split(","));
            headerData = buffer.readLine();
        }
        
        // se existir a chave "Connection" no cabeçalho
        if (request.getHeaders().containsKey("Connection")) {
            // envia para o keepAlive a conexão se o "Connection" for "keep-alive"
            request.setKeepAlive(request.getHeaders().get("Connection").get(0).equals("keep-alive"));
        }
        return request;
    }

    public void setHeader(String key, String... values) {
        if (headers == null) {
            headers = new TreeMap<>();
        }
        headers.put(key, Arrays.asList(values));
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void setHeaders(Map header) {
        this.headers = header;
    }

    public boolean isKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getResource() {
        return resource;
    }

    public String getMethod() {
        return method;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

}