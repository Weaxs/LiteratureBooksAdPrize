package org.weaxsey.remotecall.api;

import org.weaxsey.remotecall.domain.RemoteMsg;

public interface IRemoteCallService {

    String remoteCallByRequestGet(RemoteMsg remoteMsg);

    String remoteCallByRequestPost(RemoteMsg remoteMsg);

    String remoteCallByHttpClientPost(RemoteMsg remoteMsg);

    String remoteCallByHttpUrlConnectionPost(RemoteMsg remoteMsg);

}
