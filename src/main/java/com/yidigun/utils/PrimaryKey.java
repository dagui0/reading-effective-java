package com.yidigun.utils;

/**
 * {@link BaseDomainObject}의 Primary Key를 나타내는 인터페이스.
 *
 * 이 인터페이스는 PK만을 비교할 때 사용하거나, {@link java.util.Map}의 키로 사용해야 할 경우 사용할 수 있다.
 *
 * 필요에 따라 {@link Comparable} 등을 추가로 구현할 수 있다.
 *
 * <code>
 *  public class Example implements BaseDomainObject<Example.Key> {
 *     primary int no; // primary key
 *     public static record Key(int no) implements PrimaryKey, Comparable<Key> {
 *         @Override
 *         public int compareTo(@NotNull Key key) {
 *             return Integer.compare(this.no, key.no());
 *         }
 *     }
 *     @Override
 *     public Key getPrimaryKey() {
 *        return new Key(no);
 *     }
 *  }
 *
 *  TreeMap<Example.Key, Example> map = new TreeMap<>();
 *  map.put(example.getPrimaryKey(), example);
 * </code>
 *
 * @see BaseDomainObject
 */
public interface PrimaryKey {
}
