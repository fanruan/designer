package com.fr.design.formula;

public interface NameAndDescription {

    String getName();

    String getDesc();

    String searchResult(String keyWord, boolean findDescription);
}