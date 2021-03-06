package com.df.spec.locality.service;

import java.util.List;

import com.df.spec.locality.model.Comment;
import com.df.spec.locality.model.CommentObject;

public interface CommentService {

	void addComment(Comment comment, CommentObject commentObject);

	void deleteComment(String commentId);

	List<Comment> getCommentList(CommentObject object, int offset, int limit);

}
