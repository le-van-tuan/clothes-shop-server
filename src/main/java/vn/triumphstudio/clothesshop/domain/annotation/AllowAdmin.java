package vn.triumphstudio.clothesshop.domain.annotation;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.*;


@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@PreAuthorize("hasRole('ADMIN')")
public @interface AllowAdmin {
}
