package com.yidigun.base.examples;

import com.yidigun.base.DomainObject;
import com.yidigun.base.PrimaryKey;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.Instant;

/**
 * {@link MemberKey} 를 non-PK/FK로 사용하는 테이블용 도메인 클래스.
 * {@link Member}와 1:N의 비식별 관계(Non-identifying Relationship)를 가진다.
 */
@Getter
@Builder(toBuilder = true)
@EqualsAndHashCode
public class Post implements DomainObject<Post.Key>, MemberKey.Aware {

    private long postNo;          // primary key
    private String title;         // semantic field
    private String content;       // semantic field
    private long memberNo;        // semantic field from Member
    @EqualsAndHashCode.Exclude
    private Instant createDate;   // logging field
    @EqualsAndHashCode.Exclude
    private Instant updateDate;   // logging field

    @Override
    public Post.Key getPrimaryKey() {
        return new Key(postNo);
    }

    public static class PostBuilder implements MemberKey.Aware.Builder<PostBuilder> {}

    public static record Key(long postNo) implements PrimaryKey {}
}
