package com.abdullah.threepio.codegenerator.parcelmaker;

import com.abdullah.threepio.codegenerator.TGenerator;
import com.abdullah.threepio.codegenerator.TModelFactory;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

public class ParcelableGenerator extends TGenerator {

    private static final String ADD_SUFFIX = "addSuffix";
    private static final String REMOVE_SUFFIX = "removeSuffix";

    public ParcelableGenerator(ProcessingEnvironment environment, TModelFactory modelFactory) {
        super(environment, modelFactory);
    }

    @Override
    public String getSupportedAnnotation() {
        return Const.AUTO_PARCEL;
    }

    @Override
    public Set<String> getSupportedOptions() {
        HashSet<String> options = new HashSet<>();
        options.add(ADD_SUFFIX);
        options.add(REMOVE_SUFFIX);
        return options;
    }

    @Override
    public void process(Element element) {

        if(isValidElement(element)) {
            GeneratedParcelable gp = new GeneratedParcelable(element, modelFactory, messager, teUtils);
            ParcelableFieldProcessor fieldProcessor = new ParcelableFieldProcessor(teUtils, messager, gp);

            if(gp.initialize(getRemoveSuffix(), getAddSuffix())) {
                for (Element field : element.getEnclosedElements()) {
                    if (isValidField(field)) {
                        field.asType().accept(fieldProcessor, (VariableElement) field);
                    }
                }
                gp.finalizeGenerate();
            }
        }
    }

    // <editor-fold desc="Private Methods>
    private boolean isValidElement(Element element) {
        ElementKind elementKind = element.getKind();
        if(!elementKind.equals(ElementKind.CLASS)) {
            messager.printError("AutoParcel can only be applied to class type.", element);
            return false;
        }
        return true;
    }

    private boolean isValidField(Element fieldElement) {
        if(fieldElement.getKind() != ElementKind.FIELD) {
            return false;
        }

        TypeElement parcelIgnore = teUtils.getTypeElement(Const.PARCEL_IGNORE);
        if(teUtils.hasAnnotation(fieldElement, parcelIgnore)) {
            return false;
        }

        Set<Modifier> modifiers = fieldElement.getModifiers();
        if(modifiers.contains(Modifier.FINAL) ||
                modifiers.contains(Modifier.PRIVATE) ||
                modifiers.contains(Modifier.STATIC) ||
                modifiers.contains(Modifier.TRANSIENT)) {
            return false;
        }
        return true;
    }

    private String getAddSuffix() {
        return environment.getOptions().get(ADD_SUFFIX);
    }

    private String getRemoveSuffix() {
        return environment.getOptions().get(REMOVE_SUFFIX);
    }
    // </editor-fold>
}
