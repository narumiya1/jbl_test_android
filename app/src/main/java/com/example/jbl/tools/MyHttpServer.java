package com.example.jbl.tools;

import com.example.jbl.model.DinasData;
import com.example.jbl.model.TarifRequest;
import com.example.jbl.model.TcmCommand;
import com.example.jbl.model.UserData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;

public class MyHttpServer implements AutoCloseable {
  private final HttpServer mHttpServer;
  private MyCallback callback;
  public MyHttpServer(MyCallback _callback)
  {
    callback = _callback;
    try {
      mHttpServer = HttpServer.create(new InetSocketAddress(11711), 0);
      mHttpServer.setExecutor(Executors.newCachedThreadPool());
      mHttpServer.createContext("/tcm_open", tcmOpenHandler);
      mHttpServer.createContext("/tcm_close", tcmCloseHandler);
      mHttpServer.createContext("/tcm_set_tarif", setTarifHandler);
      mHttpServer.createContext("/tcm_set_user", setUserHandler);
      mHttpServer.createContext("/tcm_dinas_whitelist", setDinasHandler);
      mHttpServer.start();

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  @Override
  public void close() {
    mHttpServer.stop(1); // delay 1 detik
  }

  private void sendResponse(HttpExchange httpExchange, int responseCode, String responseText) throws IOException {
    httpExchange.sendResponseHeaders(responseCode, (long)responseText.length());
    var os = httpExchange.getResponseBody();
    os.write(responseText.getBytes());
    os.close();
  }

  private String streamToString(InputStream inputStream) throws IOException {
    StringBuilder stringBuilder = new StringBuilder();
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
      String line;
      while ((line = reader.readLine()) != null) {
        stringBuilder.append(line);
      }
    }
    return stringBuilder.toString();
  }

  private final HttpHandler setTarifHandler = exchange -> {
    if (exchange.getRequestMethod().equals("POST")) {
      try {
        String s = streamToString(exchange.getRequestBody());
        //JSONObject jsonBody = new JSONObject(s);
        Gson gson = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
          gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
            @Override
            public LocalDateTime deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
              return LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_ZONED_DATE_TIME);
            }
          }).create();
        }

        if (callback != null) callback.onSetTarif(gson.fromJson(s, TarifRequest.class));
        sendResponse(exchange, 200, "OK");
      } catch (Exception e) {
        String responseText = "ERROR: "+e.getMessage();
        sendResponse(exchange, 404, responseText);
      }
    }
  };

  private final HttpHandler tcmCloseHandler = new HttpHandler() {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
      // Get request method
      if (exchange.getRequestMethod().equals("POST")) {
        InputStream inputStream = exchange.getRequestBody();
        String requestBody = streamToString(inputStream);
        if (callback != null) {
          Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer()).create();
          callback.onTcmCommandClose(gson.fromJson(requestBody, TcmCommand.TcmCommandClose.class));
        }
        sendResponse(exchange, 200, "Welcome to my server");
      }
    }
  };

  private final HttpHandler tcmOpenHandler = new HttpHandler() {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
      switch (httpExchange.getRequestMethod()) {
        case "GET":
          // Get all messages
          sendResponse(httpExchange, 200, "Would be all messages stringified json");
          break;
        case "POST":
          InputStream inputStream = httpExchange.getRequestBody();
          String requestBody = streamToString(inputStream);
          try {
            if (callback != null) {
              Gson gson = null;
              if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer()).create();
              }
              callback.onTcmCommandOpen(gson.fromJson(requestBody, TcmCommand.TcmCommandOpen.class));
            }
            sendResponse(httpExchange, 200, "OK");
          } catch (Exception e) {
            sendResponse(httpExchange, 404, Objects.requireNonNull(e.getMessage()));
          }
          break;
      }
    }
  };

  private final HttpHandler setUserHandler = new HttpHandler() {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
      if (exchange.getRequestMethod().equals("POST")) {
        InputStream inputStream = exchange.getRequestBody();
        String requestBody = streamToString(inputStream);
        if (callback != null) {
          Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer()).create();
          Type userListType = new TypeToken<List<UserData>>(){}.getType();
          List<UserData> userList = gson.fromJson(requestBody, userListType);
          callback.onSetUser(userList);
        }
        sendResponse(exchange, 200, "Welcome to my server");
      }
    }
  };

  private final HttpHandler setDinasHandler = new HttpHandler() {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
      if (exchange.getRequestMethod().equals("POST")) {
        InputStream inputStream = exchange.getRequestBody();
        String requestBody = streamToString(inputStream);
        if (callback != null) {
          Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer()).create();
          Type dinasListType = new TypeToken<List<DinasData>>(){}.getType();
          List<DinasData> dinasList = gson.fromJson(requestBody, dinasListType);
          callback.onSetDinas(dinasList);
        }
        sendResponse(exchange, 200, "Welcome to my server");
      }
    }
  };
  public interface MyCallback {
    void onSetTarif(TarifRequest result);
    void onTcmCommandOpen(TcmCommand.TcmCommandOpen result);
    void onTcmCommandClose(TcmCommand.TcmCommandClose result);
    void onSetUser(List<UserData> result);
    void onSetDinas(List<DinasData> result);
  }

  private static final class LocalDateTimeDeserializer implements JsonDeserializer<LocalDateTime> {
    @Override
    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
      return LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_ZONED_DATE_TIME);
    }
  }
}

