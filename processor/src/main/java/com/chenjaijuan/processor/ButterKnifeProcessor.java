package com.chenjaijuan.processor;

import com.chenjaijuan.routerannotation.BindView;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

/**
 * 自定义注释
 * https://www.jianshu.com/p/ac6a69adecff
 */

@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedAnnotationTypes("com.chenjaijuan.routerannotation.BindView")
public class ButterKnifeProcessor  extends AbstractProcessor {

    private Messager messager;


    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        messager=processingEnvironment.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        for (Element element:roundEnvironment.getElementsAnnotatedWith(BindView.class)){
            if (element.getKind()== ElementKind.FIELD){
                messager.printMessage(Diagnostic.Kind.NOTE,"printMessage : " + element.toString());
            }
        }
        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return super.getSupportedAnnotationTypes();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
