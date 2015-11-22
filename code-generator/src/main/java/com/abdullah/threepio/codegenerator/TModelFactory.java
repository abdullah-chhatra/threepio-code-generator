package com.abdullah.threepio.codegenerator;

import com.abdullah.threepio.codegenerator.autojson.Const;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by abdulmunaf on 8/8/15.
 */
public class TModelFactory {

    private JCodeModel codeModel;
    private Map<String, JClass> classes;

    public TModelFactory() {
        reset();
    }

    public JCodeModel getCodeModel() {
        return codeModel;
    }

    public void reset() {
        this.codeModel = new JCodeModel();
        this.classes = new HashMap<String, JClass>();
    }

    public JClass ref(String fullyQualifiedName) {
        if(classes.containsKey(fullyQualifiedName)) {
            return classes.get(fullyQualifiedName);
        }
        JClass newClass = codeModel.ref(fullyQualifiedName);
        classes.put(fullyQualifiedName, newClass);
        return newClass;
    }

    public JClass refExt(String fullyQualifiedName, TMessager messager) {
        try {
            if (classes.containsKey(fullyQualifiedName)) {
                messager.printNote("We found it out");
                return classes.get(fullyQualifiedName);
            }

            messager.printNote("We have not found " + fullyQualifiedName);
            JClass newClass = codeModel.ref(fullyQualifiedName);
            if(newClass == null) {
                messager.printNote("Null found");
            } else {
                messager.printNote("Not null found");
            }
            //classes.put(fullyQualifiedName, newClass);
            //messager.printNote(newClass.fullName());
            return newClass;
        } catch (Exception e) {
            messager.printNote(e.getMessage());
            return ref(Const.JSON_PARSABLE);
        }
    }

    public JDefinedClass create(String fullyQualifiedName)  {
        try {
            return codeModel._class(fullyQualifiedName);
        } catch (JClassAlreadyExistsException e) {
            return codeModel._getClass(fullyQualifiedName);
        }
    }
}
