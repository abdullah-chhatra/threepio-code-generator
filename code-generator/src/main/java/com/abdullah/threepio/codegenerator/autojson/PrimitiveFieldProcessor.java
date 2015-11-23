package com.abdullah.threepio.codegenerator.autojson;

import com.abdullah.threepio.codegenerator.TEUtils;
import com.abdullah.threepio.codegenerator.TMessager;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.TypeKindVisitor7;

/**
 * Created by abdullah on 9/11/15.
 */
public class PrimitiveFieldProcessor extends TypeKindVisitor7<Void, VariableElement> {

    private final TEUtils teUtils;
    private final TMessager messager;
    private final GeneratedJsonParsable parcelable;

    public PrimitiveFieldProcessor(TEUtils teUtils,
                                   TMessager messager,
                                   GeneratedJsonParsable parcelable) {
        this.teUtils = teUtils;
        this.messager = messager;
        this.parcelable = parcelable;
    }

    @Override
    public Void visitPrimitiveAsBoolean(PrimitiveType t, VariableElement element) {
        return parcelable.addPrimitive(element, "getBoolean", "optBoolean");
    }

    @Override
    public Void visitPrimitiveAsInt(PrimitiveType t, VariableElement element) {
        return parcelable.addPrimitive(element, "getInt", "optInt");
    }

    @Override
    public Void visitPrimitiveAsLong(PrimitiveType t, VariableElement element) {
        return parcelable.addPrimitive(element, "getLong", "optLong");
    }

    @Override
    public Void visitPrimitiveAsDouble(PrimitiveType t, VariableElement element) {
        return parcelable.addPrimitive(element, "getDouble", "optDouble");
    }

    @Override
    protected Void defaultAction(TypeMirror e, VariableElement element) {
        messager.printError(e.toString() + " is not supported", element);
        return super.defaultAction(e, element);
    }
}
