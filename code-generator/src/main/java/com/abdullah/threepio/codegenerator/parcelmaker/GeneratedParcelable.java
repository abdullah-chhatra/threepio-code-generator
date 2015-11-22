package com.abdullah.threepio.codegenerator.parcelmaker;

import com.abdullah.threepio.codegenerator.TConsts;
import com.abdullah.threepio.codegenerator.TEUtils;
import com.abdullah.threepio.codegenerator.TGeneratedClass;
import com.abdullah.threepio.codegenerator.TMessager;
import com.abdullah.threepio.codegenerator.TModelFactory;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JFieldRef;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JOp;
import com.sun.codemodel.JType;
import com.sun.codemodel.JVar;

import javax.lang.model.element.Element;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

public class GeneratedParcelable extends TGeneratedClass {

    private final Element baseClass;

    private JBlock writeBlock;
    private JBlock readBlock;
    private JVar inVar;
    private JVar destVar;

    public GeneratedParcelable(Element baseClass, TModelFactory factory,
                               TMessager messager, TEUtils teUtils) {
        super(factory, messager, teUtils);
        this.baseClass = baseClass;
    }

    // <editor-fold desc="Read Write Methods">
    public boolean initialize(String removeSuffix, String addSuffix) {
        String className = getClassName(removeSuffix, addSuffix);
        if(className != null) {
            createParcelableClass(className);
            createConstructors();
            createWriteToParcel();
            createDescribeContent();
            createCreatorMember();
            return true;
        }

        return false;
    }

    public Void addStatements(VariableElement fieldElement, String readMethod, String writeMethod) {
        JFieldRef field = JExpr.ref(fieldElement.getSimpleName().toString());

        readBlock.assign(field, JExpr.invoke(inVar, readMethod));
        writeBlock.invoke(destVar, writeMethod).arg(field);
        return null;
    }

    public Void addBooleanStatements(VariableElement element) {
        JFieldRef field = JExpr.ref(element.getSimpleName().toString());
        readBlock.assign(field, JExpr.invoke(inVar, "readInt").eq(JExpr.lit(1)));
        writeBlock.invoke(destVar, "writeInt").arg(JOp.cond(field, JExpr.lit(1), JExpr.lit(0))) ;
        return null;
    }

    public Void addLoadableStatements(VariableElement element, String readMethod, String writeMethod) {
        JFieldRef field = JExpr.ref(element.getSimpleName().toString());
        JFieldRef flags = JExpr.ref("flags");

        writeBlock.invoke(destVar, writeMethod)
                  .arg(field)
                  .arg(flags);
        readBlock.assign(field,
                JExpr.invoke(inVar, readMethod)
                     .arg(getClassLoaderExpression(element)));
        return null;
    }

    public Void addStringListStatements(VariableElement element) {
        JFieldRef field = JExpr.ref(element.getSimpleName().toString());

        writeBlock.invoke(destVar, "writeStringList").arg(field);
        readBlock.assign(field, JExpr.invoke(inVar, "createStringArrayList"));
        return null;
    }

    public Void addTypedListStatements(VariableElement element) {
        JFieldRef field = JExpr.ref(element.getSimpleName().toString());
        DeclaredType type = (DeclaredType) element.asType();

        writeBlock.invoke(destVar, "writeTypedList").arg(field);

        JInvocation createInvocation = JExpr.invoke(inVar, "createTypedArrayList");
        createInvocation.arg(getCreator(type.getTypeArguments().get(0)));
        readBlock.assign(field, createInvocation);

        return null;
    }

    public void finalizeGenerate() {
        DeclaredType baseType = (DeclaredType) baseClass.asType();
        TypeMirror parcelListener = teUtils.getTypeMirror(Const.PARCEL_LISTENER);

        if(teUtils.isSubtype(baseType, parcelListener)) {
            writeBlock.invoke("writeToParcel").arg(destVar);
            readBlock.invoke("readFromParcel").arg(inVar);
        }
    }

    // </editor-fold>

    // <editor-fold desc="Private Methods">
    private void createParcelableClass(String className) {
        String baseFullName = baseClass.asType().toString();
        createClassExtends(className, baseFullName);
        addImplements(Const.PARCELABLE);
    }

    private void createConstructors() {
        createBaseConstructors(baseClass);

        JMethod constructor = createConstructor(JMod.PROTECTED, Const.PARCEL, "in");
        inVar = constructor.params().get(0);
        readBlock = constructor.body();
    }

    private void createDescribeContent() {
        JType integer = codeModel._ref(int.class);

        JMethod describeContent = generated.method(JMod.PUBLIC, integer, "describeContents");
        describeContent.annotate(factory.ref(TConsts.OVERRIDE));
        describeContent.body()._return(JExpr.lit(0));
    }

    private void createWriteToParcel() {
        JType Void = codeModel._ref(void.class);
        JType Integer = codeModel._ref(int.class);
        JClass Parcel = factory.ref(Const.PARCEL);

        JMethod wtp = generated.method(JMod.PUBLIC, Void, "writeToParcel");
        wtp.annotate(factory.ref(TConsts.OVERRIDE));
        destVar = wtp.param(Parcel, "dest");
        wtp.param(Integer, "flags");

        writeBlock = wtp.body();
    }

    private void createCreatorMember() {
        JClass Parcel = factory.ref(Const.PARCEL);
        JClass Override = factory.ref(TConsts.OVERRIDE);

        JClass creatorInterface = factory.ref(Const.CREATOR);
        creatorInterface = creatorInterface.narrow(generated);
        JDefinedClass anonymousClass = codeModel.anonymousClass(creatorInterface);

        JMethod createInstanceMethod = anonymousClass.method(JMod.PUBLIC, generated, "createFromParcel");
        JVar json = createInstanceMethod.param(Parcel, "in");
        createInstanceMethod.annotate(Override);
        createInstanceMethod.body()._return(JExpr._new(generated).arg(json));

        JMethod createArrayMethod = anonymousClass.method(JMod.PUBLIC, generated.array(), "newArray");
        JVar size = createArrayMethod.param(codeModel.INT, "size");
        createArrayMethod.annotate(Override);
        createArrayMethod.body()._return(JExpr.newArray(generated, size));

        JFieldVar creatorField =
                generated.field(JMod.PUBLIC | JMod.STATIC | JMod.FINAL, anonymousClass, "CREATOR", JExpr._new(anonymousClass));

    }

    private JExpression getClassLoaderExpression(VariableElement element) {
        TypeMirror type = element.asType();
        JClass dc = null;
        if(type instanceof ArrayType) {
            ArrayType arrayType = (ArrayType) type;
            return getCreator(arrayType.getComponentType());
        } else {
            return JExpr.dotclass(factory.ref(type.toString())).invoke("getClassLoader");
        }
    }

    private JFieldRef getCreator(TypeMirror type) {
        return factory.ref(type.toString()).staticRef("CREATOR");
    }

    private String getClassName(String removeSuffix, String addSuffix) {
        if(removeSuffix != null && addSuffix != null) {
            messager.printError(
                    "Only one of 'removeParcelableSuffix' or 'addParcelableSuffix' "
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
