package com.shop.shoponline.service;

import com.shop.shoponline.entity.UserOrderGoods;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sx
 * @since 2023-11-07
 */
public interface UserOrderGoodsService extends IService<UserOrderGoods> {

    /**
     * 批量插入订单记录
     */

    void batchUserOrderGoods(List<UserOrderGoods> list);
}
