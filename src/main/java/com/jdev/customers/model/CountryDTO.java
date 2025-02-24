package com.jdev.customers.model;

import java.util.Map;

public record CountryDTO(Name name, String cca2, Demonyms demonyms) {

    public record Name(String common, String official) {}

    public record Demonyms(Map<String, String> eng, Map<String, String> fra) {}
}
