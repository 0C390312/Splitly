package com.splitly.core.utils;

import static java.nio.file.Files.readString;
import static java.util.Objects.requireNonNull;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.file.Path;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TestUtils {

  public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
    .setSerializationInclusion(Include.NON_NULL)
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

  @SneakyThrows
  public static String readStringFromPath(String path) {
    var sourceFilePath = TestUtils.class.getClassLoader().getResource(path);
    return readString(Path.of(requireNonNull(sourceFilePath).toURI()));
  }

  @SneakyThrows
  public static String minify(String json) {
    return OBJECT_MAPPER.readTree(json).toString();
  }

  @SneakyThrows
  public static String asJson(String path) {
    return minify(readStringFromPath(path));
  }

  @SneakyThrows
  public static String asJson(Object object) {
    return OBJECT_MAPPER.writeValueAsString(object);
  }

  @SneakyThrows
  public static <T> T asObject(String result, Class<T> type) {
    return OBJECT_MAPPER.readValue(result, type);
  }

  @SneakyThrows
  public static <T> T asObject(String result, TypeReference<T> valueTypeRef) {
    return OBJECT_MAPPER.readValue(result, valueTypeRef);
  }
}
