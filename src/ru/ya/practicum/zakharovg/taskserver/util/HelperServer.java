package ru.ya.practicum.zakharovg.taskserver.util;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import ru.ya.practicum.zakharovg.taskserver.model.MessageResp;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class HelperServer {
    private static final String RESPONSE_CODE_400_MSG = "Вы использовали неверный запрос.";
    private static final String RESPONSE_CODE_405_MSG = "Метод запроса не поддерживается.";
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public static void sendResponse(HttpExchange exchange, String response, int statusCode) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=utf-8");
        switch (statusCode) {
            case 204:
                exchange.sendResponseHeaders(statusCode, -1);
                response = null;
            default:
                exchange.sendResponseHeaders(statusCode, 0);
        }

        try (OutputStream os = exchange.getResponseBody()) {
            if (response != null || !response.isBlank()) {
                os.write(response.getBytes());
            }
        }
        exchange.close();
    }

    public static void responseCode200(HttpExchange exchange, String response) throws IOException {
        sendResponse(exchange, response, 200);
    }

    public static void responseCode201(HttpExchange exchange, Gson gson, String msg) throws IOException {
        responseCode(exchange, gson, msg, 201);
    }

    public static void responseCode204(HttpExchange exchange) throws IOException {
        responseCode(exchange, null, null, 204);
    }

    public static void responseCode400(HttpExchange exchange, Gson gson) throws IOException {
        responseCode(exchange, gson, RESPONSE_CODE_400_MSG, 400);
    }

    public static void responseCode404(HttpExchange exchange, Gson gson, String msg) throws IOException {
        responseCode(exchange, gson, msg, 404);
    }

    // не верный метод запроса OPTION, HEAD ...
    public static void responseCode405(HttpExchange exchange, Gson gson) throws IOException {
        responseCode(exchange, gson, RESPONSE_CODE_405_MSG, 405);
    }

    public static void responseCode422(HttpExchange exchange, Gson gson, String msg) throws IOException {
        responseCode(exchange, gson, msg, 422);
    }

    private static void responseCode(HttpExchange exchange, Gson gson, String msg, int statusCode) throws IOException {
        if (gson != null) {
            MessageResp message = new MessageResp(msg);
            sendResponse(exchange, gson.toJson(message), statusCode);
        } else {
            sendResponse(exchange, msg, statusCode);
        }
    }

    public static int getIdFromQueries(String[] queries) {
        String[] pair = queries[0].split("=");
        if (pair[0].toLowerCase().equals("id")) {
            try {
                return Integer.valueOf(pair[1]);
            } catch (NumberFormatException e) {
                return -1;
            }
        }
        return -1;
    }
}
