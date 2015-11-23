package com.abdullah.threepio.codegenerator.autojson;

import com.abdullah.threepio.codegenerator.TBaseFieldVisitor;
import com.abdullah.threepio.codegenerator.TEUtils;
import com.abdullah.threepio.codegenerator.TMessager;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ErrorType;
import javax.lang.model.type.TypeMirror;

/**
 * Created by abdullah on 9/11/15.
 */
public class BaseFieldVisitor extends TBaseFieldVisitor<Void> {

    protected TMessager messager;
    protected GeneratedJsonParsable parsable;


    public BaseFieldVisitor(TEUtils teUtils, TMessager messager, GeneratedJsonParsable parsable) {
        super(teUtils);
        this.messager = messager;
        this.parsable = parsable;
    }

    public Void visitJsonParsable(DeclaredType type, VariableElement element) {
        return defaultAction(type, element);
    }

    @Override
    public Void visitDeclaredExt(DeclaredType type, VariableElement element) {
        TypeMirror parsableMirror = teUtils.getTypeMirror(Const.JSON_PARSABLE);
        if(teUtils.isSubtype(type, parsableMirror)) {
            return visitJsonParsable(type, element);
        }
        messager.printError(type.toString() + " is not supported", element);
        return super.visitDeclaredExt(type, element);
    }

    @Override
    public Void visitError(ErrorType type, VariableElement element) {
        if(teUtils.isSubtype(type, StringMirror)) {
            return visitJsonParsable(type, element);
        } else {
            messager.printNote("Error occured " + element.getSimpleName());
            messager.printNote("Error occured " + type.toString());
        }
        return null;
    }
}
