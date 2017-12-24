package com.github.container;

import com.github.collections.CollectionKit;
import com.github.reflect.ReflectKit;
import org.apache.log4j.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Created by winstone on 2017/12/24 0024.
 */
public class DefaultContainer implements Container {

    private static final Logger LOGGER = Logger.getLogger(DefaultContainer.class);

    /**
     * 保存所有bean对象
     */
    private static final Map<String, Object> BEAN_CONTAINER = CollectionKit.newConcurrentHashMap();

    /**
     * 保存所有注解的class
     */
    private static final Map<Class<? extends Annotation>, List<Object>> ANNOTATION_CONTAINER = CollectionKit.newConcurrentHashMap();

    private DefaultContainer() {
    }

    public static DefaultContainer single() {
        return DefaultContainerHoder.single;
    }

    private static class DefaultContainerHoder {
        private static final DefaultContainer single = new DefaultContainer();
    }

    public Map<String, Object> getBeanMap() {
        return BEAN_CONTAINER;
    }

    @Override
    public <T> T getBean(String name, Scope scope) {
        Object obj = BEAN_CONTAINER.get(name);
        if(null != scope && scope == Scope.PROTOTYPE){
            try {
                //return (T) CloneKit.deepClone(obj);
            } catch (Exception e) {
                LOGGER.error("克隆对象失败," + e.getMessage());
            }
        }
        return (T) obj;
    }

    @Override
    public <T> T getBean(Class<T> type, Scope scope) {
        Iterator<Object> it = BEAN_CONTAINER.values().iterator();
        while (it.hasNext()) {
            Object obj = it.next();
            if (type.isAssignableFrom(obj.getClass())) {
                if(null != scope && scope == Scope.PROTOTYPE){
                    try {
                     //   return (T) CloneKit.deepClone(obj);
                    } catch (Exception e) {
                        LOGGER.error("克隆对象失败," + e.getMessage());
                    }
                } else {
                    return (T) obj;
                }
            }
        }
        return null;
    }

    @Override
    public Set<String> getBeanNames() {
        return BEAN_CONTAINER.keySet();
    }

    @Override
    public Collection<Object> getBeans() {
        return BEAN_CONTAINER.values();
    }

    @Override
    public boolean hasBean(Class<?> clz) {
        if (null != this.getBean(clz, Scope.SINGLE)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean hasBean(String name) {
        if (null != this.getBean(name, Scope.SINGLE)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean removeBean(String name) {
        Object object = BEAN_CONTAINER.remove(name);
        return (null != object);
    }

    @Override
    public boolean removeBean(Class<?> clazz) {
        Object object = BEAN_CONTAINER.remove(clazz.getName());
        return (null != object);
    }

    /**
     * 注册一个bean对象到容器里
     *
     * @param clazz 要注册的class
     * @return		返回注册后的bean对象
     */
    @Override
    public Object registBean(Class<?> clazz) {

        String name = clazz.getCanonicalName();

        Object object = null;

        //非抽象类、接口
        if (!Modifier.isAbstract(clazz.getModifiers()) && !clazz.isInterface()) {

            object = ReflectKit.newInstance(clazz);

            put(name, object);
            //实现的接口对应存储
            if(clazz.getInterfaces().length > 0){
                put(clazz.getInterfaces()[0].getCanonicalName(), object);
            }

            //带有annotation
            if(null != clazz.getDeclaredAnnotations()){
                putAnnotationMap(clazz, object);
            }
        }
        return object;
    }

    /**
     * bean容器存储
     *
     * @param name			要进入IOC容器的bean名称
     * @param object		要进入IOC容器的bean对象
     */
    private void put(String name, Object object){
        if(null == BEAN_CONTAINER.get(name)){
            BEAN_CONTAINER.put(name, object);
        }
    }

    /**
     * 给annotationMap添加元素
     *
     * @param clazz			要注入的class类型
     * @param object		注册的bean对象
     */
    private void putAnnotationMap(Class<?> clazz, Object object){
        Annotation[] annotations = clazz.getAnnotations();
        List<Object> listObject = null;
        for(Annotation annotation : annotations){
            if(null != annotation){
                listObject = ANNOTATION_CONTAINER.get(annotation.annotationType());
                if(CollectionKit.isEmpty(listObject)){
                    listObject = CollectionKit.newArrayList();
                }
                listObject.add(object);
                this.put(annotation.annotationType(), listObject);
            }
        }
    }

    /**
     * annotationBean容器存储
     *
     * @param clazz			允许注册的Annotation类型
     * @param listObject	要注入的对象列表
     */
    private void put(Class<? extends Annotation> clazz, List<Object> listObject){
        if(null == ANNOTATION_CONTAINER.get(clazz)){
            ANNOTATION_CONTAINER.put(clazz, listObject);
        }
    }

    /**
     * 初始化注入
     */
    @Override
    public void initWired() throws RuntimeException {
        Iterator<Object> it = BEAN_CONTAINER.values().iterator();
        try {
            while (it.hasNext()) {

                Object obj = it.next();

                // 所有字段
                Field[] fields = obj.getClass().getDeclaredFields();
                for (Field field : fields) {

                    // 需要注入的字段
                    Inject inject = field.getAnnotation(Inject.class);
                    if (null != inject) {

                        // 要注入的字段
                        Object injectField = this.getBean(field.getType(), Scope.SINGLE);

                        // 指定装配的类
                        if (inject.value() != Class.class) {
                            injectField = this.getBean(inject.value(), Scope.SINGLE);
                            // 容器有该类
                            if (null == injectField) {
                                injectField = this.registBean(inject.value());
                            }
                        } else{
                            // 没有指定装配class, 容器没有该类，则创建一个对象放入容器
                            if (null == injectField) {
                                injectField = this.registBean(field.getType());
                            }
                        }
                        if (null == injectField) {
                       //      throw new Exception("Unable to load " + field.getType().getCanonicalName() + "！");
                        }
                        boolean accessible = field.isAccessible();
                        field.setAccessible(true);
                        field.set(obj, injectField);
                        field.setAccessible(accessible);
                    }
                }
            }
        } catch (SecurityException e) {
            LOGGER.error(e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage());
        } catch (IllegalAccessException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * 判断是否是可以注册的bean
     *
     * @param annotations		注解类型
     * @return 					true:可以注册 false:不可以注册
     */
    @Override
    public boolean isRegister(Annotation[] annotations) {
        if (null == annotations || annotations.length == 0) {
            return false;
        }
        for (Annotation annotation : annotations) {
            if (annotation instanceof Component || annotation instanceof Path) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Class<?>> getClassesByAnnotation(Class<? extends Annotation> annotation) {
        List<Object> objectList = getBeansByAnnotation(annotation);
        if(!CollectionKit.isEmpty(objectList)){
            List<Class<?>> classList = CollectionKit.newArrayList(objectList.size());
            for(Object object : objectList){
                classList.add(object.getClass());
            }
            return classList;
        }
        return null;
    }

    @Override
    public <T> List<T> getBeansByAnnotation(Class<? extends Annotation> annotation) {
        return (List<T>) ANNOTATION_CONTAINER.get(annotation);
    }

    @Override
    public void registBean(Set<Class<?>> classes) {
        if(!CollectionKit.isEmpty(classes)){
            for(Class<?> clazz : classes){
                this.registBean(clazz);
            }
        }
    }

    @Override
    public Object registBean(Object object) {
        String name = object.getClass().getName();
        put(name, object);
        return object;
    }

    @Override
    public boolean removeAll() {
        BEAN_CONTAINER.clear();
        ANNOTATION_CONTAINER.clear();
        return true;
    }
}
