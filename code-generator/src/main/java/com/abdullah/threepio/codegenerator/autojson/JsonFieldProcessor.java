package com.abdullah.threepio.codegenerator.autojson;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;

import free.abdullah.threepio.codegenerator.TEUtils;
import free.abdullah.threepio.codegenerator.TMessager;

/**
 * Created by abdullah on 9/11/15.
 */
public class JsonFieldProcessor extends BaseFieldVisitor {

    private final PrimitiveFieldProcessor primitiveFieldProcessor;
    private final ArrayFieldProcessor arrayFieldProcessor;
    private final ListFieldProcessor listFieldProcessor;

    public JsonFieldProcessor(TEUtils teUtils, TMessager messager, GeneratedJsonParsable parsable) {
        super(teUtils, messager, parsable);

        primitiveFieldProcessor = new PrimitiveFieldProcessor(teUtils, messager, parsable);
        arrayFieldProcessor = new ArrayFieldProcessor(teUtils, messager, parsable);
        listFieldProcessor = new ListFieldProcessor(teUtils, messager, parsable);
    }

    @Override
    public Void visitJsonParsable(DeclaredType type, VariableElement element) {
        return parsable.addJsonParsable(element);
    }

    @Override
    public Void visitString(DeclaredType type, VariableElement element) {
        return parsable.addPrimitive(element, "getString", "optString");
    }

    @Override
    public Void visitList(DeclaredType type, TypeMirror paramType, VariableElement element) {
        if(paramType == null) {
            messager.printError("Raw lists are not supported", element);
            return null;
        }
        return paramType.accept(listFieldProcessor, element);
    }

    @Override
    public Void visitArray(ArrayType t, VariableElement element) {
        return t.getComponentType().accept(arrayFieldProcessor, element);
    }

    @Override
    public Void visitPrimitive(PrimitiveType t, VariableElement element) {
        return t.accept(primitiveFieldProcessor, element);
    }
}
