package lambdaspecial.java;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class QuickSortLambda<T extends Comparable<T>> implements Sort<T> {

    @Override
    public List<T> sort(List<T> array) {
        if (array == null || array.size() < 2) {
            return array;
        }

        T pivot = array.get(array.size() / 2);

        Map<Integer,List<T>> grouped = array.stream()
                .collect(
                        Collectors.groupingBy(e -> Integer.compare(e.compareTo(pivot), 0),
                        Collectors.mapping(e -> e, Collectors.toList())));

        var less = grouped.get(-1);
        var equal = grouped.get(0);
        var greater = grouped.get(1);
        return Stream.of(sort(less), equal, sort(greater))
                    .filter(Objects::nonNull)
                    .flatMap(List::stream)
                    .toList();
    }
}
