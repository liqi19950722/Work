package com.acme.middleware.rpc.client.filter.seata;

import com.acme.middleware.rpc.InvocationRequest;
import com.acme.middleware.rpc.annotation.RpcFilter;
import com.acme.middleware.rpc.client.filter.ExecuteFilter;
import io.seata.core.context.RootContext;
import io.seata.core.model.BranchType;

import java.util.Map;

@RpcFilter(name = "seata")
public class SeataRequestProcessor implements ExecuteFilter {
    @Override
    public void processInvocationRequestBeforeExecute(InvocationRequest request) {
        Map<String, Object> metadata = request.getMetadata();

        String xid = RootContext.getXID();
        BranchType branchType = RootContext.getBranchType();
        if (xid != null && branchType != null) {
            metadata.put("seata." + RootContext.KEY_XID, xid);
            metadata.put("seata." + RootContext.KEY_BRANCH_TYPE, branchType.name());
        }
    }
}
