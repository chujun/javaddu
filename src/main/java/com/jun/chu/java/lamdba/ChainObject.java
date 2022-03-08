package com.jun.chu.java.lamdba;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author chujun
 * @date 2022/3/8
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChainObject {
    private static ChainObject CHAIN_OBJECT = new ChainObject();

    public static ChainObject get() {
        return CHAIN_OBJECT;
    }

    public ChainObject eq(boolean expression, Execute supplier) {
        if (expression) {
            supplier.execute();
        }
        return this;
    }
}
