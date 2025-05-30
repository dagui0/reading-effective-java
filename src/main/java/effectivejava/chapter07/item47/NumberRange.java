package effectivejava.chapter07.item47;

import java.util.AbstractList;

class NumberRange extends AbstractList<Integer> {
    private final int startInclusive;
    private final int endInclusive;
    private final int size;

    public NumberRange(int startInclusive, int endInclusive) {
        if (endInclusive < startInclusive) {
            throw new IllegalArgumentException("끝 값은 시작 값보다 작을 수 없습니다.");
        }
        this.startInclusive = startInclusive;
        this.endInclusive = endInclusive;
        this.size = (endInclusive - startInclusive) + 1;
    }

    @Override
    public Integer get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("인덱스: " + index + ", 크기: " + size);
        }
        return startInclusive + index;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean contains(Object o) {
        if (!(o instanceof Integer value)) {
            return false;
        }
        return value >= startInclusive && value <= endInclusive;
    }
}
