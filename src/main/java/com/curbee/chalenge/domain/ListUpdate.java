package com.curbee.chalenge.domain;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public final class ListUpdate implements ChangeType {
    private final String property;
    private final String[] added;
    private final String[] removed;

    public ListUpdate(String property, String[] added, String[] removed) {
        this.property = property;
        this.added = added;
        this.removed = removed;
    }

    public static List<ChangeType> workPropertyUpdate(List<Object> previous, List<Object> current) {

        List<ChangeType> changes = new ArrayList<>();
        if(previous.size() > current.size()) {
            String[] removeditems = new String[previous.size() - current.size()];
            String name = previous.get(0).getClass().getSimpleName();
            int i = 0;
            for (Object o : previous) {
                if(!doesObjectContainId(o)){
                    throw new RuntimeException("Objects must have id field");
                }
                if (!current.contains(o)) {
                    removeditems[i] = concatenate(o);
                    i++;
                }
            }
            changes.add(new ListUpdate(name, null, removeditems));

        }else if(previous.size() < current.size()) {
            String[] addeditems = new String[current.size() - previous.size()];
            String name = current.get(0).getClass().getSimpleName();
            int i = 0;
            for (Object o : current) {
                if(!doesObjectContainId(o)){
                    throw new RuntimeException("Objects must have id field");
                }
                if (!previous.contains(o)) {
                    addeditems[i] = concatenate(o);
                    i++;
                }
            }
            changes.add(new ListUpdate(name, addeditems, null));
        }else if(previous.size() == current.size()) {
            String name = current.get(0).getClass().getSimpleName();

            for(int i = 0; i < previous.size(); i++) {
                if (!previous.get(i).equals(current.get(i))) {
                    if(!doesObjectContainId(previous.get(i)) || !doesObjectContainId(current.get(i))){
                        throw new RuntimeException("Objects must have id field");
                    }
                    try {
                        Field field = current.get(i).getClass().getDeclaredField("id");
                        field.setAccessible(true);
                        Object value = field.get(current.get(i));
                        name = name + "[" + value + "]";
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    changes.addAll(PropertyUpdate.workPropertyUpdate(name, previous.get(i), current.get(i)));
                }
            }

        }

        return changes;
    }

    public static boolean doesObjectContainId(Object object) {
        return Arrays.stream(object.getClass().getFields())
                .anyMatch(f -> f.getName().equals("id"));
    }

    public static String concatenate(Object object) {
        return Arrays.stream(object.getClass().getDeclaredFields())
                .filter(f -> f.getType() == String.class)
                .sorted(Comparator.comparing(Field::getName))
                .map(f -> {
                    try { return (String)f.get(object); }
                    catch (IllegalAccessException e) { return null; }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.joining(", "));
    }

    public String getProperty() {
        return property;
    }

    public String[] getAdded() {
        return added;
    }

    public String[] getRemoved() {
        return removed;
    }

}
