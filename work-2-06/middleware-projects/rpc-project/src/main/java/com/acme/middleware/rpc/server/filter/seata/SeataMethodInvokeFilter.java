package com.acme.middleware.rpc.server.filter.seata;

import com.acme.middleware.rpc.InvocationRequest;
import com.acme.middleware.rpc.annotation.RpcFilter;
import com.acme.middleware.rpc.server.filter.MethodInvokeFilter;
import io.seata.core.context.RootContext;
import io.seata.core.model.BranchType;

import java.util.Map;
import java.util.Objects;

@RpcFilter(name = "seata")
public class SeataMethodInvokeFilter implements MethodInvokeFilter {
    private ThreadLocal<Boolean> bind = ThreadLocal.withInitial(() -> false);
    private ThreadLocal<String> formXid = ThreadLocal.withInitial(() -> "");

    @Override
    public void beforeServiceMethodInvoke(InvocationRequest request) {
        Map<String, Object> metadata = request.getMetadata();
        String xid = (String) metadata.get("seata." + RootContext.KEY_XID);
        String branchType = (String) metadata.get("seata." + RootContext.KEY_BRANCH_TYPE);

        if (xid != null) {
            formXid.set(xid);
            RootContext.bind(xid);
            if (Objects.equals(BranchType.TCC.name(), branchType)) {
                RootContext.bindBranchType(BranchType.TCC);
            }
            bind.set(true);
        }
    }

    @Override
    public void afterServiceMethodInvoke(Object result) {
        if (bind.get()) {
            BranchType previousBranchType = RootContext.getBranchType();
            String unbindXid = RootContext.unbind();
            if (BranchType.TCC == previousBranchType) {
                RootContext.unbindBranchType();
            }
            if (!formXid.get().equalsIgnoreCase(unbindXid)) {
                if (unbindXid != null) {
                    RootContext.bind(unbindXid);
                    if (BranchType.TCC == previousBranchType) {
                        RootContext.bindBranchType(BranchType.TCC);
                    }
                }
            }
        }

        bind.remove();
        formXid.remove();
    }
}
