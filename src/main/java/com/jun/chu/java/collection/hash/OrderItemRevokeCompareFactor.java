package com.jun.chu.java.collection.hash;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author jun.chu
 * @date 2019-01-08 22:02
 */
@Data
@EqualsAndHashCode
public class OrderItemRevokeCompareFactor {
    public OrderItemRevokeCompareFactor() {
    }

    public OrderItemRevokeCompareFactor(Integer skuId, Integer quantity) {
        this.skuId = skuId;
        this.quantity = quantity;
    }

    private Integer skuId;

    private Integer quantity;
}
