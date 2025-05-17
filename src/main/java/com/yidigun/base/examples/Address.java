package com.yidigun.base.examples;

import com.yidigun.base.DomainObject;
import com.yidigun.base.PrimaryKey;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.Instant;

/// [MemberKey] 를 PK/FK로 사용하는 테이블용 도메인 클래스.
/// [Member]와 1:N의 식별 관계(Identifying Relationship)를 가진다.
/// 
@Getter
@Builder(toBuilder = true)
@EqualsAndHashCode
public class Address implements DomainObject<Address.Key>, MemberKey.Aware {

    private final long memberNo;        // primary key from Member
    private final long addressNo;       // primary key
    private final String address;       // semantic field
    @EqualsAndHashCode.Exclude
    private final Instant createDate;   // logging field

    @Override
    public Key getPrimaryKey() {
        return new Key(memberNo, addressNo);
    }

    public static class AddressBuilder implements MemberKey.Aware.Builder<AddressBuilder> {}

    public static record Key(long memberNo, long addressNo) implements PrimaryKey, MemberKey.Aware {

        public static Key of(MemberKey memberKey, long addressNo) {
            return new Key(memberKey.longValue(), addressNo);
        }

        @Override
        public MemberKey getMemberKey() {
            return MemberKey.of(memberNo);
        }

        @Override
        public long getMemberNo() {
            return memberNo;
        }
    }
}
