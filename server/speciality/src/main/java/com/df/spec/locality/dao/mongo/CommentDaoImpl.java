package com.df.spec.locality.dao.mongo;

import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;

import com.df.spec.locality.dao.CommentDao;
import com.df.spec.locality.model.Comment;
import com.df.spec.locality.model.CommentObject;
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
		// TODO Auto-generated method stub
		return null;
	}

}
