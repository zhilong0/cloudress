package com.df.spec.locality.http.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.stereotype.Component;

import com.df.blobstore.image.ImageKey;
import com.df.blobstore.image.http.ImageDetails;
import com.df.blobstore.image.http.ImageLinkCreator;
import com.df.spec.locality.model.Comment;
import com.df.spec.locality.model.CommentObject;
import com.df.spec.locality.model.Constants;
import com.df.spec.locality.model.ImageSet;
import com.df.spec.locality.model.Shop;
import com.df.spec.locality.service.CommentService;
import com.df.spec.locality.service.ShopService;

@Path("/shops")
@Produces("application/json;charset=UTF-8")
@Component
public class ShopResources {

	private ShopService shopService;

	private ImageLinkCreator imageLinkCreator;

	private CommentService commentService;

	private static final int DEFAULT_PAGE_SIZE = 100;

	public void setShopService(ShopService shopService) {
		this.shopService = shopService;
	}

	public void setCommentService(CommentService commentService) {
		this.commentService = commentService;
	}

	public void setImageLinkCreator(ImageLinkCreator imageLinkCreator) {
		this.imageLinkCreator = imageLinkCreator;
	}

	@GET
	@Path("/{shopCode}")
	public Shop getShopByCode(@PathParam("shopCode") String shopCode) {
		Shop shop = shopService.getShopByCode(shopCode, true);
		processImageLink(shop);
		return shop;
	}

	@POST
	@Path("/")
	public Shop addShop(Shop shop) {
		shopService.addShop(shop, shop.getRegionCode());
		return shop;
	}

	@GET
	@Path("/speciality/{specialityCode}")
	public List<Shop> getShopListSellSpecaility(@PathParam(value = "specialityCode") String specialityCode) {
		List<Shop> shops = shopService.getShopListSellSpeciality(specialityCode);
		for (Shop shop : shops) {
			processImageLink(shop);
		}
		return shops;
	}

	@GET
	@Path("/{shopCode}/comment")
	public List<Comment> getShopCommentList(@PathParam("shopCode") String shopCode, @QueryParam("offset") int offset, @QueryParam("limit") int limit) {
		if (offset < 0) {
			offset = 0;
		}
		if (limit <= 0) {
			limit = DEFAULT_PAGE_SIZE;
		}
		CommentObject co = new CommentObject(Constants.SHOP.COMMENT_OBJECT_TYPE, shopCode);
		return commentService.getCommentList(co, offset, limit);
	}

	@POST
	@Path("/{shopCode}/comment")
	public Comment addComment(@PathParam("shopCode") String shopCode, Comment comment) {
		CommentObject co = new CommentObject(Constants.SHOP.COMMENT_OBJECT_TYPE, shopCode);
		commentService.addComment(comment, co);
		return comment;
	}

	protected void processImageLink(Shop shop) {
		ImageSet imageSet = shop.getImageSet();
		for (ImageDetails image : imageSet.getImages()) {
			String link = imageLinkCreator.createImageLink(new ImageKey(image.getImageId()), image.getAttributes());
			image.setImageLink(link);
		}
	}
}
