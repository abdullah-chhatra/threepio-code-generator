package com.abdullah.threepio.codegenerator;

import java.util.List;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

public class TEUtils {

    private final Types types;
    private final Elements elements;

    public TEUtils(ProcessingEnvironment environment) {
        this.types = environment.getTypeUtils();
        this.elements = environment.getElementUtils();
    }

    public Types getTypes() {
        return types;
    }

    public Elements getElements() {
        return elements;
    }

    public String getPackageName(Element element) {
        return elements.getPackageOf(element).toString();
    }

    public TypeElement getTypeElement(String className) {
        return elements.getTypeElement(className);
    }

    public TypeMirror getTypeMirror(String className) {
        final TypeElement element = elements.getTypeElement(className);
        if(element != null) {
            return element.asType();
        }
        return null;
    }

    public boolean isSubtype(TypeMirror t1, TypeMirror t2) {
        return types.isSubtype(t1, t2);
    }

    public boolean isSubtype(TypeMirror t1, String t2) {
        return isSubtype(t1, getTypeMirror(t2));
    }

    public boolean isSubtype(String t1, TypeMirror t2) {
        return isSubtype(getTypeMirror(t1), t2);
    }

    public boolean isSubtype(String t1, String t2) {
        return isSubtype(getTypeMirror(t1), getTypeMirror(t2));
    }

    public boolean isSameType(TypeMirror t1, TypeMirror t2) {
        return types.isSameType(t1, t2);
    }

    public boolean isAssignable(TypeMirror t1, TypeMirror t2) {
        return types.isAssignable(t1, t2);
    }
    public boolean hasAnnotation(Element element, TypeElement annotation) {
        return hasAnnotation(element, annotation.asType());
    }

    public boolean hasAnnotation(Element element, TypeMirror annotationMirror) {
        final List<? extends AnnotationMirror> mirrors = element.getAnnotationMirrors();
        for(AnnotationMirror mirror : mirrors) {
            if(types.isSameType(mirror.getAnnotationType(), annotationMirror)) {
                return true;
            }
        }
        return false;
    }

    public AnnotationMirror getAnnotationMirror(Element element, TypeElement annotation) {
        return getAnnotationMirror(element, annotation.asType());
    }

    public AnnotationMirror getAnnotationMirror(Element element, TypeMirror annotationMirror) {
        final List<? extends AnnotationMirror> mirrors = element.getAnnotationMirrors();
        for(AnnotationMirror mirror : mirrors) {
            if(types.isSameType(mirror.getAnnotationType(), annotationMirror)) {
                return mirror;
            }
        }
        return null;
    }

    public AnnotationValue getAnnotationValue(Element element, TypeElement typeElement, String name) {
        return getAnnotationValue(element, typeElement.asType(), name);
    }

    public AnnotationValue getAnnotationValue(Element element, TypeMirror typeMirror, String name) {
        AnnotationMirror mirror = getAnnotationMirror(element, typeMirror);
        if (mirror != null) {
            Map<? extends ExecutableElement, ? extends AnnotationValue> map = mirror.getElementValues();
            for (ExecutableElement key : map.keySet()) {
                if (key.getSimpleName().toString().equals(name)) {
                    return map.get(key);
                }
            }
        }

        return null;
    }

    public boolean isOfBoxedPrimitiveType(Element element) {
        return isBoxedPrimitiveType(element.asType());
    }

    public boolean isBoxedPrimitiveType(TypeMirror type) {
        try {
            types.unboxedType(type);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public PrimitiveType getPrimitiveType(DeclaredType boxedType) {
        try {
            return types.unboxedType(boxedType);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}