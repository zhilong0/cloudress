package com.df.spec.locality.http.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.stereotype.Component;

import com.df.spec.locality.model.Comment;
import com.df.spec.locality.model.CommentObject;
import com.df.spec.locality.service.CommentService;

@Path("/comments")
@Produces("application/json;charset=UTF-8")
@Component
public class CommentResources {

	private static final int DEFAULT_PAGE_SIZE = 100;

	private CommentService commentService;

	public void setCommentService(CommentService commentService) {
		this.commentService = commentService;
	}

	@GET
	@Path("/{objectType}/{objectId}")
	public List<Comment> getShopCommentList(@PathParam("objectType") String objectType, @PathParam("objectId") String objectId,
			@QueryParam("offset") int offset, @QueryParam("limit") int limit) {
		if (offset < 0) {
			offset = 0;
		}
		if (limit <= 0) {
			limit = DEFAULT_PAGE_SIZE;
		}
		CommentObject co = new CommentObject(objectType, objectId);
		return commentService.getCommentList(co, offset, limit);
	}

}
