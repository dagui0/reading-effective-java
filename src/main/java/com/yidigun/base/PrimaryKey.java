package com.yidigun.base;

import com.yidigun.base.examples.MemberKey;
import com.yidigun.base.examples.ResidentKey;

import java.util.Map;

/// [DomainObject]의 Primary Key의 값을 담는 DTO(Data transfer object) 인터페이스.
/// 이 인터페이스는 PK만을 비교할 때 사용하거나, [Map]의 키로 사용해야 할 경우 사용할 수 있다.
///
/// 이 인터페이스는 단순한 마커 인터페이스로 구현해야 할 메소드는 없다.
/// 하지만 DB 조회용 클래스를 위한 디자인 패턴에서 핵심적인 중심으로 작용한다.
///
/// 단순 키(단일컬럼)를 사용하는 경우에도 반드시 Key용 클래스를 만드는 것이 좋은데
/// 그 이유는 보통 PK에 사용되는 `int(Integer)`, `long(Long)`, `String` 등의
/// 기본 타입은 범용 타입이라서 어느 테이블의 키인지 확인할 수 없기 때문이다.
///
/// 키 전용 래핑 클래스를 만들게 되면 타입 안전성이 향상되고 버그 발생 가능성이 많이 줄어들 수 있다.
/// 
/// ## 추가 인터페이스 구현
///
/// 필요에 따라 [Comparable]이나, [CharSequence] 등을 추가로 구현할 수 있다.
///
/// ### [Comparable] 구현
/// 정렬에 사용될 수 있게 하려면 [Comparable]를 추가로 구현할 수 있다.
/// 
/// ```
/// public class Example implements DomainObject<Example.Key> {
///     primary int no; // primary key
///     public record Key(int no) implements PrimaryKey, Comparable<Key> {
///         @Override
///         public int compareTo(@NotNull Key key) {
///             return Integer.compare(this.no, key.no());
///         }
///     }
///     @Override
///     public Key getPrimaryKey() {
///        return new Key(no);
///     }
/// }
/// 
/// TreeMap<Example.Key, Example> map = new TreeMap<>();
/// map.put(example.getPrimaryKey(), example);
/// ```
///
/// ### [CharSequence] 구현
/// 만약 키의 정의가 고정 자리수별로 특정한 의미를 나타내는 경우(Structured & Semantic Key)
/// [CharSequence]를 구현하여 편의기능을 추가할 수 있다.
/// 
/// ```
/// public record ResidentId(String residentId) implements PrimaryKey, CharSequence {
///     public boolean isMale() { return valueAt(7) % 2 == 1; }
///     public boolean isFemale() { return valueAt(7) % 2 == 0; }
///     public int valueAt(int index) { return Character.getNumericValue(charAt(index)); }
///     @Override
///     public int length() { return residentId.length(); }
///     @Override
///     public char charAt(int index) { return residentId.charAt(index); }
///     @Override
///     public @NotNull String subSequence(int start, int end) {
///         return (String)residentId.subSequence(start, end);
///     }
/// }
/// 
/// ResidentId residentId = new ResidentId("1111111111118");
/// String birthday = residentId.subSequence(0, 6);
/// assertTrue(residentId.isMale());
/// ```
/// 
/// ## Key 타입을 독립 클래스로 만들 것인지 중첩 클래스로 만들 것인지
/// 
/// 특정 테이블의 PK가 여러 테이블에서 FK로 많이 사용되는 경우 독립된 클래스로 만드는 것이 좋다.(예: 회원번호 등)
/// 반면에 한 테이블에서만 사용하거나, 컬럼 수가 많은 복합키인 경우 등은 간단하게 중첩 클래스로 만드는 것이 편리하다.
/// 
/// ### 독립 클래스 예시
/// 
/// ```
/// /// memberNo는 Member테이블의 PK
/// public record MemberKey(long memberNo) implements PrimaryKey, Comparable<MemberKey> {
///     ...
///     public static MemberKey of(long memberNo) { return new MemberKey(memberNo); }
///     public long longValue() { return memberNo; }
///     ...
///     /// memberNo를 PK또는 필드에 사용하는 테이블용 인터페이스
///     /// 기본 메소드를 이용하여 메소드를 미리 구현해 놓는다. (Java 8 이상)
///     public interface Aware {
///         default MemberKey getMemberKey() { return MemberKey.of(getMemberNo()); }
///         long getMemberNo(); // lombok @Getter 사용하여 생성 가능
/// 
///         /// Aware 객체를 만드는 빌더용 인터페이스 (long과 MemberKey를 변환하기 위한)
///         public interface Builder<B extends Builder<B>> {
///             default B memberKey(MemberKey key) { return memberNo(key.longValue()); }
///             B memberNo(long memberNo); // lombok @Builder 사용하여 생성 가능
///         }
///     }
///     ...
/// }
///
/// /// PK의 원본 테이블
/// @Getter
/// @Builder(toBuilder = true)
/// public class Member implements DomainObject<MemberKey>, MemberKey.Aware {
///     private long memberNo; // primary key
///     ...
///     public MemberKey getPrimaryKey() { return MemberKey.of(memberNo); }
///     ...
///     public static class MemberBuilder implements MemberKey.Aware.Builder<MemberBuilder> {}
///     ...
/// }
///
/// /// 부모 테이블의 PK를 상속받은 복합키 테이블(식별관계, Identifying Relationship)
/// @Getter
/// @Builder(toBuilder = true)
/// public class Address implements DomainObject<Address.Key>, MemberKey.Aware {
///     private long memberNo; // primary key from foreign table
///     private long addressNo; // primary key
///     ...
///     public Address.Key getPrimaryKey() { return new Address.Key(memberNo, addressNo); }
///     ...
///     // Address.Key 역시 MemberKey.Aware임
///     // 단, record 클래스에는 @Getter를 붙일 수 없다.
///     public record Key(long memberNo, long addressNo) implements PrimaryKey, MemberKey.Aware {
///         @Override public long getMemberNo() { return memberNo; }
///     }
///     ...
///     public static class AddressBuilder implements MemberKey.Aware.Builder<AddressBuilder> {
///         AddressBuilder addressKey(Address.Key key) {
///             return memberNo(key.memberNo())
///                   .addressNo(key.addressNo());
///         }
///     }
///     ...
/// }
///
/// /// 부모 테이블의 PK를 단순 참조키로 사용하는 테이블(비식별관계, Non-identifying Relationship)
/// @Getter
/// @Builder(toBuilder = true)
/// public class Post implements DomainObject<Post.Key>, MemberKey.Aware {
///     private long postNo; // primary key
///     private String content; // semantic field
///     private long memberNo; // semantic field from Member
///     ...
///     @Override public Post.Key getPrimaryKey() { return new Post.Key(postNo); }
///     public record Key(long postNo) implements PrimaryKey {}
///     public static class PostBuilder implements MemberKey.Aware.Builder<PostBuilder> {}
///     ...
///  }
/// ```
/// 
/// @see DomainObject DomainObject(도메인 객체 인터페이스)
/// @see MemberKey MemberKey(독립 클래스방식 구현 예시)
/// @see ResidentKey ResidendId(Semantic Key 예시 (주민등록번호))
public interface PrimaryKey {
}
