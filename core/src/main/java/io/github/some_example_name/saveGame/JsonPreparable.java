package io.github.some_example_name.saveGame;


import com.fasterxml.jackson.databind.ObjectMapper;

public interface JsonPreparable {
    void prepareForSave(ObjectMapper mapper);

    void unpackAfterLoad(ObjectMapper mapper);
}


