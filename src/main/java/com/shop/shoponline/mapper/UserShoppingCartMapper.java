package com.shop.shoponline.mapper;

import com.shop.shoponline.entity.UserShoppingCart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.shoponline.vo.CartGoodsVO;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author sx
 * @since 2023-11-07
 */
public interface UserShoppingCartMapper extends BaseMapper<UserShoppingCart> {

    List<CartGoodsVO> getCartGoodsInfo(@Param("id") Integer id);


}
