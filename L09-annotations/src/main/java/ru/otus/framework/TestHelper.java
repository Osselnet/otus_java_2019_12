package ru.otus.framework;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class TestHelper {

    public static void invokeMethods(Object instance, List<Method> methods) {
        methods.forEach(method -> {
            try {
                method.invoke(instance);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new Error(e);
            }
        });
    }

    public static String getTestName(Method test) {
        String testName = test.getAnnotation(Test.class).name();
        return testName.isEmpty() ? test.getName() : testName;
    }

    public static List<Method> getAnnotatedMethods(Class<?> clazz, Class<? extends Annotation> annotation) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.getDeclaredAnnotation(annotation) != null)
                .collect(Collectors.toList());
    }
}