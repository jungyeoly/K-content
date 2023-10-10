package com.example.myapp.user.commucomment.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.myapp.user.commucomment.dao.ICommuCommentRepository;
import com.example.myapp.user.commucomment.model.CommuComment;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommuCommentService implements ICommuCommentService {
    static final Logger logger = LoggerFactory.getLogger(CommuCommentService.class);
    @Autowired
    ICommuCommentRepository commucommentRepository;

    @Override
    public void insertCommuComment(CommuComment cc) {
        commucommentRepository.insertCommuComment(cc);
    }

    @Override
    public List<CommuComment> selectCommuCommentsByCommuCommentCommuId(int commuId) {
        return commucommentRepository.selectCommuCommentsByCommuCommentCommuId(commuId);
    }

    @Override
    public CommuComment selectCommuCommentById(int commuId) {

        return commucommentRepository.selectCommuCommentById(commuId);
    }

    //부모 댓글이 삭제
    @Override
    @Transactional
    public void deleteCommuCommentAndRepliesByMainRefId(int commuId) {
        commucommentRepository.deleteCommuCommentRefAll(commuId);
        commucommentRepository.deleteCommuComment(commuId);
    }
    // 자식 하나 삭제
    @Override
    public void deleteCommuCommentRepl(int commuId) {

    }


}
