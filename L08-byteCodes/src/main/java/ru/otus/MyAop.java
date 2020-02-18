package ru.otus;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.stream.Collectors;

public class MyAop {

    static MyinterfacClass creatMyClass() {
        InvocationHandler handler = new DemoInvocationHandler(new Demo());

        return (MyinterfacClass) Proxy.newProxyInstance(MyAop.class.getClassLoader(),
                new Class<?>[]{MyinterfacClass.class}, handler);
    }

    private static boolean hasLogAnnotation(Method method) {
        return method.getDeclaredAnnotation(log.class) != null;
    }

    static class DemoInvocationHandler implements InvocationHandler {

        private final MyinterfacClass myClass;
        private Set<Method> setMethods;

        DemoInvocationHandler(MyinterfacClass myClass) {
            this.myClass = myClass;

            setMethods = Arrays.stream(this.myClass.getClass().getDeclaredMethods())
                    .filter(MyAop::hasLogAnnotation)
                    .flatMap(method -> {
                        return Arrays.stream(this.myClass.getClass().getInterfaces())
                                .flatMap(intf -> Arrays.stream(intf.getMethods()))
                                .filter(method1 -> method.getName().equals(method1.getName())
                                        && method.getReturnType().equals(method1.getReturnType())
                                        && equalParamTypes(method.getParameterTypes(), method1.getParameterTypes()));
                    })
                    .collect(Collectors.toSet());
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            if (setMethods.contains(method))
                System.out.println("executed method: " + method.getName() + ", param:" + method.getParameterCount());

            return method.invoke(myClass, args);
        }

        private boolean equalParamTypes(Class<?>[] params1, Class<?>[] params2) {
            if (params1.length == params2.length) {
                for (int i = 0; i < params1.length; i++) {
                    if (params1[i] != params2[i])
                        return false;
                }
                return true;
            }
            return false;
        }
    }
}