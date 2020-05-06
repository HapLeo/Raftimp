# Raftimp
基于Raft算法的KV系统的Java实现示例

根据角色划分业务功能，可分为Follower-跟随者，Candidate-候选人，Leader-领导者。
- Follower的业务功能: 启动时默认此角色。此角色需要监听Leader心跳和投票选举。
- Candidate的业务功能：Follower监听Leader心跳超时后转换成Candidate身份并向其他节点发送选举请求，
等待其他节点的回应后，统计是否通过选举（收到一半以上的回应），通过则转换成Leader,否则继续作为Follower监听
心跳并同步其他节点的日志。
- Leader的业务功能：接收外部的读写请求，并将数据发送到Follower节点；持续发送Leader心跳，续约Leader状态。
 