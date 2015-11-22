package com.abdullah.threepio.codegenerator;

import com.sun.codemodel.JCodeModel;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;

/**
 * Created by abdullah on 31/10/15.
 */
public abstract class TGenerator {

    protected final ProcessingEnvironment environment;
    protected final TModelFactory modelFactory;

    protected final TEUtils teUtils;
    protected final TMessager messager;
    protected final JCodeModel codeModel;

    public TGenerator(ProcessingEnvironment environment,
                      TModelFactory modelFactory) {
        this.environment = environment;
        this.modelFactory = modelFactory;
        this.teUtils = new TEUtils(environment);
        this.messager = new TMessager(environment);
        this.codeModel = modelFactory.getCodeModel();
    }

    public abstract String getSupportedAnnotation();

    public Set<String> getSupportedOptions() {
        return new HashSet<>();
    }

    public abstract void process(Element element);
}
