package org.weaxsey.remotecall.api;

import org.weaxsey.remotecall.domain.RequestParam;

/**
 * remote call
 *
 * @author Weaxs
 */
public interface IRemoteCallService {

    /**
     * remote call rest by get
     * @param remoteMsg call param
     * @return response
     */
    String remoteCallByRequestGet(RequestParam remoteMsg);

    /**
     * remote call rest by post
     * @param remoteMsg call param
     * @return response
     */
    String remoteCallByRequestPost(RequestParam remoteMsg);

    /**
     * remote call rest by HttpClientPost
     * @param remoteMsg call param
     * @return response
     */
    String remoteCallByHttpClientPost(RequestParam remoteMsg);

    /**
     * remote call rest by post
     * @param remoteMsg call param
     * @return response
     */
    String remoteCallByHttpUrlConnectionPost(RequestParam remoteMsg);

}
