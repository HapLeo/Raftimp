package com.wuyulin.raftimp.controller;

import com.wuyulin.raftimp.config.RaftConfig;
import com.wuyulin.raftimp.model.Leader;
import com.wuyulin.raftimp.model.Role;
import com.wuyulin.raftimp.model.VoteRequest;
import com.wuyulin.raftimp.model.VoteResponse;
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
    public VoteResponse checkVoteTicket(VoteRequest request) {

        VoteResponse voteResponse = new VoteResponse();
        voteResponse.setFromUrl("http:localhost:" + RaftConfig.port + "/vote/checkVoteTicket");
        voteResponse.setTerm(Role.term.get());

        Integer term = request.getTerm();
        if (term != null && term > Role.term.get()) {
            // 设置term和新的leader_url
            Role.term.set(term);
            Leader.Leader_url = Leader.Leader_url;
            voteResponse.setCode(200);
            voteResponse.setMsg("接受此选举！");
            return voteResponse;
        }
        voteResponse.setCode(400);
        voteResponse.setMsg("拒绝此选举！");
        return voteResponse;
    }
}
