package com.tt.spec.locality.service;

import java.util.List;

import com.tt.spec.locality.model.Comment;
import com.tt.spec.locality.model.CommentObject;

public interface CommentService {

	void addComment(Comment comment, CommentObject commentObject);

	void deleteComment(String commentId);

	List<Comment> getCommentList(CommentObject object, int offset, int limit);

}
