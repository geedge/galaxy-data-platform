package com.mesalab.tool.domain;/**
 * Created by dell on 2018-10-8.
 */

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.InjectableValues;
import com.maxmind.geoip2.record.Traits;

import java.util.List;

/**
 * @ClassName JsonInjector
 * @Description TODO
 * @Author dell
 * @Date 2018-10-8 18:38
 * @Version 1.0
 **/
public class JsonInjector extends InjectableValues {

    private final List<String> locales;
    private final String ip;

    public JsonInjector(List<String> locales, String ip) {
        this.locales = locales;
        this.ip = ip;
    }

    @Override
    public Object findInjectableValue(Object valueId, DeserializationContext ctxt,
                                      BeanProperty forProperty, Object beanInstance) {
        if ("locales".equals(valueId)) {
            return locales;
        }
        if ("ip_address".equals(valueId)) {
            return ip;
        }
        if ("traits".equals(valueId)) {
            return new Traits(ip);
        }
        return null;
    }

}
