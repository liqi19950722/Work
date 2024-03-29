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

import com.acme.middleware.rpc.InvocationResponse;
import com.acme.middleware.rpc.client.ExchangeFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import static com.acme.middleware.rpc.client.ExchangeFuture.removeExchangeFuture;

/**
 * {@link InvocationResponse}
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
public class InvocationResponseHandler extends SimpleChannelInboundHandler<InvocationResponse> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, InvocationResponse response) throws Exception {
        // 当 RPC Server 成功响应时，requestId 对象 Promise(Future) 设置响应结果，并标记处理成功
        String requestId = response.getRequestId();
        ExchangeFuture exchangeFuture = removeExchangeFuture(requestId);
        if (exchangeFuture != null) {
            Object result = response.getEntity();
            exchangeFuture.getPromise().setSuccess(result);
        }
    }
}
