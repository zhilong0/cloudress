package com.df.spec.locality.dao.mongo;

import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;

import com.df.spec.locality.dao.CommentDao;
import com.df.spec.locality.model.Comment;
import com.df.spec.locality.model.CommentObject;
import com.df.spec.locality.model.Constants;
import com.google.common.collect.ImmutableList;
import com.mongodb.MongoClient;

public class CommentDaoImpl extends BasicDAO<Comment, ObjectId> implements CommentDao {

	public CommentDaoImpl(Datastore ds) {
		super(ds);
	}

	public CommentDaoImpl(MongoClient mongoClient, Morphia morphia, String dbName) {
		super(mongoClient, morphia, dbName);
	}

	@Override
	public void addComment(Comment comment) {
		this.save(comment);
	}

	@Override
	public void deleteComment(String commentId) {
		this.deleteById(new ObjectId(commentId));
	}

	@Override
	public List<Comment> getCommentList(CommentObject object, int offset, int limit) {
		Query<Comment> query = this.createQuery();
		query.filter(Constants.COMMENT.OBJECT_ID + " =", object.getObjectId());
		query.filter(Constants.COMMENT.OBJECT_TYPE + " =", object.getObjectType());
		query.order("-" + Constants.COMMENT.WRITE_DATE);
		query.offset(offset);
		query.limit(limit);
		return ImmutableList.copyOf(this.find(query));
	}

}
