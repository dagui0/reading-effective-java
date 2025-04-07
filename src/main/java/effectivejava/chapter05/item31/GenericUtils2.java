package effectivejava.chapter05.item31;

import java.util.*;

public class GenericUtils2 {

    @SuppressWarnings({"rawtypes","unchecked"})
    public static Set union1(Set s1, Set s2) {
        Set result = new HashSet(s1);
        result.addAll(s2);
        return result;
    }

    public static <T> Set<T> union2(Set<? extends T> s1, Set<? extends T> s2) {
        Set<T> result = new HashSet<>(s1);
        result.addAll(s2);
        return result;
    }

    public static <E extends Comparable<? super E>> E max(Collection<? extends E> c) {
        return max(c, Comparator.naturalOrder());
    }

    public static <E extends Comparable<? super E>> E min(Collection<? extends E> c) {
        return min(c, Comparator.naturalOrder());
    }

    public static <E> E max(Collection<? extends E> c, Comparator<? super E> comparator) {
        if (c.isEmpty())
            throw new IllegalArgumentException("Collection is empty");

        E result = null;
        for (E e : c) {
            if (e != null && (result == null || comparator.compare(e, result) > 0)) {
                result = e;
            }
        }
        return result;
    }

    public static <E> E min(Collection<? extends E> c, Comparator<? super E> comparator) {
        return max(c, comparator.reversed());
    }

    public static void swap(List<?> list, int i, int j) {
        swapHelper(list, i, j);
    }

    private static <T> void swapHelper(List<T> list, int i, int j) {
        T temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }
}
