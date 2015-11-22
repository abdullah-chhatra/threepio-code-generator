package com.abdullah.threepio.codegenerator.parcelmaker;

import com.abdullah.threepio.codegenerator.TEUtils;
import com.abdullah.threepio.codegenerator.TMessager;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.util.TypeKindVisitor7;

/**
 * Created by abdullah on 2/11/15.
 */
public class PrimitiveFieldProcessor extends TypeKindVisitor7<Void, VariableElement> {

    private final TEUtils teUtils;
    private final TMessager messager;
    private final GeneratedParcelable parcelable;

    public PrimitiveFieldProcessor(TEUtils teUtils,
                                   TMessager messager,
                                   GeneratedParcelable parcelable) {
        this.teUtils = teUtils;
        this.messager = messager;
        this.parcelable = parcelable;
    }

    @Override
    public Void visitPrimitiveAsBoolean(PrimitiveType t, VariableElement element) {
        return parcelable.addBooleanStatements(element);
    }

    @Override
    public Void visitPrimitiveAsByte(PrimitiveType t, VariableElement element) {
        return parcelable.addStatements(element, "readByte", "writeByte");
    }

    @Override
    public Void visitPrimitiveAsShort(PrimitiveType t, VariableElement element) {
        messager.printError("Short type is not supported", element);
        return null;
    }

    @Override
    public Void visitPrimitiveAsInt(PrimitiveType t, VariableElement element) {
        return parcelable.addStatements(element, "readInt", "writeInt");
    }

    @Override
    public Void visitPrimitiveAsLong(PrimitiveType t, VariableElement element) {
        return parcelable.addStatements(element, "readLong", "writeLong");
    }

    @Override
    public Void visitPrimitiveAsChar(PrimitiveType t, VariableElement element) {
        messager.printError("Char type is not supported", element);
        return null;

    }

    @Override
    public Void visitPrimitiveAsFloat(PrimitiveType t, VariableElement element) {
        return parcelable.addStatements(element, "readFloat", "writeFloat");
    }

    @Override
    public Void visitPrimitiveAsDouble(PrimitiveType t, VariableElement element) {
        return parcelable.addStatements(element, "readDouble", "writeDouble");
    }
}
