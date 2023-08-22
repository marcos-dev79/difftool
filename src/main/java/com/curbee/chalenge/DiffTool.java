package com.curbee.chalenge;


import com.curbee.chalenge.domain.ChangeType;
import com.curbee.chalenge.domain.ListUpdate;
import com.curbee.chalenge.domain.PropertyUpdate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DiffTool {

    public List<ChangeType> diff(Object previous, Object current) {
        List<ChangeType> changes = new ArrayList<>();
        if(!(previous instanceof Collection<?>) && !(current instanceof Collection<?>)) {
            if (!previous.equals(current)) {
                changes.addAll(PropertyUpdate.workPropertyUpdate("", previous, current));
            }
        }else {
            if (previous != null && current != null) {
                if (!previous.equals(current)) {
                    changes.addAll(ListUpdate.workPropertyUpdate((List) previous, (List) current));
                }
            } else {
                throw new RuntimeException("Objects cannot be null");
            }
        }

        return changes;
    }

}
