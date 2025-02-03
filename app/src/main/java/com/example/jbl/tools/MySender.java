package com.example.jbl.tools;

import android.util.Log;

import com.example.jbl.Config;
import com.google.gson.*;


import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MySender implements Runnable {
  private final Queue<Params> queue = new LinkedList<>();
  private volatile boolean isRunning = true;
  private final Config cfg = Config.getInstance();

  public MySender() {

//    Object newObj = Class.forName("EMoneyData").cast(a);
//    enqueue(newObj,"");
//    EMoneyData mobj = (EMoneyData) newObj;
    //MyClass mobj = MyClass.class.cast(obj);
  }

  public void enqueue(Object obj, String path) {
    queue.add(new Params(obj, path));
  }

  public String test(Object obj) {
    Gson gson = new GsonBuilder()
        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer())
        .serializeNulls()
        .create();
//    Gson gson = new GsonBuilder()
//        .registerTypeAdapter(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
//          @Override
//          public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
//            ZoneOffset zoneOffset = src.atZone(ZoneId.systemDefault()).getOffset();
//            return context.serialize(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)+zoneOffset);
//          }
//        })
//        .serializeNulls()
//        .create();
//    Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) -> {
//          if (src == null) return JsonNull.INSTANCE;
//          else {
//            ZoneOffset zoneOffset = src.atZone(ZoneId.systemDefault()).getOffset();
//            return context.serialize(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)+zoneOffset);
//          }
//    })
//        .serializeNulls()
//        .create();
    return gson.toJson(obj);
  }

  public void close() { isRunning = false; }

  @Override
  public void run() {
    Params pp = null;
    while (isRunning) {
      try {
        if (!queue.isEmpty()) {
          pp = queue.peek();
        }
        if (pp != null) {
          SendToTcm(pp);
        }
      }catch (InterruptedException e) {
        Log.d("SENDER", Objects.requireNonNull(e.getMessage()));
      }
    }
  }

  private void SendToTcm(Params pp) throws InterruptedException {
    Gson gson = new GsonBuilder()
        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer())
        .serializeNulls()
        .create();
    String json = gson.toJson(pp.obj);
    String ip = cfg.getIpTcm();
    OkHttpClient client = new OkHttpClient();

    RequestBody body = RequestBody.create(MediaType.get("application/json"), json);
    Request request = new Request.Builder()
        .url("http://"+ip.trim()+":12621"+pp.path.trim())
        .post(body)
        .build();
    try {
      try (Response response = client.newCall(request).execute()) {
        assert response.body() != null;
        if (!response.body().string().equals("OK")) {
          return;
        }
      }
    } catch (IOException e) {
      // Masukin balik ke antrian, agar bisa dikirim ulang
      enqueue(pp.obj, pp.path);
      Thread.sleep(5000);
    }
  }

  private static class Params {
    public Object obj;
    public String path;
    public Params(Object o, String p) { obj = o; path = p; }
  }

//  private static final class LocalDateAdapter extends TypeAdapter<LocalDateTime> {
//    @Override
//    public void write(final JsonWriter jsonWriter, final LocalDateTime localDate ) throws IOException {
//      jsonWriter.value(localDate.toString());
//    }
//
//    @Override
//    public LocalDateTime read( final JsonReader jsonReader ) throws IOException {
//      //jsonReader.nextNull();
//      return LocalDateTime.parse(jsonReader.nextString());
//    }
//  }

  private static final class LocalDateTimeSerializer implements JsonSerializer<LocalDateTime> {
    @Override
    public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
      if (src == null) return JsonNull.INSTANCE;
      else {
        ZoneOffset zoneOffset = src.atZone(ZoneId.systemDefault()).getOffset();
        return context.serialize(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)+zoneOffset);
      }
    }
  }
}
