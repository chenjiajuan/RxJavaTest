package com.chenjiajuan.commom.router;

import android.text.TextUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * create by chenjiajuan on 2018/10/4
 */
 class MethodHolder {
    private final String methodName;
    private final Class[] paramTypes;
    private final Class handler;

    private boolean isKotlinClass = false;
    private Object kotlinInstanceHolder = null;

    MethodHolder(String methodName, Class[] paramTypes, Class handler) {
        this.methodName = methodName;
        this.paramTypes = paramTypes;
        this.handler = handler;

        isKotlinClass = isKotlinClass(handler);
        if (isKotlinClass) {
            kotlinInstanceHolder = getKotlinClassInstance(handler);
            if (kotlinInstanceHolder == null) {
                throw new IllegalArgumentException("@Call with kotlin class only work on 'Companion object' and 'object class'");
            }
        }
    }


    private boolean isKotlinClass(Class clazz) {
        Annotation[] annotations = clazz.getAnnotations();

        for (Annotation annotation : annotations) {
            if (TextUtils.equals(annotation.annotationType().getName(), "kotlin.Metadata")) {
                return true;
            }
        }

        return false;
    }

    /**
     * @param clazz either 'Companion object' or 'object class'
     * @return instance or null
     */
    private Object getKotlinClassInstance(Class clazz) {
        Object field = null;

        //check kotlin companion object
        String clazzName = clazz.getName();
        String kotlinCompanion = "$Companion";
        if (clazzName.endsWith(kotlinCompanion)) {
            try {
                Class iclazz = clazz.getClassLoader().loadClass(clazzName.substring(0, clazzName.length() - kotlinCompanion.length()));
                field = getStaticField(iclazz, "Companion");
            } catch (ClassNotFoundException ignored) {
            }
            if (field != null)
                return field;
        }

        //check object class
        field = getStaticField(clazz, "INSTANCE");

        return field;
    }

    private Object getStaticField(Class clazz, String field) {
        try {
            Field companion = clazz.getDeclaredField(field);
            if (companion != null && Modifier.isStatic(companion.getModifiers())) {
                companion.setAccessible(true);
                return companion.get(clazz);
            }
        } catch (NoSuchFieldException ignored) {
        } catch (IllegalAccessException ignored) {
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    <T> T call(Object... objects) {
        try {
            Method method = handler.getDeclaredMethod(methodName, paramTypes);
            method.setAccessible(true);

            if (isKotlinClass) {
                return (T) method.invoke(kotlinInstanceHolder, objects);
            } else {
                return (T) method.invoke(null, objects);
            }
        } catch (NoSuchMethodException e) {
        } catch (SecurityException e) {
        } catch (IllegalAccessException e) {
        } catch (IllegalArgumentException e) {
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getCause());
        }
        throw new MappingNotFoundException(String.format("public static method?: %s", this.toString()));
    }

    String getMethodName() {
        return methodName;
    }

    Class[] getParamTypes() {
        return paramTypes;
    }

    Class getHandler() {
        return handler;
    }

    @SuppressWarnings("unchecked")
    boolean isStatic() {
        try {
            Method method = handler.getDeclaredMethod(methodName, paramTypes);

            if (isKotlinClass) {
                return kotlinInstanceHolder != null;
            } else {
                return Modifier.isStatic(method.getModifiers());
            }
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String toString() {
        String format = "%s.%s(";
        StringBuffer stringBuffer = null;
        for (Class clazz : paramTypes) {
            if (stringBuffer == null) {
                stringBuffer = new StringBuffer();
                stringBuffer.append(clazz.getSimpleName());
            } else {
                stringBuffer.append(", ").append(clazz.getSimpleName());
            }
        }
        if (stringBuffer == null) {
            format += ")";
        } else {
            format += stringBuffer.toString() + ")";
        }
        return String.format(format, handler.getName(), methodName);
    }
}
