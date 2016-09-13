package com.oncreate.ariadna.Adapters;

import android.util.SparseArray;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;

public class SparseArrayTypeAdapter<T> extends TypeAdapter<SparseArray<T>> {
    private final Class<T> classOfT;
    private final Gson gson;
    private final Type typeOfSparseArrayOfObject;
    private final Type typeOfSparseArrayOfT;

    /* renamed from: com.sololearn.core.web.SparseArrayTypeAdapter.1 */
    class C13101 extends TypeToken<SparseArray<T>> {
        C13101() {
        }
    }

    /* renamed from: com.sololearn.core.web.SparseArrayTypeAdapter.2 */
    class C13112 extends TypeToken<SparseArray<Object>> {
        C13112() {
        }
    }

    public SparseArrayTypeAdapter(Class<T> classOfT, Gson gson) {
        this.typeOfSparseArrayOfT = new C13101().getType();
        this.typeOfSparseArrayOfObject = new C13112().getType();
        this.classOfT = classOfT;
        this.gson = gson;
    }

    public void write(JsonWriter jsonWriter, SparseArray<T> tSparseArray) throws IOException {
        if (tSparseArray == null) {
            jsonWriter.nullValue();
        } else {
            this.gson.toJson(this.gson.toJsonTree(tSparseArray, this.typeOfSparseArrayOfT), jsonWriter);
        }
    }

    public SparseArray<T> read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }
        SparseArray<Object> temp = (SparseArray) this.gson.fromJson(jsonReader, this.typeOfSparseArrayOfObject);
        SparseArray<T> result = new SparseArray(temp.size());
        for (int i = 0; i < temp.size(); i++) {
            int key = temp.keyAt(i);
            result.put(key, this.gson.fromJson(this.gson.toJsonTree(temp.get(key)), this.classOfT));
        }
        return result;
    }
}
