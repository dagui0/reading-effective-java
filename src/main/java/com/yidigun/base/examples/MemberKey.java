package com.yidigun.base.examples;

import com.yidigun.base.PrimaryKey;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/// 독립 PK 클래스 예시.
///
/// 회원 테이블 처럼 여러 테이블과 릴레이션을 가지는 경우 PK가 독립된 클래스로 구현되는 것이 좋다.
/// 
/// ## [MemberKey.Aware] 인터페이스
/// 
/// [MemberKey]를 사용하는 도메인 객체용 클래스를 위한 인터페이스이다.
/// 
/// ```
/// public class Member implements DomainObject<MemberKey>, MemberKey.Aware {
///     ...
///     private long memberNo; // primary key
///     ...
///     @Override
///     public MemberKey getPrimaryKey() { return MemberKey.of(memberNo); }
///     ...
/// }
/// public class Post implements DomainObject<Post.Key>, MemberKey.Aware {
///     ...
///     private long memberNo; // 작성자 회원번호
///     ...
/// }
/// ```
/// 
/// ## [MemberKey.Aware.Builder] 인터페이스
/// 
/// [MemberKey.Aware] 객체 생성에 사용할 Builder 용 인터페이스이다.
///
/// [lombok.Builder]를 이용하여 빌더를 자동생성하면 `클래스명 + Builder`라는 이름으로
/// `static` 중첩 클래스가 생성된다. 이 클래스는 미리 중첩클래스를 생성해 놓아도
/// lombok은 잘 알아서 빌더 메소드들을 추가해준다.
///
/// [MemberKey.Aware.Builder#memberKey(MemberKey)]가 기본 메소드로 구현되어있어
/// [lombok.Builder]과 함께 사용하면 중첩 클래스를 만들기만 하면 추가로 할 일이 없다.
/// 
/// ```
/// @Builder(toBuilder = true)
/// class Member implements DomainObject<MemberKey>, MemberKey.Aware {
///     ...
///     public static class MemberBuilder implements MemberKey.Aware.Builder<MemberBuilder> {}
///     ...
/// }
/// ```
/// 
/// ### [MemberKey.Aware.Builder]가 제네릭 타입을 사용하는 이유
/// 
/// 빌더 패턴의 모든 빌더 메소드는 빌더 인스턴스를 리턴해야 한다.
/// 이때, 기본 메소드가 `this`를 리턴하면 `this`는 실제 빌더 인스턴스의 클래스가 아니라
/// 인터페이스 타입이 되어서 이후 빌드 메소드 체인이 깨지게 된다.
/// 
/// ## `null` 값 변환 문제
/// 
/// 이 클래스는 [#memberNo]가 `<= 0`인 경우 null로 간주한다는 정책에 따라 구현되었다.
/// `null` 관련 정책은 프로젝트의 DB설계 및 개발 컨벤션의 기본 정책에 따라서 얼마든지 달라질 수 있다.
/// (`long` 대신 [Long]을 사용하면 `null`을 자연스럽게 허용할 수도 있다.)
///
/// 이 예시에서는 [MemberKey] 객체를 사용하여 기본 타입 `long`과의 호환성을
/// 최대한 가져가는 방법을 탐구한다.
/// 
/// * 기본 정책: `memberNo <= 0` 인 경우 `null`로 간주한다.
/// * 생성자 [#MemberKey]와 팩토리 메소드 [#of(long)]를 통해
///   객체를 생성할 때는 `memberNo <= 0` 이면 [IllegalArgumentException] 예외를 던진다.
/// * 그렇지만, [MemberKey.Aware.Builder] 인터페이스를 통해서
///   [MemberKey.Aware] 객체가 생성될 때는 `memberNo`를 `0`으로 설정할 수 있는데,
///   이는 객체가 DB에 저장되어 PK값이 생성되기 전의 상황이거나
///   PK가 아닌 `memberNo` 필드의 값이 `null`임을 의미한다.
/// * 마찬가지로 [MemberKey.Aware#getMemberKey()] 메소드는 `memberNo <= 0`인 경우 `null`을 리턴한다.
///
/// @see PrimaryKey
/// @see Member
/// @see Address
/// @see Post
///
@EqualsAndHashCode
public final class MemberKey implements PrimaryKey, Comparable<MemberKey> {

    /// `null`로 간주할 `memberNo`의 값.
    /// `memberNo`가 `<= 0`인 경우 `null`로 간주한다.
    public static final long NULL_MEMBER_NO = 0L;

    /// 회원번호
    private final long memberNo;

    /// 생성자. `memberNo`가 올바르지 않은 경우(`<= 0`)
    /// [IllegalArgumentException]을 던진다.
    ///
    /// @param memberNo 회원 번호
    /// @throws IllegalArgumentException if memberNo <= 0
    private MemberKey(long memberNo) {
        if (memberNo <= NULL_MEMBER_NO) {
            throw new IllegalArgumentException("memberNo must be positive");
        }
        this.memberNo = memberNo;
    }

