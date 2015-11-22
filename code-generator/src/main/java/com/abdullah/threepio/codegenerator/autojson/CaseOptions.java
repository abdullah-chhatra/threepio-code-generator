package com.abdullah.threepio.codegenerator.autojson;

import com.google.common.base.CaseFormat;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 * Created by abdullah on 11/11/15.
 */
public enum CaseOptions {

    None("none"),
    LowerCamel("lower_camel"),
    UpperCamel("upper_camel"),
    LowerSnake("lower_snake"),
    UpperSnake("upper_snake"),
    LowerHyphen("lower_hyphen"),
    UpperHyphen("upper_hyphen");

    private String value;
    private static Map<String, CaseOptions> enumMap;

    CaseOptions(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    static {
        ImmutableMap.Builder<String, CaseOptions> builder = ImmutableMap.builder();
        for (CaseOptions value : CaseOptions.values()) {
            builder.put(value.getValue(), value);
        }
        enumMap = builder.build();
    }

    public static CaseOptions enumOf(String value) {
        CaseOptions e = enumMap.get(value);
        if (e == null) {
            throw new IllegalArgumentException("In CaseOptions no matching element found with value - " + value);
        }
        return e;
    }


    protected CaseFormat getCaseFormat() {
        switch (this) {
            case LowerCamel:
                return CaseFormat.LOWER_CAMEL;

            case UpperCamel:
                return CaseFormat.UPPER_CAMEL;

            case LowerHyphen:
                return CaseFormat.LOWER_HYPHEN;

            case LowerSnake:
                return CaseFormat.LOWER_UNDERSCORE;

            case UpperSnake:
                return CaseFormat.UPPER_UNDERSCORE;

            default:
                return null;
        }
    }

    public String convert(String string, CaseOptions to) {
        CaseFormat fromFormat = getCaseFormat();
        CaseFormat toFormat = to.getCaseFormat();

        if(fromFormat == null || toFormat == null) {
            return string;
        }
        return fromFormat.converterTo(toFormat).convert(string);
    }
}