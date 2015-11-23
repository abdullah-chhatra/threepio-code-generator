package com.abdullah.threepio.threepio.codegenerator.app;

import com.abdullah.threepio.autodata.json.AutoJson;
import com.abdullah.threepio.autodata.json.JsonField;
import com.abdullah.threepio.autodata.parcel.AutoParcel;

/**
 * Created by abdullah on 23/11/15.
 */
@AutoParcel
@AutoJson
public class AddressBase {

    @JsonField
    String street;

    @JsonField
    String city;

    @JsonField
    String zipCode;
}
