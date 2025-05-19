package com.yidigun.base.examples;

import com.yidigun.base.DomainObject;
import com.yidigun.base.PrimaryKey;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.Instant;

/// [MemberKey] 를 non-PK/FK로 사용하는 테이블용 도메인 클래스.
/// [Member]와 1:N의 비식별 관계(Non-identifying Relationship)를 가진다.
///
@Getter
@Builder(toBuilder = true)
@EqualsAndHashCode
public class Post implements DomainObject<Post.Key>, MemberKey.Aware {

    /// primary key
    private long postNo;
    /// semantic field
    private String title;
    /// semantic field
    private String content;
    /// semantic field from Member
    private long memberNo;
    /// logging field
    @EqualsAndHashCode.Exclude
    private Instant createDate;
    /// logging field
    @EqualsAndHashCode.Exclude
    private Instant updateDate;

    @Override
    public Post.Key getPrimaryKey() {
        return new Key(postNo);
    }

    /// lombok @Builder 용 빌더클래스
    public static class PostBuilder implements MemberKey.Aware.Builder<PostBuilder> {
        PostBuilder primaryKey(Post.Key key) {
            return postNo(key.postNo());
        }
    }

    /// PrimaryKey 클래스
    /// @param postNo 게시물 일련번호
    public record Key(long postNo) implements PrimaryKey {}
}
