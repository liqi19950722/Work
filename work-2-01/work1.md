# 使用 SOFAJRAFT 实现状态机

1. （必须）补充官方 exmaple ElectionBootstrap 增加 Follower 节点日志信息，参考 Leader 事件实现

    ```java
    //新增接口
    public interface FollowerStateListener {

        void onFollowerStart(LeaderChangeContext leaderChangeContext);

        void onFollowerStop(LeaderChangeContext leaderChangeContext);
    }
    // com.alipay.sofa.jraft.example.election.ElectionOnlyStateMachine 重写`onStopFollowing`和`onStartFollowing`方法
    class ElectionOnlyStateMachine {
        private final List<FollowerStateListener> followerListeners;

        @Override
        public void onStopFollowing(LeaderChangeContext ctx) {
            super.onStopFollowing(ctx);
            followerListeners.forEach(listener -> {
                listener.onFollowerStop(ctx);
            });
        }

        @Override
        public void onStartFollowing(LeaderChangeContext ctx) {
            super.onStartFollowing(ctx);
            followerListeners.forEach(listener -> {
                listener.onFollowerStart(ctx);
            });
        }
    }

    // com.alipay.sofa.jraft.example.election.ElectionBootstrap增加监听器
    node.addFollowerStateListener(new FollowerStateListener() {
                @Override
                public void onFollowerStart(LeaderChangeContext leaderChangeContext) {
                    PeerId leader = leaderChangeContext.getLeaderId();
                    System.out.printf("[ElectionBootstrap]  %s 成为 Leader %s 的 Follower\n", serverIdStr, leader.getEndpoint());
                }

                @Override
                public void onFollowerStop(LeaderChangeContext leaderChangeContext) {
                    System.out.println("[ElectionBootstrap] Follower stop on term: " + leaderChangeContext.getTerm());
                }
            });
    ```


   1. 启动`ElectionBootstrap`
      - server1(Leader) log

        ```
        [ElectionBootstrap] Leader's ip is: 127.0.0.1, port: 8081
        [ElectionBootstrap] Leader start on term: 21
        ```
      - server2 log
        
        ```
        [ElectionBootstrap]  127.0.0.1:8082 成为 Leader 127.0.0.1:8081 的 Follower
        ```
      - server3 log
       
        ```
        [ElectionBootstrap]  127.0.0.1:8083 成为 Leader 127.0.0.1:8081 的 Follower
        ```
             
   2.  重启server1
       - server1 log

         ```
         [ElectionBootstrap]  127.0.0.1:8081 成为 Leader 127.0.0.1:8082 的 Follower
         ```
       - server2(Leader) log 
         
         ```
         [ElectionBootstrap] Follower stop on term: 21
         [ElectionBootstrap] Leader's ip is: 127.0.0.1, port: 8082
         [ElectionBootstrap] Leader start on term: 22
         ```  

       - server3 log
         
         ```
         [ElectionBootstrap] Follower stop on term: 21
         [ElectionBootstrap]  127.0.0.1:8083 成为 Leader 127.0.0.1:8082 的 Follower
         ```


2. （可选）补充官方 exmaple counter 增加 CounterOperation 的操作符，比如 DECREMENT，实现 decrementAndGet 方法，类似于 Atomic 类
   -  protobuf 版本3.5.1
   -  已实现减法 -1
  