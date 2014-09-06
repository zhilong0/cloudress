package com.df.spec.locality.dao;

import java.util.List;

import com.df.spec.locality.model.Comment;
import com.df.spec.locality.model.CommentObject;

public interface CommentDao {

	void addComment(Comment comment);

	void deleteComment(String commentId);

	List<Comment> getCommentList(CommentObject object, int offset, int limit);
}
