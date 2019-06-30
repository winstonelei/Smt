package com.github.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Top k based on minimum heap.
 */
public final class TopKMinHeap<T extends Comparable<T>> {
    private final int size;
    private final List<T> heap;

    /**
     * Create a new heap
     * @param comparator A comparator that handles elements of type T.
     */
    public TopKMinHeap(int size) {
        this.size = size;
        this.heap = new ArrayList<T>(this.size);
    }

    /**
     * Build heap from array.
     */
    private void buildHeap() {
        for (int i = size / 2 - 1; i >= 0; i--) {
            heapify(i);
        }
    }

    private void heapify(int i) {
        // 获取左右结点的数组下标
        int l = left(i);
        int r = right(i);

        // 这是一个临时变量，表示 跟结点、左结点、右结点中最小的值的结点的下标
        int smallest = i;

        // 存在左结点，且左结点的值小于根结点的值
        if (l < size && heap.get(l).compareTo(heap.get(i)) < 0)
            smallest = l;

        // 存在右结点，且右结点的值小于以上比较的较小值
        if (r < size && heap.get(r).compareTo(heap.get(smallest)) < 0) {
            smallest = r;
        }

        // 左右结点的值都大于根节点，直接return，不做任何操作
        if (i == smallest) {
            return;
        }

        // 交换根节点和左右结点中最小的那个值，把根节点的值替换下去
        swap(i, smallest);

        // 由于替换后左右子树会被影响，所以要对受影响的子树再进行heapify
        heapify(smallest);
    }

    // 获取右结点的数组下标
    private int right(int i) {
        return (i + 1) << 1;
    }

    // 获取左结点的数组下标
    private int left(int i) {
        return ((i + 1) << 1) - 1;
    }

    // 交换元素位置
    private void swap(int i, int j) {
        T tmp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, tmp);
    }

    // 获取对中的最小的元素，根元素
    public T getRoot() {
        return heap.get(0);
    }

    // 替换根元素，并重新heapify
    public void push(T t) {
        if (heap.size() < size) {
            heap.add(t);
            if (heap.size() == size) {
                buildHeap();
            }
        } else {
            if (t.compareTo(heap.get(0)) < 0) {
                return;
            }
            heap.set(0, t);
            heapify(0);
        }
    }

    public List<T> sort() {
        List<T> sortedList = new ArrayList<T>(heap);
        Collections.sort(sortedList);
        Collections.reverse(sortedList);
        return sortedList;
    }

    public List<T> getHeap() {
        return heap;
    }

    public static void main(String[] args) {
        TopKMinHeap<Double> tkm = new TopKMinHeap<Double>(10);
        List<Double> test = new ArrayList<Double>();
        for (int i = 1; i < 10; ++i) {
            Double d = Math.random() * 100;
            test.add(d);
            tkm.push(d);
        }
        Collections.sort(test);
        Collections.reverse(test);
        System.out.println(Arrays.toString(test.toArray()));
        System.out.println(Arrays.toString(tkm.sort().toArray()));
    }
}
