package com.tt.spec.locality.http.resources;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.tt.blobstore.image.ImageAttributes;
import com.tt.blobstore.image.ImageAttributesLoader;
import com.tt.blobstore.image.ImageKey;
import com.tt.blobstore.image.http.ImageDetails;
import com.tt.blobstore.image.http.ImageLinkCreator;
import com.tt.spec.locality.geo.Coordinate;
import com.tt.spec.locality.geo.GeoService;
import com.tt.spec.locality.model.Comment;
import com.tt.spec.locality.model.CommentObject;
import com.tt.spec.locality.model.Constants;
import com.tt.spec.locality.model.ImageSet;
import com.tt.spec.locality.model.Product;
import com.tt.spec.locality.model.Region;
import com.tt.spec.locality.model.Shop;
import com.tt.spec.locality.service.ApprovableService;
import com.tt.spec.locality.service.CommentService;
import com.tt.spec.locality.service.RegionService;
import com.tt.spec.locality.service.ShopService;

@Path("/shops")
@Produces("application/json;charset=UTF-8")
public class ShopResources {

	@Autowired
	private ShopService shopService;

	@Autowired
	private ImageAttributesLoader imageAttributeLoader;

	@Autowired
	private ImageLinkCreator imageLinkCreator;

	@Autowired
	private CommentService commentService;

	@Autowired
	private GeoService geoService;

	@Autowired
	private RegionService regionService;

	@Autowired
	private ApprovableService approvableService;

	private static final int DEFAULT_PAGE_SIZE = 100;

	public void setRegionService(RegionService regionService) {
		this.regionService = regionService;
	}

	public void setShopService(ShopService shopService) {
		this.shopService = shopService;
	}

	public void setCommentService(CommentService commentService) {
		this.commentService = commentService;
	}

	public void setImageLinkCreator(ImageLinkCreator imageLinkCreator) {
		this.imageLinkCreator = imageLinkCreator;
	}

	public void setGeoService(GeoService geoService) {
		this.geoService = geoService;
	}

	public void setApprovableService(ApprovableService approvableService) {
		this.approvableService = approvableService;
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
	@PreAuthorize("isAuthenticated()")
	public Shop addShop(Shop shop) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String regionCode = shop.getRegionCode();
		if (regionCode == null && shop.getAddress() != null) {
			Coordinate coordinate = geoService.lookupCoordinate(shop.getAddress(), null);
			Region region = geoService.lookupRegionWithCoordiate(coordinate);
			region = regionService.findRegion(region.getProvince(), region.getCity(), region.getDistrict());
			shop.setRegionCode(region.getCode());
		}
		shop.getImageSet().clear();
		if (shop.getImage() != null) {
			ImageAttributes ia = imageAttributeLoader.loadImageAttributes(new ImageKey(shop.getImage()));
			if (ia != null) {
				shop.getImageSet().addImage(new ImageDetails(shop.getImage(), ia));
			}
		}
		shop.setCreatedBy(authentication.getName());
		shopService.addShop(shop);
		return shop;
	}

	@POST
	@Path("/{shopCode}/products")
	@PreAuthorize("isAuthenticated()")
	public Product addProduct(String shopCode, Product product) {
		return shopService.addProduct(shopCode, product);
	}

	@DELETE
	@Path("/{shopCode}/products")
	@PreAuthorize("isAuthenticated()")
	public void deleteGoods(String shopCode, @QueryParam("productId") String productId) {
		if (productId == null) {
			return;
		}
		shopService.deleteProduct(shopCode, productId);
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
	@Path("/region/{regionCode}")
	public List<Shop> getShopListInRegion(@PathParam(value = "regionCode") String regionCode, @QueryParam("offset") int offset, @QueryParam("limit") int limit) {
		if (offset < 0) {
			offset = 0;
		}
		if (limit <= 0) {
			limit = 20;
		}
		List<Shop> shops = shopService.getShopListInRegion(regionCode, offset, limit);
		for (Shop shop : shops) {
			processImageLink(shop);
		}
		return shops;
	}

	@GET
	@Path("/to_be_approved")
	@PreAuthorize("hasPermission('SPECIALITY','MASTER_DATA_APPROVAL')")
	public List<Shop> getShopWaitList(@QueryParam("offset") int offset, @QueryParam("limit") int limit) {
		if (offset < 0) {
			offset = 0;
		}
		if (limit <= 0) {
			limit = 20;
		}
		List<Shop> shops = shopService.getWaitToApproveShopList(offset, limit);
		for (Shop shop : shops) {
			processImageLink(shop);
		}
		return shops;
	}

	@GET
	@Path("/mine")
	@PreAuthorize("isAuthenticated()")
	public List<Shop> getMyShops() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		List<Shop> shops = shopService.getMyShops(authentication.getName());
		for (Shop shop : shops) {
			processImageLink(shop);
		}
		return shops;
	}

	@POST
	@Path("/{shopCode}/images")
	@PreAuthorize("isAuthenticated() and hasPermission('SPECIALITY','MASTER_DATA_EDIT')")
	public boolean addImages(@PathParam("shopCode") String shopCode, String[] imageIds) {
		return shopService.updateImageSet(shopCode, imageIds, true);
	}

	@DELETE
	@Path("/{shopCode}/images")
	@PreAuthorize("isAuthenticated() and hasPermission('SPECIALITY','MASTER_DATA_EDIT')")
	public boolean deleteImages(@PathParam("shopCode") String shopCode, String[] imageIds) {
		return shopService.updateImageSet(shopCode, imageIds, false);
	}

	@POST
	@Path("/{shopCode}/reject")
	@PreAuthorize("isAuthenticated() and hasPermission('SPECIALITY','MASTER_DATA_APPROVAL')")
	public boolean rejectShop(@PathParam("shopCode") String shopCode, String rejectReason) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Shop shop = shopService.getShopByCode(shopCode, true);
		return approvableService.reject(shop, authentication.getName(), rejectReason);
	}

	@POST
	@Path("/{shopCode}/approve")
	@PreAuthorize("isAuthenticated() and hasPermission('SPECIALITY','MASTER_DATA_APPROVAL')")
	public boolean approveShop(@PathParam("shopCode") String shopCode) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Shop shop = shopService.getShopByCode(shopCode, true);
		return approvableService.approve(shop, authentication.getName());
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
		// set the image from the first image in the image set
		shop.setImage(null);
		for (ImageDetails image : imageSet.getImages()) {
			String link = imageLinkCreator.createImageLink(new ImageKey(image.getImageId()), image.getAttributes());
			if (shop.getImage() == null) {
				shop.setImage(link);
			}
			image.setImageLink(link);
		}
	}
}
