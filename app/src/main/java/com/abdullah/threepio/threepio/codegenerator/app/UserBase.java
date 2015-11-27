package com.abdullah.threepio.threepio.codegenerator.app;

import android.os.Parcel;

import com.abdullah.threepio.autodata.json.AutoJson;
import com.abdullah.threepio.autodata.json.JsonField;
import com.abdullah.threepio.autodata.json.JsonParseListener;
import com.abdullah.threepio.autodata.parcel.AutoParcel;
import com.abdullah.threepio.autodata.parcel.ParcelListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by abdullah on 27/11/15.
 */
@AutoParcel
@AutoJson
public class UserBase implements ParcelListener, JsonParseListener {

    @JsonField
    String firstName;

    @Override
    public void onReadParcel(Parcel in) {

    }

    @Override
    public void onWriteParcel(Parcel out, int flags) {

    }

    @Override
    public void onReadJson(JSONObject in) throws JSONException {

    }

    @Override
    public void onWriteJson(JSONObject out) throws JSONException {

    }
}
