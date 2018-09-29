package com.cmig.future.csportal.module.operate.neighbor.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cmig.future.csportal.module.operate.neighbor.dto.NhTopicCommentView;

@Repository
public interface NhTopicCommentViewMapper{

    List<NhTopicCommentView> findList(NhTopicCommentView nhTopicCommentView);

}