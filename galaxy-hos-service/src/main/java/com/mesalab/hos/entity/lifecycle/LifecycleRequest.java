package com.mesalab.hos.entity.lifecycle;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "LifecycleConfiguration")
public class LifecycleRequest {
    private RuleObj ruleObj;

    public LifecycleRequest() {
    }

    @XmlElement(name = "Rule")
    public RuleObj getRuleObj() {
        return ruleObj;
    }

    public void setRuleObj(RuleObj ruleObj) {
        this.ruleObj = ruleObj;
    }

    @Override
    public String toString() {
        return "LifecycleRequest{" +
                "ruleObj=" + ruleObj +
                '}';
    }
}
