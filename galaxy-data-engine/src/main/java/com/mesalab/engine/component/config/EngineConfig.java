package com.mesalab.engine.component.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.Serializable;

@Data
@Component
public class EngineConfig implements Serializable {

    @Value("${engine.maxCacheNum}")
    private int maxCacheNum;
    @Value("${engine.defaultResultNum}")
    private int defaultResultNum;

}
