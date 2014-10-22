package com.tt.spec.locality.dao;

import java.util.List;

import com.tt.spec.locality.model.Comment;
import com.tt.spec.locality.model.CommentObject;

public interface CommentDao {

	void addComment(Comment comment);

	void deleteComment(String commentId);

	List<Comment> getCommentList(CommentObject object, int offset, int limit);
}
