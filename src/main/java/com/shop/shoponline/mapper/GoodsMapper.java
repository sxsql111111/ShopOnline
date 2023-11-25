package com.shop.shoponline.mapper;

import com.shop.shoponline.entity.Goods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.shoponline.vo.UserOrderGoodsVO;
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
public interface GoodsMapper extends BaseMapper<Goods> {

    /**
     * 根据订单查询商品信息列表
     */
    List<UserOrderGoodsVO> getGoodsListByOrderId(@Param("id") Integer id);
}

