package com.shop.shoponline.service;

import com.shop.shoponline.entity.UserShoppingCart;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.shoponline.query.CartQuery;
import com.shop.shoponline.query.EditCartQuery;
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

  //修改购物车
  CartGoodsVO editCart(EditCartQuery query);

  //删除购物车
  void removeCartGoods(Integer userId,List<Integer> Ids);

  //    购物车全选/取消全选
  void editCartSelected(Boolean selected,Integer userId);

}
