package com.game.puzzle.logic;

import java.awt.*;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Array;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Small helper that helps with iterating
 * over two dimensional arrays
 *
 * @param <T>
 */
public class ArrayIterator<T> {
    @FunctionalInterface
    public interface Iterator<T> {
        T iterate(T obj, int x, int y);
    }

    @FunctionalInterface
    public interface FilterIterator<T> {
        boolean iterate(T obj, int x, int y);
    }

    private Dimension size;
    public T[][] array;

    /**
     * Create new instance
     *
     * @param cls   Instance class
     * @param size  Size of array
     */
    @SuppressWarnings("unchecked")
    ArrayIterator(Class<? extends T> cls, @NotNull Dimension size) {
        this.size = size;
        this.array = (T[][])Array.newInstance(cls, size.width, size.height);
    }

    /**
     * Use existing array
     *
     * @param array 2D array used in class
     * @param size  Size of array
     */
    private ArrayIterator(T[][] array, @NotNull Dimension size) {
        this.size = size;
        this.array = array;
    }

    public Dimension getSize() { return size; }

    /**
     * Random shuffle 2D array
     */
    public ArrayIterator<T> shuffle() {
        final ThreadLocalRandom localRandom = ThreadLocalRandom.current();

        return this.map(
                (element, x, y) -> {
                    final int x1 = localRandom.nextInt(0, size.height);
                    final int y1 = localRandom.nextInt(0, size.width);

                    final T temp = array[y1][x1];
                    array[y1][x1] = element;
                    array[y][x] = temp;
                    return null;
                }
        );
    }

    /**
     * Iterate through 2D array
     *
     * @param iterator  Function called every iterate
     */
    public ArrayIterator<T> map(Iterator<T> iterator) {
        for (int i = size.height - 1; i >= 0; --i) {
            for (int j = size.width - 1; j >= 0; --j) {
                T newValue = iterator.iterate(array[i][j], j, i);
                if (newValue != null)
                    array[i][j] = newValue;
            }
        }
        return this;
    }

    /**
     * @param iterator  Function called every iterate
     * @return  Value coordinate inside array
     */
    public Point find(FilterIterator<T> iterator) {
        for (int i = size.height - 1; i >= 0; --i) {
            for (int j = size.width - 1; j >= 0; --j) {
                if (iterator.iterate(array[i][j], j, i))
                    return new Point(j, i);
            }
        }
        return null;
    }

    /**
     * Foreach existing array
     *
     * @param size      2D size of array
     * @param array     Array object
     * @param iterator  Map functor
     * @param <K>       Fluent api object
     */
    public static <K> ArrayIterator<K> map(K[][] array, Dimension size, Iterator<K> iterator) {
        ArrayIterator<K> wrapped = new ArrayIterator<K>(array, size);
        wrapped.map(iterator);
        return wrapped;
    }
}
