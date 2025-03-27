package com.yidigun.utils;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;

@SuppressWarnings("all")
@SupportedAnnotationTypes("com.yidigun.utils.CheckHashCodeCalculated")
public class CheckHashCodeCalculatedProcessor extends AbstractProcessor {

    private ProcessingEnvironment env;

    @Override
    public synchronized void init(ProcessingEnvironment pe) {
        System.out.println("CheckHashCodeCalculatedProcessor.init: " + pe);
        this.env = pe;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (!roundEnv.processingOver()) {
            for (TypeElement te: annotations) {
                final Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(te);
                for (Element element: elements) {
                    System.out.println("CheckHashCodeCalculatedProcessor.process: " + element);
                    env.getMessager().printMessage(Diagnostic.Kind.WARNING,
                            String.format("%s : thou shalt not hack %s", roundEnv.getRootElements(), element),
                            element);
                }
            }
        }
        return true;
    }
}
