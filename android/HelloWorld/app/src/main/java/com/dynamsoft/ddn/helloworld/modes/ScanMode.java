package com.dynamsoft.ddn.helloworld.modes;

import static com.dynamsoft.ddn.helloworld.modes.ScanMode.AUTO_SCAN_MODE;
import static com.dynamsoft.ddn.helloworld.modes.ScanMode.SCAN_EDIT_MODE;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({SCAN_EDIT_MODE, AUTO_SCAN_MODE})
@Retention(RetentionPolicy.CLASS)
public @interface ScanMode {
    int SCAN_EDIT_MODE = 0;
    int AUTO_SCAN_MODE = 1;
}
