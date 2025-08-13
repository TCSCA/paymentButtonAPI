package api.apiAdminCommerce.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class ThrowableTypeAdapter extends TypeAdapter<Throwable> {

    @Override
    public void write(JsonWriter out, Throwable value) throws IOException {
        out.beginObject();
        out.name("message").value(value.getMessage());
        out.name("stackTrace").beginArray();
        for (StackTraceElement element : value.getStackTrace()) {
            out.value(element.toString());
        }
        out.endArray();
        out.endObject();
    }

    @Override
    public Throwable read(JsonReader in) throws IOException {
        return null;
    }

}
