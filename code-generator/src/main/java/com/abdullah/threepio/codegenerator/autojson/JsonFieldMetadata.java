package com.abdullah.threepio.codegenerator.autojson;

import com.sun.codemodel.JExpression;

/**
 * Created by abdullah on 10/11/15.
 */
public class JsonFieldMetadata {

    String key;
    boolean optional;
    JExpression defaultValue;

    public JsonFieldMetadata() {
        key = "";
        optional = false;
        defaultValue = null;
    }
}
