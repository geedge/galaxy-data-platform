package com.mesalab.common.model.arango;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author Mark Vollmary
 */
@Data
public class BaseEdgeDocument {

    private static final long serialVersionUID = 6904923804449368783L;

    @JsonProperty(value = "_from")
    private String from;

    @JsonProperty(value = "_to")
    private String to;

}
