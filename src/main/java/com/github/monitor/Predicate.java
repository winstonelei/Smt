package com.github.monitor;

/**
 * Predicate is used in conditional execution
 *
 * @param <T>
 */
public interface Predicate<T> {

    boolean apply(T item);

    public static class PassAll implements Predicate<Object> {
        public static final PassAll INSTANCE = new PassAll();

        public boolean apply(Object item) {
            return true;
        }
    }
}
