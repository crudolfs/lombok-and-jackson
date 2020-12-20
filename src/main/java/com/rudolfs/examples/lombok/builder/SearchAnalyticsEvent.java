package com.rudolfs.examples.lombok.builder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
@Accessors(fluent = true)
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@SuperBuilder(toBuilder = true)
public class SearchAnalyticsEvent extends AbstractAnalyticsEvent {

    private static final String DEFAULT_EVENT_TYPE = "search";

    @NonNull
    @Builder.Default
    private final String eventType = DEFAULT_EVENT_TYPE;

    @JsonProperty("searchWords")
    @JsonPropertyDescription("The words that have been searched for")
    @Builder.Default
    private final List<String> searchWords = new ArrayList<>();

    @Override
    public String eventType() {
        return this.eventType;
    }
}
