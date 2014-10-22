package com.tt.spec.locality.dao;

import java.util.List;

import com.tt.blobstore.image.http.ImageDetails;
import com.tt.spec.locality.model.Product;
import com.tt.spec.locality.model.Shop;

public interface ShopDao extends DaoTemplate {

	boolean update(Shop shop);

	boolean disable(String shopCode);

	boolean enable(String shopCode);

	List<Shop> getShopListInRegion(String regionCode, int offset, int limit);

	List<Shop> getShopListByCreatedBy(String createdBy);

	List<String> getShopCodeListBySpeciality(String specialityCode, int offset, int limit);

	List<Shop> getShopList(List<String> shopCodeList, boolean filterDisabled);

	Shop find(String shopName, String address);

	List<Shop> getWaitToApproveShopList(int offset, int limit);

	Product addProduct(String shopCode, Product product);

	Product getProduct(String shopCode, String specialityCode);

	List<Product> getProductList(String shopCode);

	boolean updateProduct(Product product);

	boolean markProductAsDelete(String productId);

	boolean addImages(String shopCode, ImageDetails[] images);

	boolean deleteImages(String shopCode, String[] imageIds);

}
