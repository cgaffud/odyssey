package com.bedmen.odyssey.modifier;

public class EnvironmentConditionalMeleeModifier extends FloatModifier {
    public final AttackBoostFactorFunction attackBoostFactorFunction;

    protected EnvironmentConditionalMeleeModifier(String id, AttackBoostFactorFunction attackBoostFactorFunction) {
        super(id);
        this.attackBoostFactorFunction = attackBoostFactorFunction;
    }
}
