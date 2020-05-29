package com.android.jetpack.basiclib.http.convert;

import com.android.jetpack.basiclib.http.api.ApiResponse;
import com.google.gson.Gson;
import com.android.jetpack.basiclib.http.exception.ApiException;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;



final class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final Type type;

    GsonResponseBodyConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        try {
            ApiResponse result = gson.fromJson(response, ApiResponse.class);
            int code = result.getCode();
            if (code == 1) {
                return gson.fromJson(response, type);
            } else {
                throw new ApiException(Integer.valueOf(code), result.getMsg());
            }
        } finally {
            value.close();
        }
    }
}