package com.shop.shoponline.service;

import com.shop.shoponline.entity.UserShoppingCart;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.shoponline.query.CartQuery;
import com.shop.shoponline.vo.CartGoodsVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sx
 * @since 2023-11-07
 */
public interface UserShoppingCartService extends IService<UserShoppingCart> {

//    添加购物车
  CartGoodsVO addShopCart(CartQuery query);

  //购物车列表
  List<CartGoodsVO> shopCartList(Integer userId);

}
