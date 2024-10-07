package io.usnack.nocomment.notion.service.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.HashMap;
import java.util.Map;

public abstract class NotionData {
    private Map<String, Object> extraProps = new HashMap<>();

    @JsonAnySetter
    public void setData(String key, Object value) {
        extraProps.put(key, value);
    }

    @JsonAnyGetter
    public Map<String, Object> getExtraProps() {
        return extraProps;
    }
}
