package com.tarosuke777.hms.form;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SelectOptionTrainingMenu {
    private final String key;
    private final String value;
    private final Integer parentKey;
    private final Integer maxWeight;
    private final Integer maxReps;
    private final Integer maxSets;
}
