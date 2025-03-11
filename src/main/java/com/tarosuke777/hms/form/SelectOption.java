package com.tarosuke777.hms.form;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SelectOption {
    private final String key;
    private final String value;
    private final String parentKey;	
}
