package com.abdullah.threepio.codegenerator.autojson;

import com.abdullah.threepio.codegenerator.TConsts;
import com.abdullah.threepio.codegenerator.TEUtils;
import com.abdullah.threepio.codegenerator.TGeneratedClass;
import com.abdullah.threepio.codegenerator.TMessager;
import com.abdullah.threepio.codegenerator.TModelFactory;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JCatchBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JFieldRef;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JTryBlock;
import com.sun.codemodel.JVar;

import javax.lang.model.element.Element;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

public class GeneratedJsonParsable extends TGeneratedClass {

    private static final String CREATOR = "JSON_CREATOR";

    private final Element baseClass;

    private JVar in;
    private JVar reader;
    private JBlock inBlock;

    private JVar out;
    private JVar writer;
    private JBlock outBlock;

    private JMethod constructor;
    private JMethod toJson;

    JClass jsonObject = factory.ref(Const.JSON_OBJECT);
    JClass jsonException = factory.ref(Const.JSON_EXCEPTION);

    private final AnnotationUtil aUtil;
    private final Options options;

    protected GeneratedJsonParsable(Element baseClass, TModelFactory factory,
                                    TMessager messager, TEUtils teUtils,
                                    Options options) {
        super(factory, messager, teUtils);
        this.baseClass = baseClass;
        this.aUtil = new AnnotationUtil(teUtils, options);
        this.options = options;

        messager.printNote(options.getKeyCase().toString());
        messager.printNote(options.getVarCase().toString());
    }

    public Void addPrimitive(VariableElement element, String getMethod, String optMethod) {
        JFieldRef field = getField(element);
        JsonFieldMetadata metaData = aUtil.getFieldMetadata(element);

        outBlock.invoke(out, "put").arg(metaData.key).arg(field);

        JInvocation inInvocation = JExpr.invoke(in, metaData.optional ? optMethod : getMethod);
        inInvocation.arg(metaData.key);
        if(metaData.defaultValue != null) {
            inInvocation.arg(metaData.defaultValue);
        }
        inBlock.assign(field, inInvocation);
        return null;
    }

    public Void addJsonParsable(VariableElement element) {
        JFieldRef field = getField(element);
        JsonFieldMetadata metaData = aUtil.getFieldMetadata(element);

        outBlock.invoke(writer, "write")
                .arg(metaData.key)
                .arg(field)
                .arg(JExpr.lit(metaData.optional));

        JInvocation inInvocation = JExpr.invoke(reader, "readJsonParsable")
                                        .arg(metaData.key)
                                        .arg(JExpr.lit(metaData.optional))
                                        .arg(getCreator(element.asType()));
        inBlock.assign(field, inInvocation);
        return null;
    }

    public Void addArray(VariableElement element, String readMethod) {
        JFieldRef field = getField(element);
        JsonFieldMetadata metaData = aUtil.getFieldMetadata(element);

        outBlock.invoke(writer, "write")
                .arg(metaData.key)
                .arg(field)
                .arg(JExpr.lit(metaData.optional));

        JInvocation inInvocation = JExpr.invoke(reader, readMethod)
                                        .arg(metaData.key)
                                        .arg(JExpr.lit(metaData.optional));
        inBlock.assign(field, inInvocation);
        return null;
    }

    public Void addJsonParsableArray(VariableElement element) {
        JFieldRef field = getField(element);
        JsonFieldMetadata metaData = aUtil.getFieldMetadata(element);

        outBlock.invoke(writer, "write")
                .arg(metaData.key)
                .arg(field)
                .arg(JExpr.lit(metaData.optional));

        ArrayType fieldType = (ArrayType) element.asType();
        TypeMirror componentType = fieldType.getComponentType();
        JInvocation inInvocation = JExpr.invoke(reader, "readJsonParsableArray")
                                        .arg(metaData.key)
                                        .arg(JExpr.lit(metaData.optional))
                                        .arg(getCreator(componentType));
        inBlock.assign(field, inInvocation);
        return null;
    }

    public Void addStringList(VariableElement element) {
        JFieldRef field = getField(element);
        JsonFieldMetadata metaData = aUtil.getFieldMetadata(element);

        outBlock.invoke(writer, "writeStringList")
                .arg(metaData.key)
                .arg(field)
                .arg(JExpr.lit(metaData.optional));

        JInvocation inInvocation = JExpr.invoke(reader, "readStringArrayList")
                                        .arg(metaData.key)
                                        .arg(JExpr.lit(metaData.optional));
        inBlock.assign(field, inInvocation);
        return null;
    }

