package com.rudolfs.examples.lombok.builder;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@EqualsAndHashCode
@ToString
@Getter
@Accessors(fluent = true)
@SuperBuilder(toBuilder = true)
public abstract class AbstractAnalyticsEvent {

    @NonNull
    @JsonProperty("sessionId")
    @JsonPropertyDescription("An identifier that uniquely identifies the user session")
    private final String sessionId;

    @NonNull
    @JsonProperty("userAgent")
    @JsonPropertyDescription("Full user agent string as provided by the client")
    @Builder.Default
    private final String userAgent = "Mozilla";

    @JsonProperty("timestamp")
    @JsonPropertyDescription("The date when this event was fired")
    private final Instant timestamp;

    @NonNull
    @JsonGetter("eventType")
    @JsonPropertyDescription("The name of the event")
    public abstract String eventType();
}
