package com.abdullah.threepio.codegenerator.autojson;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.TypeKindVisitor7;

import free.abdullah.threepio.codegenerator.TEUtils;
import free.abdullah.threepio.codegenerator.TMessager;

/**
 * Created by abdullah on 10/11/15.
 */
public class ArrayFieldProcessor extends BaseFieldVisitor {

    private final PrimitiveArrayProcessor primitiveArrayProcessor;

    public ArrayFieldProcessor(TEUtils teUtils, TMessager messager, GeneratedJsonParsable parsable) {
        super(teUtils, messager, parsable);
        primitiveArrayProcessor = new PrimitiveArrayProcessor(messager, parsable);
    }

    @Override
    public Void visitJsonParsable(DeclaredType type, VariableElement element) {
        return parsable.addJsonParsableArray(element);
    }

    @Override
    public Void visitString(DeclaredType type, VariableElement element) {
        return parsable.addArray(element, "readStringArray");
    }

    @Override
    public Void visitPrimitive(PrimitiveType t, VariableElement element) {
        return t.accept(primitiveArrayProcessor, element);
    }

    @Override
    protected Void defaultAction(TypeMirror e, VariableElement element) {
        messager.printError(e.toString() + " is not supported", element);
        return super.defaultAction(e, element);
    }
}

class PrimitiveArrayProcessor extends TypeKindVisitor7<Void, VariableElement> {

    final TMessager messager;
    final GeneratedJsonParsable parsable;

    public PrimitiveArrayProcessor(TMessager messager, GeneratedJsonParsable parsable) {
        this.messager = messager;
        this.parsable = parsable;
    }

    @Override
    public Void visitPrimitiveAsBoolean(PrimitiveType t, VariableElement element) {
        return parsable.addArray(element, "readBoolArray");
    }

    @Override
    public Void visitPrimitiveAsInt(PrimitiveType t, VariableElement element) {
        return parsable.addArray(element, "readIntArray");
    }

    @Override
    public Void visitPrimitiveAsLong(PrimitiveType t, VariableElement element) {
        return parsable.addArray(element, "readLongArray");
    }

    @Override
    public Void visitPrimitiveAsDouble(PrimitiveType t, VariableElement element) {
        return parsable.addArray(element, "readDoubleArray");
    }

    @Override
    protected Void defaultAction(TypeMirror e, VariableElement element) {
        messager.printError(e.toString() + " is not supported", element);
        return super.defaultAction(e, element);
    }
}