    public Void addJsonParsableList(VariableElement element) {
        JFieldRef field = getField(element);
        JsonFieldMetadata metaData = aUtil.getFieldMetadata(element);

        outBlock.invoke(writer, "writeJsonParsableList")
                .arg(metaData.key)
                .arg(field)
                .arg(JExpr.lit(metaData.optional));

        DeclaredType fieldType = (DeclaredType) element.asType();
        TypeMirror listType = fieldType.getTypeArguments().get(0);
        JInvocation inInvocation = JExpr.invoke(reader, "readJsonParsableArrayList")
                                        .arg(metaData.key)
                                        .arg(JExpr.lit(metaData.optional))
                                        .arg(getCreator(listType));
        inBlock.assign(field, inInvocation);
        return null;
    }

    // <editor-fold desc="Initialize Methods">
    public boolean initialize() {
        String className = getClassName(options.getRemoveSuffix(), options.getAddSuffix());
        if(className != null) {
            createJsonClass(className);
            createConstructors();
            createToJson();
            createCreatorMember();
            return true;
        }
        return false;
    }

    public void finalizeGenerate() {
        TypeMirror jsonParseListener = teUtils.getTypeMirror(Const.JSON_PARSE_LISTENER);
        if(teUtils.isSubtype(baseClass.asType(), jsonParseListener)) {
            outBlock.invoke("onWriteJson").arg(out);
            inBlock.invoke("onReadJson").arg(in);
        }

        outBlock._return(out);
    }

    private void createJsonClass(String className) {
        String baseFullName = baseClass.asType().toString();
        createClassExtends(className, baseFullName);
        addImplements(Const.JSON_PARSABLE);
    }

    private void createConstructors() {
        createBaseConstructors(baseClass);
        constructor = createConstructor(JMod.PUBLIC, Const.JSON_OBJECT, "in");
        constructor._throws(jsonException);
        in = constructor.params().get(0);
        inBlock = createTryBlock(constructor);

        JClass jsonReader = factory.ref(Const.JSON_READER);
        reader = inBlock.decl(jsonReader, "reader", JExpr._new(jsonReader).arg(in));

    }

    private void createToJson() {
        toJson = generated.method(JMod.PUBLIC, jsonObject, "toJson");
        toJson._throws(jsonException);
        toJson.annotate(Override.class);

        outBlock = createTryBlock(toJson);
        out = outBlock.decl(jsonObject, "out", JExpr._new(jsonObject));

        JClass jsonWriter = factory.ref(Const.JSON_WRITER);
        writer = outBlock.decl(jsonWriter, "writer", JExpr._new(jsonWriter).arg(out));
    }

    private JBlock createTryBlock(JMethod method) {
        JBlock methodBlock = method.body();
        JTryBlock tryBlock = methodBlock._try();

        JCatchBlock catchBlock = tryBlock._catch(jsonException);
        JVar e = catchBlock.param("e");
        JExpression excp = JExpr.lit(generated.name() + " -> ").plus(e.invoke("getMessage"));

        catchBlock.body()._throw(JExpr._new(jsonException).arg(excp));
        return tryBlock.body();
    }

    private void createCreatorMember() {
        JClass Override = factory.ref(TConsts.OVERRIDE);

        JClass creatorInterface = factory.ref(Const.CREATOR);
        creatorInterface = creatorInterface.narrow(generated);
        JDefinedClass anonymousClass = codeModel.anonymousClass(creatorInterface);

        JMethod createMethod = anonymousClass.method(JMod.PUBLIC, generated, "create");
        createMethod.annotate(Override);
        createMethod._throws(jsonException);
        JVar json = createMethod.param(jsonObject, "in");
        createMethod.body()._return(JExpr._new(generated).arg(json));

        JMethod createArrayMethod = anonymousClass.method(JMod.PUBLIC, generated.array(), "createArray");
        JVar size = createArrayMethod.param(codeModel.INT, "size");
        createArrayMethod.annotate(Override);
        createArrayMethod.body()._return(JExpr.newArray(generated, size));

        JFieldVar creatorField =
                generated.field(JMod.PUBLIC | JMod.STATIC | JMod.FINAL,
                        anonymousClass,
                        CREATOR,
                        JExpr._new(anonymousClass));
    }
    // </editor-fold>

    // <editor-fold />
    private JFieldRef getCreator(TypeMirror type) {
        return factory.ref(type.toString()).staticRef(CREATOR);
    }

    private String getClassName(String removeSuffix, String addSuffix) {
        if(removeSuffix != null && addSuffix != null) {
            messager.printError(
                    "Only one of 'removeJsonParsableSuffix' or 'addJsonParsableSuffix' "
                            + "should be specified as processor argument");
            return null;
        }

        String className = baseClass.asType().toString();
        if(addSuffix != null) {
            return className += addSuffix;
        }

        if(removeSuffix == null) {
            removeSuffix = "Base";
        }
        if(className.endsWith(removeSuffix)) {
            int lastIndex = className.length() - removeSuffix.length();
            return className.substring(0, lastIndex);
        } else {
            messager.printError("The class name should end with '" + removeSuffix + "'.", baseClass);
            return null;
        }
    }
    // </editor-fold>
}
