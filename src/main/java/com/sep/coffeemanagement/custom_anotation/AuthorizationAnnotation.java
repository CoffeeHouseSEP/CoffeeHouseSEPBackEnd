package com.sep.coffeemanagement.custom_anotation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;

import com.sep.coffeemanagement.constant.Constant;
import com.sep.coffeemanagement.controller.AbstractController;
import com.sep.coffeemanagement.repository.internal_user.UserRepository;
import java.lang.annotation.*;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = { AuthorizationAnnotation.AuthorizationHandler.class })
public @interface AuthorizationAnnotation {
  String message() default "Inaccessible!!";

  String[] ROLES() default {
    Constant.ADMIN_ROLE, Constant.BRANCH_ROLE, Constant.USER_ROLE
  };

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  class AuthorizationHandler
    extends AbstractController
    implements ConstraintValidator<AuthorizationAnnotation, String> {
    @Autowired
    private UserRepository userRepository;

    private String[] roles;

    @Override
    public void initialize(AuthorizationAnnotation constraintAnnotation) {
      this.roles = constraintAnnotation.ROLES();
    }

    @Override
    public boolean isValid(String request, ConstraintValidatorContext context) {
      System.out.println(request); //        String id = checkAuthentication(request);
      //        InternalUser user = userRepository.getOneByAttribute("internalUserId",id).orElseThrow(()->new ResourceNotFoundException("user not found"));
      //        Boolean b = Arrays.asList(roles).contains(user.getRole());
      //            return b;
      return StringUtils.containsWhitespace(request);
    }
  }
}
