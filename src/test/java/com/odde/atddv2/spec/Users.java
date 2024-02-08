package com.odde.atddv2.spec;

import com.github.leeonky.jfactory.Global;
import com.github.leeonky.jfactory.Spec;
import com.odde.atddv2.entity.User;

public class Users {
    @Global
    public static class 用户 extends Spec<User> {
        @Override
        public void main() {
            property("id").ignore();
        }
    }
}
