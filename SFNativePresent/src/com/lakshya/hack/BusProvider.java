package com.lakshya.hack;

import com.squareup.otto.Bus;

/**
 * Created by govinda_jajoo on 24-Dec-16.
 */

public class BusProvider {
    private static final Bus bus = new Bus();

    public static Bus getInstance()  {
        return bus;
    }

}