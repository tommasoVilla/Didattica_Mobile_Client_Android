package it.uniroma2.dicii.sdcc.didatticamobile.dao;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import it.uniroma2.dicii.sdcc.didatticamobile.config.AppConfiguration;
import it.uniroma2.dicii.sdcc.didatticamobile.error.ParserException;

/*
 * A JSONParser is an utility object that provides methods used to map JSON string to object
 * */
public class JSONParser {

    /**
     * fromStreamToObject is the method that convert the json inside an InputStream in an object
     * of the given class
     * @param inputStream from which the JSON is read
     * @param c the class of the object to build
     * @return {@code Object} the built object
     * */
    public Object fromStreamToObject(InputStream inputStream, Class<?> c) throws ParserException {
        try {
            String jsonString = readStream(inputStream);
            // Gson library is used to simplify conversion
            Gson gson = new Gson();
            JsonReader jsonReader = new JsonReader(new StringReader(jsonString));
            jsonReader.setLenient(true);

            return gson.fromJson(jsonReader, c);

        } catch (IOException e){
            throw new ParserException(e.getMessage(), e.getCause());
        }
    }

    /**
     * fromStreamToList is the method that converts the json inside an InputStrema in a list of object of type
     * listType
     * @param inputStream from which the JSON is read
     * @param listType the Type of a list item.
     * @return {@code <T>List} the built list
     * Note that listType is usually retrieved invoking new TypeToken<Collection<T>>(){}.getType()
     * where T is the type of a list item. This is necessary to solve the issue about the Generic Type
     * erasure in Java.
     * * */
    public <T> T fromStreamToList(InputStream inputStream, Type listType) throws ParserException {
        try {
            String jsonString = readStream(inputStream);

            Gson gson = new Gson();
            JsonReader jsonReader = new JsonReader(new StringReader(jsonString));
            jsonReader.setLenient(true);

            return gson.fromJson(jsonReader, listType);
        } catch (IOException e) {
            throw new ParserException(e.getMessage(), e.getCause());
        }
    }

    /**
     * Converts an {@code InputStream} object into the corresponding JSON string.
     * @param inputStream the stream codifying JSON string
     * @return the JSON string codified by the stream.
     * */
    private String readStream(InputStream inputStream) throws IOException {

        StringBuilder stringBuilder = new StringBuilder();
        int bufferSize = Integer.parseInt(AppConfiguration.getInstance().readProperty("buffer_size"));
        byte[] buffer = new byte[bufferSize];
        while (inputStream.read(buffer) != -1) {
            String stringChunk = new String(buffer);
            stringBuilder.append(stringChunk);
        }
        return stringBuilder.toString();

    }


}
