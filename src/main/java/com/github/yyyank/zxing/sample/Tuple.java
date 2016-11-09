package com.github.yyyank.zxing.sample;

/**
 * Created by yy_yank on 2016/11/09.
 */

import java.nio.file.Path;

/**
 * tuple
 */
class Tuple {
    public Path key;
    public String value;

    Tuple(Path key, String value) {
        this.key = key;
        this.value = value;
    }
}