package com.streamlined.springhibernatetask.validator;

import com.streamlined.springhibernatetask.entity.EntityType;

public interface Validator<T extends EntityType<?>> {

    boolean isValid(T entity);

}
