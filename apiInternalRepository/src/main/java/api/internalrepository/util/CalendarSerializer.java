package api.internalrepository.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CalendarSerializer implements JsonSerializer<Calendar>, JsonDeserializer<Calendar> {

    @Override
    public JsonElement serialize(Calendar src, Type typeOfSrc, JsonSerializationContext context) {
        DateFormat sdf = new SimpleDateFormat("'{\"year\":'yyyy',\"month\":'MM',\"dayOfMonth\":'dd}");

        String formatedDate = sdf.format(src.getTime());
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("year", formatedDate.substring(8,12));
        jsonObject.addProperty("month", formatedDate.substring(21,23));
        jsonObject.addProperty("dayOfMonth", formatedDate.substring(37,39));
        return jsonObject;
    }

    @Override
    public Calendar deserialize(JsonElement json, Type typeOfT,  JsonDeserializationContext context) throws JsonParseException {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(json.getAsJsonPrimitive().getAsLong());
        return cal;
    }
}
