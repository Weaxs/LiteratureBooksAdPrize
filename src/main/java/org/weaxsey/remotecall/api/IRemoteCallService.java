package org.weaxsey.remotecall.api;

import org.weaxsey.remotecall.domain.RemoteMsg;

public interface IRemoteCallService {

    String remoteCallByRequestGET(RemoteMsg remoteMsg);

    String remoteCallByRequestPOST(RemoteMsg remoteMsg);

    String remoteCallByHttpClientPOST(RemoteMsg remoteMsg);

    String remoteCallByHttpURLConnectionPOST(RemoteMsg remoteMsg);

}
