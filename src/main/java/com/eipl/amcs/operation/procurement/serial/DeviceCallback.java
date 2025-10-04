package com.eipl.amcs.operation.procurement.serial;

import java.util.Map;

public interface DeviceCallback {
    void onResponseFromDevice(Map<String, String> resp, String tag);
}
