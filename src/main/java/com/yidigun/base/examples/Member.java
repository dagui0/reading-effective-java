package com.yidigun.base.examples;

import com.yidigun.base.DomainObject;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.Instant;

/// [MemberKey] 를 PK로 사용하는 마스터 테이블용 도메인 클래스.
///
@Getter
@Builder(toBuilder = true)
@EqualsAndHashCode
public class Member implements DomainObject<MemberKey>, MemberKey.Aware {

    /// primary key
    private long memberNo;
    /// semantic field
    private String name;
    /// semantic field
    private Instant registerDate;
    /// logging field
    @EqualsAndHashCode.Exclude
    private Instant createDate;
    /// logging field
    @EqualsAndHashCode.Exclude
    private Instant updateDate;

    @Override
    public MemberKey getPrimaryKey() {
        return MemberKey.of(memberNo);
    }

    /// lombok @Builder 용 빌더클래스
    public static class MemberBuilder implements MemberKey.Aware.Builder<MemberBuilder> {
        MemberBuilder primaryKey(MemberKey key) {
            return memberKey(key);
        }
    }
}
