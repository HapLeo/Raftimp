package com.wuyulin.raftimp.controller;

import com.wuyulin.raftimp.config.NodeStatus;
import com.wuyulin.raftimp.model.Follower;
import com.wuyulin.raftimp.model.Role;
import com.wuyulin.raftimp.model.VoteRequest;
import com.wuyulin.raftimp.model.VoteResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vote")
public class VoteController {

    /**
     * 检查选票，符合则返回投票成功
     *
     * @param request
     * @return
     */
    @RequestMapping("/checkVoteTicket")
    @ResponseBody
    public VoteResponse checkVoteTicket(@RequestBody VoteRequest request) {

        VoteResponse voteResponse = new VoteResponse();
        voteResponse.setFromUrl(NodeStatus.localUrl + "/vote/checkVoteTicket");
        voteResponse.setTerm(NodeStatus.term.get());

        Integer term = request.getTerm();
        if (term == null) {
            voteResponse.setMsg("选票未携带任期！");
            voteResponse.setCode(500);
            return voteResponse;
        }
        if (term > NodeStatus.term.get()) {
            // 设置term和新的leader_url
            NodeStatus.term.set(term);
            NodeStatus.leaderNode = request.getFromUrl();
            voteResponse.setCode(200);
            voteResponse.setMsg("接受此选举！");
            // 身份转变成Follower
            Role role = new Follower();
            role.run();

            return voteResponse;
        }
        voteResponse.setCode(400);
        voteResponse.setMsg("拒绝此选举！选票的任期为" + term + ",当前节点的任期为" + NodeStatus.term.get());
        return voteResponse;
    }
}
