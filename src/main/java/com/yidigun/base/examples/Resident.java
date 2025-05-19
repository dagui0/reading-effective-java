package com.yidigun.base.examples;

import com.yidigun.base.DomainObject;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

@Getter
@EqualsAndHashCode
@Builder(toBuilder = true)
public final class Resident implements DomainObject<ResidentKey>, ResidentKey.Aware {

    private final String residentId;
    private final String name;
    private final String address;
    @EqualsAndHashCode.Exclude
    private Instant createDate;

    public static class ResidentBuilder implements ResidentKey.Aware.Builder<ResidentBuilder> {}

    @Override
    public ResidentKey getPrimaryKey() {
        return ResidentKey.ofUnchecked(residentId);
    }
}
