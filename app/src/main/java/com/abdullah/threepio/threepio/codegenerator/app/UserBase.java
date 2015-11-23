package com.abdullah.threepio.threepio.codegenerator.app;

import com.abdullah.threepio.autodata.json.AutoJson;
import com.abdullah.threepio.autodata.json.JsonField;
import com.abdullah.threepio.autodata.parcel.AutoParcel;

/**
 * Created by abdullah on 23/11/15.
 */
@AutoJson
@AutoParcel
public class UserBase {

    @JsonField
    String name;

    @JsonField
    String designation;

    @JsonField
    int age;

    @JsonField
    Address address;

    public UserBase() {

    }

    public UserBase(String name, String designation) {

    }
}
