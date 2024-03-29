/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.acme.middleware.rpc.transport;

import com.acme.middleware.rpc.InvocationRequest;
import com.acme.middleware.rpc.InvocationResponse;
import com.acme.middleware.rpc.context.ServiceContext;
import com.acme.middleware.rpc.server.RpcServer;
import com.acme.middleware.rpc.server.filter.MethodInvokeFilter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * {@link InvocationRequest} 处理器
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
public class InvocationRequestHandler extends SimpleChannelInboundHandler<InvocationRequest> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ServiceContext serviceContext;
    private final List<MethodInvokeFilter> filters;

    public InvocationRequestHandler(ServiceContext serviceContext, RpcServer rpcServer) {
        this.serviceContext = serviceContext;
        this.filters = Collections.unmodifiableList(rpcServer.getMethodInvokeFilters());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, InvocationRequest request) throws Exception {

        String serviceName = request.getServiceName();
        String methodName = request.getMethodName();
        Object[] parameters = request.getParameters();
        Class[] parameterTypes = request.getParameterTypes();

        beforeServiceMethodInvoke(request);

        Object service = serviceContext.getService(serviceName);
        Object entity = null;
        String errorMessage = null;
        try {
            entity = MethodUtils.invokeMethod(service, methodName, parameters, parameterTypes);
        } catch (Exception e) {
            errorMessage = e.getMessage();
        } finally {
            afterServiceMethodInvoke(entity);
        }

        logger.info("Read {} and invoke the {}'s method[name:{}, param-types:{}, params:{}] : {}",
                request, serviceName, methodName, Arrays.asList(parameterTypes), Arrays.asList(parameters), entity);

        InvocationResponse response = new InvocationResponse();
        response.setRequestId(request.getRequestId());
        response.setEntity(entity);
        response.setErrorMessage(errorMessage);

        ctx.writeAndFlush(response);

        logger.info("Write and Flush {}", response);
    }

    private void afterServiceMethodInvoke(Object result) {
        this.filters.forEach(filter -> filter.afterServiceMethodInvoke(result));
    }

    private void beforeServiceMethodInvoke(InvocationRequest request) {
        this.filters.forEach(filter -> filter.beforeServiceMethodInvoke(request));
    }
}
