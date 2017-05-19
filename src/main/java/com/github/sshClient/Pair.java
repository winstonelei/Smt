package com.github.sshClient;

import java.io.Serializable;

/**
 * Created by dell on 2016/12/6.
 */
public class Pair<T1, T2> implements Serializable {
    private static final long serialVersionUID = -3986244606585552569L;

    protected T1 first = null;
    protected T2 second = null;

    /**
     * Default constructor.
     */
    public Pair() {
    }

    /**
     * Constructor
     * @param a operand
     * @param b operand
     */
    public Pair(T1 a, T2 b) {
        this.first = a;
        this.second = b;
    }

    /**
     * Constructs a new pair, inferring the type via the passed arguments
     * @param <T1> type for first
     * @param <T2> type for second
     * @param a first element
     * @param b second element
     * @return a new pair containing the passed arguments
     */
    public static <T1, T2> Pair<T1, T2> newPair(T1 a, T2 b) {
        return new Pair<T1, T2>(a, b);
    }

    private static boolean equals(Object x, Object y) {
        return (x == null && y == null) || (x != null && x.equals(y));
    }

    /**
     * Return the first element stored in the pair.
     * @return T1
     */
    public T1 getFirst() {
        return first;
    }

    /**
     * Replace the first element of the pair.
     * @param a operand
     */
    public void setFirst(T1 a) {
        this.first = a;
    }

    public T1 getKey() {
        return getFirst();
    }

    public void setKey(T1 a) {
        setFirst(a);
    }

    /**
     * Return the second element stored in the pair.
     * @return T2
     */
    public T2 getSecond() {
        return second;
    }

    /**
     * Replace the second element of the pair.
     * @param b operand
     */
    public void setSecond(T2 b) {
        this.second = b;
    }

    public T2 getValue() {
        return getSecond();
    }

    public void setValue(T2 b) {
        setSecond(b);
    }

    @Override
    @SuppressWarnings("rawtypes")
    public boolean equals(Object other) {
        return other instanceof Pair && equals(first, ((Pair) other).first) && equals(second, ((Pair) other).second);
    }

    @Override
    public int hashCode() {
        if (first == null)
            return (second == null) ? 0 : second.hashCode() + 1;
        else if (second == null)
            return first.hashCode() + 2;
        else
            return first.hashCode() * 17 + second.hashCode();
    }

    @Override
    public String toString() {
        return "{" + getFirst() + "," + getSecond() + "}";
    }
}

