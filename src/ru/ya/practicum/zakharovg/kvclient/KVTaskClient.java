package ru.ya.practicum.zakharovg.kvclient;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private URI url;
    private String token;
    private HttpClient client;

    //
    public KVTaskClient(URI url) throws IOException, InterruptedException {
        this.url = url;
        client = HttpClient.newHttpClient();
        initToken();
    }

    public void put(String key, String json) throws IOException, InterruptedException {
        HttpRequest.BodyPublisher publisher = HttpRequest.BodyPublishers.ofString(json);
        URI uri = URI.create(url + "/save/" + key + "?API_TOKEN=" + token);
        HttpRequest request = HttpRequest.newBuilder().POST(publisher).uri(uri).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public String load(String key) throws IOException, InterruptedException {
        URI uri = URI.create(url + "/load/" + key + "?API_TOKEN=" + token);
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return response.body();
        }
        return "";
    }

    private void initToken() throws IOException, InterruptedException {
        URI uri = URI.create(url + "/register");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            token = response.body();
        }
    }
}
