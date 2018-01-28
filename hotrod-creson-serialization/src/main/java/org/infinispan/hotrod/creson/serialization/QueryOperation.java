package org.infinispan.hotrod.creson.serialization;

import org.infinispan.client.creson.serialization.CresonRequest;
import org.infinispan.client.creson.serialization.CresonResponse;
import org.infinispan.client.creson.serialization.Marshalling;
import org.infinispan.client.hotrod.configuration.ClientIntelligence;
import org.infinispan.client.hotrod.impl.operations.RetryOnFailureOperation;
import org.infinispan.client.hotrod.impl.protocol.Codec;
import org.infinispan.client.hotrod.impl.protocol.HeaderParams;
import org.infinispan.client.hotrod.impl.protocol.HotRodConstants;
import org.infinispan.client.hotrod.impl.transport.Transport;
import org.infinispan.client.hotrod.impl.transport.TransportFactory;
import org.infinispan.client.hotrod.impl.transport.tcp.TcpTransportFactory;


import java.net.SocketAddress;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class QueryOperation extends RetryOnFailureOperation<CresonResponse> {

    private final RemoteQuery remoteQuery;

    public QueryOperation(Codec codec, TransportFactory transportFactory, byte[] cacheName, AtomicInteger topologyId,
                          int flags, ClientIntelligence clientIntelligence, RemoteQuery remoteQuery) {
        super(codec, transportFactory, cacheName, topologyId, flags, clientIntelligence);
        this.remoteQuery = remoteQuery;
    }

    @Override
    protected Transport getTransport(int i, Set<SocketAddress> failedServers) {
        if (!(transportFactory instanceof TcpTransportFactory)) {
            return transportFactory.getTransport(failedServers, this.cacheName);
        }

        Collection<SocketAddress> servvers = ((TcpTransportFactory)transportFactory).getServers();

        return transportFactory.getAddressTransport(servvers.iterator().next());
  }

    @Override
    protected CresonResponse executeOperation(Transport transport) {
        HeaderParams params = writeHeader(transport, HotRodConstants.QUERY_REQUEST);
        CresonRequest request = new CresonRequest();
        request.setMaxResults(remoteQuery.maxResults);
        request.setQueryString(remoteQuery.jpqlString);
        request.setStartOffset(remoteQuery.startOffset);

        transport.writeArray(Marshalling.marshall(request));
        transport.flush();
        readHeaderAndValidate(transport, params);
        byte[] responseBytes = transport.readArray();
        return (CresonResponse) Marshalling.unmarshall(responseBytes);
    }
}
