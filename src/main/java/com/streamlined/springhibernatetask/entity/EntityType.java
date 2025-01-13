package com.streamlined.springhibernatetask.entity;

public interface EntityType<T> {

    T getPrimaryKey();
    
    boolean isIdenticalTo(EntityType<T> entity);

}
