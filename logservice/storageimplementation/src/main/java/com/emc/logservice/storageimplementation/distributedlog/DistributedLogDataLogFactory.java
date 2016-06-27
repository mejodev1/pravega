/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.emc.logservice.storageimplementation.distributedlog;

import com.emc.logservice.storageabstraction.DurableDataLog;
import com.emc.logservice.storageabstraction.DurableDataLogException;
import com.emc.logservice.storageabstraction.DurableDataLogFactory;

/**
 * Represents a DurableDataLogFactory that creates and manages instances of DistributedLogDataLog instances.
 */
public class DistributedLogDataLogFactory implements DurableDataLogFactory {
    private final DistributedLogConfig config;
    private final LogClient client;

    /**
     * Creates a new instance of the DistributedLogDataLogFactory class.
     *
     * @param clientId The Id of the client to set for the DistributedLog client.
     * @param config   DistributedLog configuration.
     * @throws NullPointerException     If any of the arguments are null.
     * @throws IllegalArgumentException If the clientId is invalid.
     */
    public DistributedLogDataLogFactory(String clientId, DistributedLogConfig config) {
        this.config = config;
        this.client = new LogClient(clientId, config);
    }

    /**
     * Initializes this instance of the DistributedLogDataLogFactory.
     *
     * @throws IllegalStateException   If the DistributedLogDataLogFactory is already initialized.
     * @throws DurableDataLogException If an exception is thrown during initialization. The actual exception thrown may
     *                                 be a derived exception from this one, which provides more information about
     *                                 the failure reason.
     */
    public void initialize() throws DurableDataLogException {
        this.client.initialize();
    }

    //region DurableDataLogFactory Implementation

    @Override
    public DurableDataLog createDurableDataLog(String containerId) {
        String logName = ContainerToLogNameConverter.getLogName(containerId);
        return new DistributedLogDataLog(logName, this.client);
    }

    @Override
    public void close() {
        this.client.close();
    }

    //endregion
}
