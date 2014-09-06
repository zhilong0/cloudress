package com.df.spec.locality.service.impl;

import java.util.Date;

import org.springframework.util.Assert;

import com.df.spec.locality.dao.CommentDao;
import com.df.spec.locality.model.Comment;
import com.df.spec.locality.model.CommentObject;
import com.df.spec.locality.service.CommentService;

public class CommentServiceImpl implements CommentService {

	private CommentDao commentDao;

	public CommentServiceImpl(CommentDao commentDao) {
		this.commentDao = commentDao;
	}

	public void setCommentDao(CommentDao commentDao) {
		this.commentDao = commentDao;
	}

	@Override
	public void addComment(Comment comment, CommentObject commentObject) {
		Assert.notNull(commentObject);
		Assert.notNull(commentObject.getObjectId());
		Assert.notNull(commentObject.getObjectType());
		Assert.notNull(comment.getComment());
		comment.setWriteDate(new Date());
		comment.setCommentObject(commentObject);
		commentDao.addComment(comment);
	}

	@Override
	public void deleteComment(String commentId) {
		commentDao.deleteComment(commentId);
	}

}
