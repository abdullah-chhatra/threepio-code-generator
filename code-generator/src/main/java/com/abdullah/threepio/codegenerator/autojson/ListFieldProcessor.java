package com.abdullah.threepio.codegenerator.autojson;

import com.abdullah.threepio.codegenerator.TEUtils;
import com.abdullah.threepio.codegenerator.TMessager;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

/**
 * Created by abdullah on 10/11/15.
 */
public class ListFieldProcessor extends BaseFieldVisitor {

    public ListFieldProcessor(TEUtils teUtils, TMessager messager, GeneratedJsonParsable parsable) {
        super(teUtils, messager, parsable);
    }

    @Override
    public Void visitJsonParsable(DeclaredType type, VariableElement element) {
        return parsable.addJsonParsableList(element);
    }

    @Override
    public Void visitString(DeclaredType type, VariableElement element) {
        return parsable.addStringList(element);
    }

    @Override
    protected Void defaultAction(TypeMirror e, VariableElement element) {
        messager.printError(e.toString() + " is not supported", element);
        return super.defaultAction(e, element);
    }
}
