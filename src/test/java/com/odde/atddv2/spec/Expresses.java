package com.odde.atddv2.spec;

import com.github.leeonky.jfactory.Spec;
import com.odde.atddv2.entity.mongo.Express;
import com.odde.atddv2.entity.mongo.ExpressStatus;

public class Expresses {
    public static class 快递信息 extends Spec<Express> {
        @Override
        public void main() {
            property("list[]").is(快递状态.class);
        }
    }

    public static class 快递状态 extends Spec<ExpressStatus> {
    }
}