    /// 팩토리 메소드. `memberNo`가 올바르지 않은 경우(`<= 0`)
    /// [IllegalArgumentException]을 던진다.
    ///
    /// @param memberNo 회원 번호
    /// @return MemberKey 객체
    /// @throws IllegalArgumentException if memberNo <= 0
    public static MemberKey of(long memberNo) {
        return new MemberKey(memberNo);
    }

    /// 안전한 팩토리 메소드.
    /// `memberNo`가 올바르지 않은 경우라도 [Optional]로 안전하게 처리가 가능하다.
    ///
    /// @param memberNo 회원번호
    /// @return MemberKey 객체를 감싼 Optional 객체
    public static Optional<MemberKey> tryOf(long memberNo) {
        return Optional.ofNullable(
                (memberNo <= NULL_MEMBER_NO)? null: new MemberKey(memberNo));
    }

    /// [MemberKey]를 long으로 변환하기 위한 메소드.
    ///
    /// @return 회원 번호
    public long longValue() {
        return memberNo;
    }

    @Override
    public String toString() {
        return String.valueOf(memberNo);
    }

    /// 정렬을 위해 사용할 수 있는 크기 비교 메소드
    ///
    /// @param o 크기 비교할 대상 객체
    /// @return 음수: this < o, 0: this == o, 양수: this > o
    /// @see Comparable#compareTo(Object)
    @Override
    public int compareTo(@NotNull MemberKey o) {
        return Long.compare(this.memberNo, o.memberNo);
    }

    /// [MemberKey]를 사용하는 도메인 객체용 인터페이스.
    public interface Aware {

        /// [MemberKey]를 `long`으로 변환하기 위한 메소드.
        /// `null`을 리턴할 수 있으므로 확인해야 한다.
        /// `null` 확인을 피하려면 [#tryGetMemberKey()]를 사용한다.
        ///
        /// @return MemberKey 객체 또는 null
        default MemberKey getMemberKey() {
            return tryOf(getMemberNo()).orElse(null);
        }

        /// `null` 검사를 피하기 위해 사용할 수 있는 [MemberKey] 조회 메소드.
        ///
        /// @return MemberKey 객체를 감싼 Optional 객체
        default Optional<MemberKey> tryGetMemberKey() {
            return tryOf(getMemberNo());
        }

        /// [MemberKey]가 `null`이 아닌 올바른 값인지 확인하는 메소드.
        ///
        /// @return true: memberNo > 0
        default boolean hasValidMemberKey() {
            return getMemberNo() > NULL_MEMBER_NO;
        }

        /// [MemberKey] 객체를 사용하지 않고 memberNo를 직접 조회하는 방법.
        /// [lombok.Getter]에 의해 자동 생성할 수 있다.
        ///
        /// 이 메소드는 물리적 DB처리를 위한 경우에만 사용하고,
        /// 비즈니스 처리나 출력을 위해 사용하지 않는 것이 좋다.
        ///
        /// 회원 번호는 반드시 [MemberKey] 객체를 사용하여 처리하도록 한다.
        ///
        /// @return 회원 번호
        long getMemberNo();

        /// Builder 클래스에서 [MemberKey]를 `long`으로 변환하기 위한 인터페이스.
        /// [lombok.Builder]를 사용하여 [#memberNo(long)] 메소드가
        /// 자동 생성되었다면 추가로 할 일은 없다.
        ///
        /// @param <B> 구현 빌더 클래스 타입
        interface Builder<B extends Builder<B>> {

            /// [MemberKey]를 `memberNo`(`long`)으로 변환하기 위한 빌드 메소드.
            ///
            /// @param memberKey MemberKey 객체
            /// @return 빌더 인스턴스
            default B memberKey(MemberKey memberKey) {
                return memberNo((memberKey != null? memberKey.longValue(): NULL_MEMBER_NO));
            }

            /// [MemberKey.Aware#tryGetMemberKey()]를 사용하여 조회한 값을
            /// 그대로 빌더에 전달하는 경우를 위한 빌드 메소드.
            ///
            /// @param optMemberKey MemberKey 객체를 감싼 Optional 객체
            /// @return 빌더 인스턴스
            @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
            default B memberKey(Optional<MemberKey> optMemberKey) {
                return memberNo(optMemberKey.map(MemberKey::longValue).orElse(NULL_MEMBER_NO));
            }

            /// memberNo를 설정하기 위한 빌드 메소드.
            /// [lombok.Builder]를 사용하여 자동 생성 가능.
            ///
            /// @param memberNo 회원 번호
            /// @return 빌더 인스턴스
            B memberNo(long memberNo);
        }
    }
}
