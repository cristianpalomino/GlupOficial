package pe.com.glup.managers.bus;

import com.squareup.otto.Bus;

public final class BusHolder {

    private static final Bus BUS = new Bus();

    public static Bus getInstance() {
        return BUS;
    }

    private BusHolder() {
    }
}