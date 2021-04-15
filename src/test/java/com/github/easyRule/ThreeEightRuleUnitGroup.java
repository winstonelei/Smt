package com.github.easyRule;
import org.jeasy.rules.annotation.*;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.support.UnitRuleGroup;

@Rule(name = "被3和8同时整除", description = "这是一个组合规则")
public class ThreeEightRuleUnitGroup extends UnitRuleGroup {

    public ThreeEightRuleUnitGroup(Object... rules) {
        for (Object rule : rules) {
            addRule(rule);
        }
    }

    @Override
    public void execute(Facts facts) throws Exception {
        //super.execute(facts);
        System.out.println("xxxx h"+facts.get("number"));
    }

    @Override
    public int getPriority() {
        return 0;
    }

}