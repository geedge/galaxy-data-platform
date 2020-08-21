package com.mesalab.common.model.arango;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Mark Vollmary
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseDocument implements Serializable {

    private static final long serialVersionUID = -1824742667228719116L;

    @JsonProperty(value = "_id")
    protected String id;
    @JsonProperty(value = "_key")
    protected String key;
    @JsonProperty(value = "_rev")
    protected String revision;

}
