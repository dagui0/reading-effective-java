package com.yidigun.base.examples;

import com.yidigun.base.DomainObject;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.Instant;

/**
 * {@link MemberKey} 를 PK로 사용하는 마스터 테이블용 도메인 클래스.
 */
@Getter
@Builder(toBuilder = true)
@EqualsAndHashCode
public class Member implements DomainObject<MemberKey>, MemberKey.Aware {

    private long memberNo;          // primary key
    private String name;            // semantic field
    private Instant registerDate;   // semantic field
    @EqualsAndHashCode.Exclude
    private Instant createDate;     // logging field
    @EqualsAndHashCode.Exclude
    private Instant updateDate;     // logging field

    @Override
    public MemberKey getPrimaryKey() {
        return MemberKey.of(memberNo);
    }

    public static class MemberBuilder implements MemberKey.Aware.Builder<MemberBuilder> {}
}
