package save3;


import com.fasterxml.jackson.databind.ObjectMapper;

public interface JsonPreparable {
    void prepareForSave(ObjectMapper mapper);

    void unpackAfterLoad(ObjectMapper mapper);
}


