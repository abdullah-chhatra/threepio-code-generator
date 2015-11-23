package com.abdullah.threepio.codegenerator;

import com.abdullah.threepio.codegenerator.autojson.JsonParsableGenerator;
import com.abdullah.threepio.codegenerator.parcelmaker.ParcelableGenerator;
import com.sun.codemodel.JCodeModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;


public class TProcessor extends AbstractProcessor {

    ProcessingEnvironment environment;
    TModelFactory modelFactory;

    List<TGenerator> generators = new ArrayList<TGenerator>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.environment = processingEnv;
        this.modelFactory = new TModelFactory();

        generators.add(new ParcelableGenerator(processingEnv, modelFactory));
        generators.add(new JsonParsableGenerator(processingEnv, modelFactory));
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_7;
    }

    @Override
    public Set<String> getSupportedOptions() {
        HashSet<String> options = new HashSet<>();
        for(TGenerator generator : generators) {
            options.addAll(generator.getSupportedOptions());
        }
        return options;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        HashSet<String> annotations = new HashSet<>();
        for(TGenerator generator : generators) {
            annotations.add(generator.getSupportedAnnotation());
        }
        return annotations;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        TEUtils utils = new TEUtils(processingEnv);
        TMessager messager = new TMessager(processingEnv);

        try {
            for(TGenerator generator : generators) {
                TypeElement annotation = utils.getTypeElement(generator.getSupportedAnnotation());
                if(annotation != null) {
                    for(Element element : roundEnv.getElementsAnnotatedWith(annotation)) {
                        generator.process(element);
                    }
                }
            }
            writeGenerateClass(modelFactory.getCodeModel());
            modelFactory.reset();
        }catch (Exception e) {
            messager.printError(e.getMessage());
            e.printStackTrace();
        }

        return true;
    }

    private void writeGenerateClass(JCodeModel codeModel) {
        try {
            codeModel.build(new TCodeWriter(environment.getFiler()));
        } catch (IOException e) {
            new TMessager(processingEnv)
                    .printError("Error occurred while writing code files. Reason - " + e.getMessage());
        }
    }
}
