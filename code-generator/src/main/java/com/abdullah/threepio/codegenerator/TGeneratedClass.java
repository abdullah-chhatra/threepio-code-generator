package com.abdullah.threepio.codegenerator;

import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldRef;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;

/**
 * Created by abdullah on 7/11/15.
 */
public class TGeneratedClass {

    protected final TModelFactory factory;
    protected final JCodeModel codeModel;
    protected final TMessager messager;
    protected final TEUtils teUtils;

    protected JDefinedClass generated;

    protected TGeneratedClass(TModelFactory factory, TMessager messager, TEUtils teUtils) {
        this.factory = factory;
        this.codeModel = factory.getCodeModel();
        this.messager = messager;
        this.teUtils = teUtils;
    }

    // <editor-fold desc="Class methods">
    protected void createClass(String name) {
        generated = factory.create(name);
    }

    protected void createClassExtends(String name, String... baseClasses) {
        generated = factory.create(name);
        for(String baseClass : baseClasses) {
            addExtends(baseClass);
        }
    }

    protected void createClassImplements(String name, String... baseInterfaces) {
        generated = factory.create(name);
        for(String baseInterface : baseInterfaces) {
            addImplements(baseInterface);
        }
    }

    protected void addExtends(String baseClass) {
        generated._extends(factory.ref(baseClass));

    }

    protected void addImplements(String baseInterface) {
        generated._implements(factory.ref(baseInterface));
    }
    // </editor-fold>

    // <editor-fold desc="Constructor Methods">

    /**
     * Creates forwarding constructors for all the public and protected constructors in base class.
     *
     * @param   baseClass
     *          Base class of this generated class.
     */
    protected void createBaseConstructors(Element baseClass) {
        for(Element element : baseClass.getEnclosedElements()) {
            if(element.getKind() == ElementKind.CONSTRUCTOR) {
                createConstructor((ExecutableElement) element);
            }
        }
    }

    /**
     * Creates a constructor with specified mod and parameters. If such constructor already
     * exists then it will be returned.
     *
     * @param   mod
     *          Public, private or protected mod.
     *
     * @param   params
     *          Paired list of parameters viz. Type, Name, Type, Name...
     *
     * @return  A constructor with specified mod an parameters.
     */
    protected JMethod createConstructor(int mod, String... params) {
        List<String> paramTypes = new ArrayList<>();
        for(int i = 0 ; i < params.length ; i += 2) {
            paramTypes.add(params[i]);
        }

        JMethod constructor = getConstructor(paramTypes);
        if(constructor == null) {
            constructor = generated.constructor(mod);
            for(int i = 0 ; i < params.length ; i += 2) {
                JType paramType = factory.ref(params[i]);
                String paramName = params[i + 1];
                constructor.param(paramType, paramName);
            }
        }

        return constructor;
    }

    /**
     * Returns a constructor with specified parameters if it exists.
     *
     * @param   paramTypes
     *          List of parameters types.
     *
     * @return  Returns a constructor with specified parameters if it exists, null otherwise.
     */
    protected JMethod getConstructor(List<String> paramTypes) {
        JType[] types = new JType[paramTypes.size()];
        int i = 0;
        for(String type : paramTypes) {
            types[i++] = factory.ref(type);
        }
        return generated.getConstructor(types);
    }

    /**
     * Returns a constructor with specified parameters if it exists.
     *
     * @param   paramTypes
     *          List of parameters types.
     *
     * @return  Returns a constructor with specified parameters if it exists, null otherwise.
     */
    protected JMethod getConstructor(String... paramTypes) {
        return getConstructor(Arrays.asList(paramTypes));
    }

    /**
     * Returns a generated constructor corresponding to the executable element passed to it.
     *
     * @param   constructor
     *          Corresponding constructor element in class.
     *
     * @return  A generated constructor if it already exists, null otherwise.
     */
    protected JMethod getConstructor(ExecutableElement constructor) {
        List<? extends VariableElement> params = constructor.getParameters();
        JType[] paramTypes = new JType[params.size()];
        int i = 0;
        for(VariableElement param : params) {
            paramTypes[i++] = factory.ref(param.asType().toString());
        }

        return generated.getConstructor(paramTypes);
    }

    /**
     * Creates or returns a generated constructor corresponding to the executable element passed
     * to it. If the constructor is private then it will not create a corresponding constructor
     * and will return null.
     *
     * @param   constructor
     *          Corresponding constructor element in class.
     *
     * @return  A generated constructor.
     */
    protected JMethod createConstructor(ExecutableElement constructor) {
        JMethod gConst = getConstructor(constructor);
        if(gConst == null) {
            int mod = getConstructorMode(constructor);
            if(mod != JMod.PRIVATE) {
                gConst = generated.constructor(getConstructorMode(constructor));
                JInvocation superInvocation = gConst.body().invoke("super");
                for(VariableElement param : constructor.getParameters()) {
                    JType paramType = factory.ref(param.asType().toString());
                    String paramName = param.getSimpleName().toString();
                    gConst.param(paramType, paramName);
                    superInvocation.arg(JExpr.ref(paramName));
                }
            }
        }
        return gConst;
    }

    /**
     * Checks if corresponding constructor exists for the element passed.
     *
     * @param   constructor
     *          Corresponding constructor to check.
     *
     * @return  True is exists, false otherwise.
     */
    protected boolean hasConstructor(ExecutableElement constructor) {
        return getConstructor(constructor) != null;
    }

    /**
     * Checks if a constructor with specified parameter types exists.
     *
     * @param   paramTypes
     *          Constructor parameter types.
     *
     * @return  True if exists, false otherwise.
     */
    protected boolean hasConstructor(List<String> paramTypes) {
        return getConstructor(paramTypes) != null;
    }

    /**
     * Checks if a constructor with specified parameter types exists.
     *
     * @param   paramTypes
     *          Constructor parameter types.
     *
     * @return  True if exists, false otherwise.
     */
    protected boolean hasConstructor(String... paramTypes) {
        return getConstructor(paramTypes) != null;
    }

    protected int getConstructorMode(ExecutableElement constructor) {
        Set<Modifier> modifiers = constructor.getModifiers();
        if(modifiers.contains(Modifier.PUBLIC)) {
            return JMod.PUBLIC;
        }
        else if(modifiers.contains(Modifier.PROTECTED)) {
            return JMod.PROTECTED;
        } else if(modifiers.contains(Modifier.PRIVATE)) {
            return JMod.PRIVATE;
        }
        return JMod.NONE;
    }
    // </editor-fold>

    protected JFieldRef getField(VariableElement element) {
        return JExpr.ref(element.getSimpleName().toString());
    }
}
