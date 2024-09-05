package com.jinwuui.howdoilook.config;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = CustomMockSecurityContext.class)
public @interface CustomMockUser {

    String email() default "jinwuui@gmail.com";

    String password() default "1234";

    String nickname() default "홍길동";

}
