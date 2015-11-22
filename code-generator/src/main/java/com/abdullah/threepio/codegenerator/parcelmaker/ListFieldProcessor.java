package com.abdullah.threepio.codegenerator.parcelmaker;

import com.abdullah.threepio.codegenerator.TEUtils;
import com.abdullah.threepio.codegenerator.TMessager;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;

/**
 * Created by abdullah on 5/11/15.
 */
public class ListFieldProcessor extends BaseFieldVisitor {

    public ListFieldProcessor(TEUtils teUtils, TMessager messager, GeneratedParcelable parcelable) {
        super(teUtils, messager, parcelable);
    }

    @Override
    public Void visitDeclaredExt(DeclaredType type, VariableElement element) {
        messager.printError("Unsupported List with generic type: " + type.toString());
        return null;
    }

    @Override
    public Void visitParcelable(DeclaredType type, VariableElement element) {
        return parcelable.addTypedListStatements(element);
    }

    @Override
    public Void visitSerializable(DeclaredType type, VariableElement element) {
        messager.printError("Serializable list is not supported", element);
        return super.visitSerializable(type, element);
    }

    @Override
    public Void visitIBinder(DeclaredType type, VariableElement element) {
        messager.printError("IBinder list is not supported", element);
        return super.visitIBinder(type, element);
    }

    @Override
    public Void visitBundle(DeclaredType type, VariableElement element) {
        messager.printError("Bundle list is not supported", element);
        return super.visitBundle(type, element);
    }

    @Override
    public Void visitString(DeclaredType type, VariableElement element) {
        return parcelable.addStringListStatements(element);
    }
}
