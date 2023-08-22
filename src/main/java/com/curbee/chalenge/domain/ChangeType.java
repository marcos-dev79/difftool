package com.curbee.chalenge.domain;

import java.util.List;

public sealed interface ChangeType permits PropertyUpdate, ListUpdate {
}
