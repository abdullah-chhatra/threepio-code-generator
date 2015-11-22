package com.abdullah.threepio.codegenerator.autojson;

import java.util.Map;

/**
 * Created by abdullah on 11/11/15.
 */
public class Options {

    public static final String ADD_SUFFIX = "addSuffix";
    public static final String REMOVE_SUFFIX = "removeSuffix";
    public static final String KEY_CASE = "jsonKeyCase";
    public static final String VAR_CASE = "varCase";

    private final String addSuffix;
    private final String removeSuffix;
    private final CaseOptions keyCase;
    private final CaseOptions varCase;

    public Options(Map<String, String> options) {
        addSuffix = options.get(ADD_SUFFIX);
        removeSuffix = options.get(REMOVE_SUFFIX);
        if(options.containsKey(KEY_CASE)) {
            keyCase = CaseOptions.enumOf(options.get(KEY_CASE));
        } else {
            keyCase = CaseOptions.None;
        }

        if(options.containsKey(VAR_CASE)) {
            varCase = CaseOptions.enumOf(options.get(VAR_CASE));
        } else {
            varCase = CaseOptions.LowerCamel;
        }
    }

    public String getAddSuffix() {
        return addSuffix;
    }

    public String getRemoveSuffix() {
        return removeSuffix;
    }

    public CaseOptions getKeyCase() {
        return keyCase;
    }

    public CaseOptions getVarCase() {
        return varCase;
    }
}


