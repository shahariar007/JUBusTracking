package com.hossain.ju.bus.location;

import java.util.HashMap;
import java.util.List;

/**
 * Created by mohammod.hossain on 7/10/2018.
 */

public interface LocationListener {

    public void onFail();

    public void onSuccessfullRouteFetch( List<List<HashMap<String, String>>> result);

}
