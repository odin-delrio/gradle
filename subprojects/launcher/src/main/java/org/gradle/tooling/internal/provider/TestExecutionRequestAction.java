/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.tooling.internal.provider;

import org.gradle.StartParameter;
import org.gradle.tooling.internal.protocol.test.InternalJvmTestExecutionDescriptor;
import org.gradle.tooling.internal.protocol.test.InternalTestExecutionRequest;

import java.util.Collection;

public class TestExecutionRequestAction extends SubscribableBuildAction {
    private final StartParameter startParameter;
    private InternalTestExecutionRequest testExecutionRequest;

    public TestExecutionRequestAction(InternalTestExecutionRequest testExecutionRequest, StartParameter startParameter, BuildClientSubscriptions clientSubscriptions) {
        super(clientSubscriptions);
        this.testExecutionRequest = testExecutionRequest;
        this.startParameter = startParameter;
    }

    @Override
    public StartParameter getStartParameter() {
        return startParameter;
    }

    public Collection<InternalJvmTestExecutionDescriptor> getTestExecutionDescriptors() {
        return testExecutionRequest.getTestExecutionDescriptors();
    }
}
