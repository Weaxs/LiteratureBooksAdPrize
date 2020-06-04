package org.weaxsey.remotecall.api;

import org.weaxsey.remotecall.domain.RemoteMsg;

import java.util.Map;

public interface IRemoteCallService {

    String remoteCallByRequestPOST(RemoteMsg remoteMsg);

    String remoteCallByHttpClientPOST(RemoteMsg remoteMsg);

    String remoteCallByHttpURLConnectionPOST(RemoteMsg remoteMsg);

}
