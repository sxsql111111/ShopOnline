package com.shop.shoponline.service;

import com.shop.shoponline.common.result.PageResult;
import com.shop.shoponline.entity.UserOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.shoponline.query.OrderPreQuery;
import com.shop.shoponline.query.OrderQuery;
import com.shop.shoponline.vo.OrderDetailVO;
import com.shop.shoponline.vo.SubmitOrderVO;
import com.shop.shoponline.vo.UserAddressVO;
import com.shop.shoponline.vo.UserOrderVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sx
 * @since 2023-11-07
 */
public interface UserOrderService extends IService<UserOrder> {
    /**
     * 提交订单
     */
    Integer addGoodsOrder(UserOrderVO orderVO);

    /**
     * 订单详情
     *
     */
    OrderDetailVO getOrderDetail(Integer id);

    /**
     * 填写订单-获取预付订单
     */
    SubmitOrderVO getPreOrderDetail(Integer userId);


    List<UserAddressVO> getAddressListByUserId(Integer userId, Integer addressId);

    /**
     * 填写订单-获取立即购买订单
     */
    SubmitOrderVO getPreNowOrderDetail(OrderPreQuery query);

    /**
     * 填写订单-再次购买订单
     */
     SubmitOrderVO getRepurchaseOrderDetail(Integer id);

    /**
     * 订单列表
     *
     * @param query
     * @return
     */
    PageResult<OrderDetailVO> getOrderList(OrderQuery query);
}
