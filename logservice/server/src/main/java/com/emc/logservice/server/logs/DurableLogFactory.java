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

package com.emc.logservice.server.logs;

import com.emc.logservice.server.Cache;
import com.emc.logservice.server.OperationLogFactory;
import com.emc.logservice.server.UpdateableContainerMetadata;
import com.emc.logservice.storageabstraction.DurableDataLogFactory;
import com.google.common.base.Preconditions;

import java.util.concurrent.Executor;

/**
 * Default Factory for DurableLogs.
 */
public class DurableLogFactory implements OperationLogFactory {
    private final DurableDataLogFactory dataLogFactory;
    private final Executor executor;

    public DurableLogFactory(DurableDataLogFactory dataLogFactory, Executor executor) {
        Preconditions.checkNotNull(dataLogFactory, "dataLogFactory");
        Preconditions.checkNotNull(executor, "executor");
        this.dataLogFactory = dataLogFactory;
        this.executor = executor;
    }

    @Override
    public OperationLog createDurableLog(UpdateableContainerMetadata containerMetadata, Cache cache) {
        return new DurableLog(containerMetadata, this.dataLogFactory, cache, this.executor);
    }
}
