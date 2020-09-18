/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Amanda
 */
public class HTTPResponse {

    private String protocol;
    private int responseCode;
    private String message;
    private byte[] responseContent;
    private Map<String, List<String>> headers;
    private OutputStream output;

    public HTTPResponse() {

    }

    public HTTPResponse(String protocol, int responseCode, String message) {
        this.protocol = protocol;
        this.responseCode = responseCode;
        this.message = message;
    }

    /**
     * envia os dados da resposta ao cliente
     *
     * @throws IOException
     */
    public void send() throws IOException {
        // escreve o headers em bytes
        output.write(mountHeader());
        // escreve o conteúdo em bytes
        output.write(responseContent);
        // encerra a resposta
        output.flush();
    }

    /**
     * insere um item de cabeçalho no mapa
     *
     * @param key
     * @param values lista com um ou mais valores para esta chave
     */
    public void setHeader(String key, String... values) {
        if (headers == null) {
            headers = new TreeMap<>();
        }
        headers.put(key, Arrays.asList(values));
    }

    /**
     * pega o tamanho da resposta em bytes
     *
     * @return retorna o valor em bytes do tamanho do conteúdo da resposta convertido em string
     */
    public String getResponseSize() {
        return getResponseContent().length + "";
    }

    /**
     * converte o cabeçalho em string
     *
     * @return retorna o cabeçalho em bytes
     */
    private byte[] mountHeader() {
        return this.toString().getBytes();
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(protocol).append(" ").append(responseCode).append(" ").append(message).append("\r\n");
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            str.append(entry.getKey());
            String correctedString = Arrays.toString(entry.getValue().toArray()).replace("[", "").replace("]", "");
            str.append(": ").append(correctedString).append("\r\n");
        }
        str.append("\r\n");
        return str.toString();
    }

    public void setResponseContent(byte[] responseContent) {
        this.responseContent = responseContent;
    }

    public void setOutput(OutputStream output) {
        this.output = output;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
    }

    public byte[] getResponseContent() {
        return responseContent;
    }

    public OutputStream getOutput() {
        return output;
    }

}