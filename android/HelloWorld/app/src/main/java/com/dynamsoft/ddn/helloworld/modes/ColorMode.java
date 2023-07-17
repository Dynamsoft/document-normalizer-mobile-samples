package com.dynamsoft.ddn.helloworld.modes;


import static com.dynamsoft.ddn.helloworld.modes.ColorMode.COLOR_MODE_BINARY;
import static com.dynamsoft.ddn.helloworld.modes.ColorMode.COLOR_MODE_COLOUR;
import static com.dynamsoft.ddn.helloworld.modes.ColorMode.COLOR_MODE_GRAYSCALE;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({COLOR_MODE_BINARY, COLOR_MODE_COLOUR, COLOR_MODE_GRAYSCALE})
@Retention(RetentionPolicy.CLASS)
public @interface ColorMode {
    int COLOR_MODE_BINARY = 0;
    int COLOR_MODE_COLOUR = 1;
    int COLOR_MODE_GRAYSCALE = 2;
}
