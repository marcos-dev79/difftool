package com.curbee.chalenge.domain;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public final class PropertyUpdate implements ChangeType {
    private final String property;
    private final String previous;
    private final String current;

    public PropertyUpdate(String property, String previous, String current) {
        this.property = property;
        this.previous = previous;
        this.current = current;
    }

    public static List<ChangeType> workPropertyUpdate(String property, Object previous, Object current) {
        List<ChangeType> changes = new ArrayList<>();
        Field[] previousfields = previous.getClass().getDeclaredFields();
        Field[] currentfields = current.getClass().getDeclaredFields();

        for (Field previousfield : previousfields) {
            for (Field currentfield : currentfields) {
                if (previousfield.getName().equals(currentfield.getName())) {
                    try {
                        previousfield.setAccessible(true);
                        currentfield.setAccessible(true);

                        if (previousfield.get(previous) != null && currentfield.get(current) != null) {
                            if (!previousfield.get(previous).equals(currentfield.get(current))) {
                                String propertyname = (property != "") ? property + "." + previousfield.getName() : previousfield.getName();
                                changes.add(new PropertyUpdate(propertyname, previousfield.get(previous).toString(), currentfield.get(current).toString()));
                            }
                        } else {
                            throw new RuntimeException("Objects cannot be null");
                        }


                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return changes;
    }

    public String getProperty() {
        return property;
    }

    public String getPrevious() {
        return previous;
    }

    public String getCurrent() {
        return current;
    }
}
