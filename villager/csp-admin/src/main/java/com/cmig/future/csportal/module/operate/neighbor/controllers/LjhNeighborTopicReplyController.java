package com.cmig.future.csportal.module.operate.neighbor.controllers;

import com.cmig.future.csportal.module.operate.neighbor.dto.LjhNeighborTopicComment;
import com.cmig.future.csportal.module.operate.neighbor.dto.LjhNeighborTopicReply;
import com.cmig.future.csportal.module.operate.neighbor.service.ILjhNeighborTopicCommentService;
import com.cmig.future.csportal.module.operate.neighbor.service.ILjhNeighborTopicReplyService;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LjhNeighborTopicReplyController extends BaseController {

    @Autowired
    private ILjhNeighborTopicReplyService service;

    @RequestMapping(value = "/csp/ljh/neighbor/topic/reply/delete")
    @ResponseBody
    public ResponseData query(@RequestParam(value = "id", required = true) String id, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        LjhNeighborTopicReply reply = new LjhNeighborTopicReply();
        reply.setId(id);
        service.deleteReply(requestContext, reply);
        return new ResponseData();
    }
}
