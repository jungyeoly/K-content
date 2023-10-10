package com.example.myapp.user.commucomment.service;

import com.example.myapp.user.commucomment.model.CommuComment;

import java.util.List;

public interface ICommuCommentService {

    void insertCommuComment(CommuComment commuComment);

    List<CommuComment> selectCommuCommentsByCommuCommentCommuId(int commuId);

    CommuComment selectCommuCommentById(int commuId);

    void deleteCommuCommentAndRepliesByMainRefId(int commuId);

    void deleteCommuCommentRepl(int commuId);
}
