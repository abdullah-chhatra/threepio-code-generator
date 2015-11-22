package com.abdullah.threepio.codegenerator;

import com.sun.codemodel.CodeWriter;
import com.sun.codemodel.JPackage;

import java.io.IOException;
import java.io.OutputStream;

import javax.annotation.processing.Filer;

/**
 * Created by abdulmunaf on 6/8/15.
 */
public class TCodeWriter extends CodeWriter {

    private Filer filer;
    private OutputStream codeStream;

    public TCodeWriter(Filer filer) {
        this.filer = filer;
    }

    @Override
    public OutputStream openBinary(JPackage pkg, String fileName) throws IOException {
        //Remove .java from the file name to get class name.
        String fullClassName = pkg.name() + "." + fileName.substring(0, fileName.length() - 5);
        codeStream = filer.createSourceFile(fullClassName).openOutputStream();
        return codeStream;
    }

    @Override
    public void close() throws IOException {
        if(codeStream != null) {
            codeStream.close();
            codeStream = null;
        }
    }
}
