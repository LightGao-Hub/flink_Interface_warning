package com.shenque.warning.entity;

import java.io.Serializable;

/**
 * @author gl
 * @create 2020-04-20 22:44
 */
public class Demo implements Serializable {

    private String id;
    private Long value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "JavaDemo{" +
                "id='" + id + '\'' +
                ", value=" + value +
                '}';
    }
}

