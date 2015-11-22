package com.abdullah.threepio.codegenerator;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

/**
 * Created by abdullah on 31/10/15.
 */
public class TMessager {

    final Messager messager;

    public TMessager(ProcessingEnvironment environment) {
        this.messager = environment.getMessager();
    }

    public void printNote(String message) {
        messager.printMessage(Diagnostic.Kind.NOTE, message);
    }

    public void printError(String message) {
        messager.printMessage(Diagnostic.Kind.ERROR, message);
    }

    public void printError(String message, Element element) {
        messager.printMessage(Diagnostic.Kind.ERROR, message, element);
    }

    public void printError(String message, Element element, AnnotationMirror mirror) {
        messager.printMessage(Diagnostic.Kind.ERROR, message, element, mirror);
    }
}
