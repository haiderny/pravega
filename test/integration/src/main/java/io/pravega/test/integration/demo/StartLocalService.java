/**
 * Copyright (c) 2017 Dell Inc., or its subsidiaries. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package io.pravega.test.integration.demo;

import io.pravega.segmentstore.contracts.StreamSegmentStore;
import io.pravega.segmentstore.server.host.handler.PravegaConnectionListener;
import io.pravega.segmentstore.server.store.ServiceBuilder;
import io.pravega.segmentstore.server.store.ServiceBuilderConfig;
import io.pravega.client.stream.mock.MockStreamManager;
import lombok.Cleanup;

public class StartLocalService {
    
    static final int PORT = 9090;
    static final String SCOPE = "Scope";
    static final String STREAM_NAME = "Foo";

    public static void main(String[] args) throws Exception {
        @Cleanup
        ServiceBuilder serviceBuilder = ServiceBuilder.newInMemoryBuilder(ServiceBuilderConfig.getDefaultConfig());
        serviceBuilder.initialize();
        StreamSegmentStore store = serviceBuilder.createStreamSegmentService();
        @Cleanup
        PravegaConnectionListener server = new PravegaConnectionListener(false, PORT, store);
        server.startListening();
        
        @Cleanup
        MockStreamManager streamManager = new MockStreamManager(SCOPE, "localhost", StartLocalService.PORT);
        streamManager.createScope(SCOPE);
        streamManager.createStream(SCOPE, STREAM_NAME, null);
        
        Thread.sleep(60000);
        System.exit(0);
    }
}
