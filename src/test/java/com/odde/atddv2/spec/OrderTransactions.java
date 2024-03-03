package com.odde.atddv2.spec;

import com.github.leeonky.jfactory.Global;
import com.github.leeonky.jfactory.Spec;
import com.odde.atddv2.entity.OrderTransaction;

public class OrderTransactions {

    @Global
    public static class 订单支付信息 extends Spec<OrderTransaction> {
        @Override
        public void main() {
            property("order").is("未发货的", "订单");
        }
    }
}
