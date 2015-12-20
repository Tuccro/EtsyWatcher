package com.tuccro.etsywatcher.async;

import java.util.List;

/**
 * Created by tuccro on 12/20/15.
 */
public interface IGetRequestResult {

    void onGetRequestResult(List<String> result);

    void onGetRequestError(List<String> result);
}
