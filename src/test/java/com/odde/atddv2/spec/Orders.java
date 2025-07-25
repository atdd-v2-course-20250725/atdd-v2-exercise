package com.odde.atddv2.spec;

import com.github.leeonky.jfactory.Spec;
import com.github.leeonky.jfactory.Trait;
import com.odde.atddv2.entity.Order;

import static com.odde.atddv2.entity.Order.OrderStatus.delivering;
import static com.odde.atddv2.entity.Order.OrderStatus.toBeDelivered;

public class Orders {
    public static class 订单 extends Spec<Order> {
        @Override
        public void main() {
            property("id").ignore();
        }

        @Trait
        public void 未发货的() {
            property("status").value(toBeDelivered);
            property("deliverNo").value(null);
            property("deliveredAt").value(null);
        }

        @Trait
        public void 已发货的() {
            property("status").value(delivering);
        }
    }

    public static class 物流订单 extends 订单 {

        @Override
        public void main() {
            property("deliverNo").ignore();
            super.main();
        }

        @Trait
        @Override
        public void 已发货的() {
            property("express").is(Expresses.快递信息.class);
            super.已发货的();
        }
    }
}
