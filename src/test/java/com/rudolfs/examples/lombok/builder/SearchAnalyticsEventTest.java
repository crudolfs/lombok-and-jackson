package com.rudolfs.examples.lombok.builder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.time.Instant;
import java.util.List;

import static com.rudolfs.examples.json.ObjectMapperFactory.objectMapper;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SearchAnalyticsEventTest {

    private static final Instant NOW = Instant.now();

    @Test
    void serialize() throws JsonProcessingException, JSONException {
        AbstractAnalyticsEvent analyticsEvent = SearchAnalyticsEvent.builder()
                .sessionId("01234")
                .userAgent("Chrome")
                .timestamp(NOW)
                .eventType("search_custom")
                .searchWords(List.of("name", "location"))
                .build();

        String actualJson = objectMapper().writeValueAsString(analyticsEvent);
        String expectedJson = String.format("{\"sessionId\":\"01234\",\"userAgent\":\"Chrome\",\"timestamp\":\"%s\",\"eventType\":\"search_custom\",\"searchWords\":[\"name\",\"location\"]}",
                NOW);
        JSONAssert.assertEquals(expectedJson, actualJson, true);
    }

    @Test
    void deserialize() throws JsonProcessingException {
        String json = String.format("{\"sessionId\":\"01234\",\"userAgent\":\"Chrome\",\"timestamp\":\"%s\",\"eventType\":\"search_custom\",\"searchWords\":[\"name\",\"location\"]}",
                NOW);

        AbstractAnalyticsEvent analyticsEvent = objectMapper().readValue(json, SearchAnalyticsEvent.class);

        AbstractAnalyticsEvent expectedAnalyticsEvent = SearchAnalyticsEvent.builder()
                .sessionId("01234")
                .userAgent("Chrome")
                .timestamp(NOW)
                .eventType("search_custom")
                .searchWords(List.of("name", "location"))
                .build();

        assertThat(analyticsEvent).isEqualTo(expectedAnalyticsEvent);
    }

    @Test
    void objectInstantiationSucceedsOnMissingOptionalProperty() {
        assertThatCode(() -> SearchAnalyticsEvent.builder()
                .sessionId("01234")
                .userAgent("Chrome")
                .eventType("search_custom")
                .searchWords(List.of("name", "location"))
                .build()
        ).doesNotThrowAnyException();
    }

    @Test
    void objectInstantiationFailsOnMissingRequiredProperty() {
        assertThatThrownBy(() -> SearchAnalyticsEvent.builder()
                .userAgent("Chrome")
                .eventType("search_custom")
                .searchWords(List.of("name", "location"))
                .build()
        ).isInstanceOf(NullPointerException.class)
                .hasMessage("sessionId is marked non-null but is null");
    }

    @Test
    void objectInstantiationSucceedsWithDefaultValues() {
        SearchAnalyticsEvent analyticsEvent = SearchAnalyticsEvent.builder()
                .sessionId("01234")
                .build();

        assertThat(analyticsEvent.eventType()).isEqualTo("search");
        assertThat(analyticsEvent.userAgent()).isEqualTo("Mozilla");
        assertThat(analyticsEvent.searchWords()).isEmpty();
    }

    @Test
    void doesNotSerializeEmptyOptionalFields() throws JsonProcessingException, JSONException {
        SearchAnalyticsEvent analyticsEvent = SearchAnalyticsEvent.builder()
                .sessionId("01234")
                .build();

        assertThat(analyticsEvent.timestamp()).isNull();
        assertThat(analyticsEvent.searchWords()).isEmpty();

        String actualJson = objectMapper().writeValueAsString(analyticsEvent);
        String expectedJson = "{\"sessionId\":\"01234\",\"userAgent\":\"Mozilla\",\"eventType\":\"search\"}";
        JSONAssert.assertEquals(expectedJson, actualJson, true);
    }

    @Test
    void deserializeMissingFieldWithDefaultValue() throws JsonProcessingException {
        String json = "{\"sessionId\":\"01234\",\"searchWords\":[\"name\",\"location\"]}";

        AbstractAnalyticsEvent analyticsEvent = objectMapper().readValue(json, SearchAnalyticsEvent.class);

        assertThat(analyticsEvent.eventType()).isEqualTo("search");
        assertThat(analyticsEvent.userAgent()).isEqualTo("Mozilla");
        assertThat(analyticsEvent.timestamp()).isNull();
    }

    @Test
    void deserializeSucceedsOnMissingOptionalField() throws JsonProcessingException {
        String json = "{\"sessionId\":\"01234\",\"userAgent\":\"Chrome\",\"eventType\":\"search_custom\",\"searchWords\":[\"name\",\"location\"]}";

        AbstractAnalyticsEvent analyticsEvent = objectMapper().readValue(json, SearchAnalyticsEvent.class);

        AbstractAnalyticsEvent expectedAnalyticsEvent = SearchAnalyticsEvent.builder()
                .sessionId("01234")
                .userAgent("Chrome")
                .eventType("search_custom")
                .searchWords(List.of("name", "location"))
                .build();

        assertThat(analyticsEvent).isEqualTo(expectedAnalyticsEvent);
    }

    @Test
    void deserializeFailsOnMissingRequiredFields() {
        String json = String.format("{\"userAgent\":\"Chrome\",\"timestamp\":\"%s\",\"eventType\":\"search_custom\",\"searchWords\":[\"name\",\"location\"]}",
                NOW);

        assertThatThrownBy(() -> objectMapper().readValue(json, SearchAnalyticsEvent.class))
                .isInstanceOf(ValueInstantiationException.class)
                .hasMessageContaining("sessionId is marked non-null but is null");
    }

    @Test
    void copyObjectSucceeds() {
        SearchAnalyticsEvent analyticsEvent = SearchAnalyticsEvent.builder()
                .sessionId("01234")
                .searchWords(List.of("name", "location"))
                .build();

        SearchAnalyticsEvent copiedAnalyticsEvent = analyticsEvent.toBuilder()
                .sessionId("9999")
                .build();

        assertThat(copiedAnalyticsEvent).isNotEqualTo(analyticsEvent);
        assertThat(copiedAnalyticsEvent.sessionId()).isEqualTo("9999");
        assertThat(copiedAnalyticsEvent.userAgent()).isEqualTo("Mozilla");
        assertThat(copiedAnalyticsEvent.timestamp()).isNull();
        assertThat(copiedAnalyticsEvent.eventType()).isEqualTo("search");
        assertThat(copiedAnalyticsEvent.searchWords()).containsExactly("name", "location");
    }
}
