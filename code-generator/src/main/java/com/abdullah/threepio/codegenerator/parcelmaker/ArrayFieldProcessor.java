package com.abdullah.threepio.codegenerator.parcelmaker;

import com.abdullah.threepio.codegenerator.TEUtils;
import com.abdullah.threepio.codegenerator.TMessager;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.util.TypeKindVisitor7;

/**
 * Created by abdullah on 3/11/15.
 */
public class ArrayFieldProcessor extends BaseFieldVisitor {

    private final PrimitiveArrayFieldProcessor primitiveProcessor;

    public ArrayFieldProcessor(TEUtils teUtils,
                               TMessager messager,
                               GeneratedParcelable parcelable) {
        super(teUtils, messager, parcelable);

        primitiveProcessor = new PrimitiveArrayFieldProcessor(teUtils, messager, parcelable);
    }

    @Override
    public Void visitDeclaredExt(DeclaredType type, VariableElement element) {
        return super.visitDeclaredExt(type, element);
    }

    @Override
    public Void visitParcelable(DeclaredType type, VariableElement element) {
        return parcelable.addLoadableStatements(element, "createTypedArray", "writeTypedArray");
    }

    @Override
    public Void visitSerializable(DeclaredType type, VariableElement element) {
        messager.printError("Serializable array is not supported", element);
        return defaultAction(type, element);
    }

    @Override
    public Void visitIBinder(DeclaredType type, VariableElement element) {
        messager.printError("IBinder array is not supported", element);
        return defaultAction(type, element);
        //return parcelable.addStatements(element, "createBinderArray", "writeBinderArray");
    }

    @Override
    public Void visitBundle(DeclaredType type, VariableElement element) {
        return parcelable.addLoadableStatements(element, "createTypedArray", "writeTypedArray");
    }

    @Override
    public Void visitString(DeclaredType type, VariableElement element) {
        return parcelable.addStatements(element, "createStringArray", "writeStringArray");
    }

    @Override
    public Void visitPrimitive(PrimitiveType t, VariableElement element) {
        return t.accept(primitiveProcessor, element);
    }
}

class PrimitiveArrayFieldProcessor extends TypeKindVisitor7<Void, VariableElement> {

    private final TEUtils teUtils;
    private final TMessager messager;
    private final GeneratedParcelable parcelable;

    public PrimitiveArrayFieldProcessor(TEUtils teUtils,
                                        TMessager messager,
                                        GeneratedParcelable parcelable) {
        this.teUtils = teUtils;
        this.messager = messager;
        this.parcelable = parcelable;
    }

    @Override
    public Void visitPrimitiveAsBoolean(PrimitiveType t, VariableElement element) {
        return parcelable.addStatements(element, "createBooleanArray", "writeBooleanArray");
    }

    @Override
    public Void visitPrimitiveAsByte(PrimitiveType t, VariableElement element) {
        return parcelable.addStatements(element, "createByteArray", "writeByteArray");
    }

    @Override
    public Void visitPrimitiveAsInt(PrimitiveType t, VariableElement element) {
        return parcelable.addStatements(element, "createIntArray", "writeIntArray");
    }

    @Override
    public Void visitPrimitiveAsLong(PrimitiveType t, VariableElement element) {
        return parcelable.addStatements(element, "createLongArray", "writeLongArray");
    }

    @Override
    public Void visitPrimitiveAsChar(PrimitiveType t, VariableElement element) {
        return parcelable.addStatements(element, "createCharArray", "writeCharArray");
    }

    @Override
    public Void visitPrimitiveAsFloat(PrimitiveType t, VariableElement element) {
        return parcelable.addStatements(element, "createFloatArray", "writeFloatArray");
    }

    @Override
    public Void visitPrimitiveAsDouble(PrimitiveType t, VariableElement element) {
        return parcelable.addStatements(element, "createDoubleArray", "writeDoubleArray");
    }
}
