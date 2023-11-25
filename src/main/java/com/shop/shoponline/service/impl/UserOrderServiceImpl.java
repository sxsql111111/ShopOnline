package com.shop.shoponline.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shop.shoponline.common.exception.ServerException;
import com.shop.shoponline.convert.UserOrderDetailConvert;
import com.shop.shoponline.entity.Goods;
import com.shop.shoponline.entity.UserOrder;
import com.shop.shoponline.entity.UserOrderGoods;
import com.shop.shoponline.entity.UserShippingAddress;
import com.shop.shoponline.enums.OrderStatusEnum;
import com.shop.shoponline.mapper.GoodsMapper;
import com.shop.shoponline.mapper.UserOrderMapper;
import com.shop.shoponline.query.OrderGoodsQuery;
import com.shop.shoponline.service.UserOrderGoodsService;
import com.shop.shoponline.service.UserOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.shoponline.vo.OrderDetailVO;
import com.shop.shoponline.vo.UserOrderVO;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author sx
 * @since 2023-11-07
 */
@Service
public class UserOrderServiceImpl extends ServiceImpl<UserOrderMapper, UserOrder> implements UserOrderService {
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private UserOrderGoodsService userOrderGoodsService;
    private ScheduledExecutorService executorService= Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> cancelTask;
    @Async
    public void scheduOrderCancel(UserOrder userOrder){
        cancelTask=executorService.schedule(()->{
            if (userOrder.getStatus()== OrderStatusEnum.WAITING_FOR_PAYMENT.getValue()){
                userOrder.setStatus(OrderStatusEnum.CANCELLED.getValue());
                baseMapper.updateById(userOrder);
            }
        },30, TimeUnit.MINUTES);
    }
    public void cancelScheduledTask(){
        if(cancelTask !=null&&!cancelTask.isDone()){
            cancelTask.cancel(true);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer addGoodsOrder(UserOrderVO orderVO) {
        //1.声明订单总支付费用。总费运，总购买件数
        BigDecimal totalPrice =new BigDecimal(0);
        Integer totalCount=0;
        BigDecimal totalFreight=new BigDecimal(0);
        UserOrder userOrder=new UserOrder();
        userOrder.setUserId(orderVO.getUserId());
        userOrder.setAddressId(orderVO.getAddressId());

        //订单编号使用uuid随机生成不重复的编号
        userOrder.setOrderNumber(UUID.randomUUID().toString());
        userOrder.setDeliveryTimeType(orderVO.getDeliveryType());
        //提交订单默认状态为待付款
        userOrder.setStatus(OrderStatusEnum.WAITING_FOR_PAYMENT.getValue());
        if (orderVO.getBuyerMessage()!=null){
            userOrder.setBuyerMessage(orderVO.getBuyerMessage());
        }
        userOrder.setPayType(orderVO.getPayType());
        userOrder.setPayChannel(orderVO.getPayChannel());

        //异步取消创建的订单，如果订单创建30分钟后用户没有付款，修改订单状态为取消
        scheduOrderCancel(userOrder);
        List<UserOrderGoods> orderGoodsList = new ArrayList<>( );
        //购买的商品列表，订单-商品表批量添加数据
        for (OrderGoodsQuery goodsVo :orderVO.getGoods()){
            Goods goods=goodsMapper.selectById(goodsVo.getId());
            if(goodsVo .getCount() >goods.getInventory()){
                throw  new ServerException(goods.getName()+"库存不足");
            }
            UserOrderGoods userOrderGoods=new UserOrderGoods();
            userOrderGoods.setGoodsId(goods.getId());
            userOrderGoods.setName(goods.getName());
            userOrderGoods.setCover(goods.getCover());
            userOrderGoods.setOrderId(userOrder.getId());
            userOrderGoods.setCount(goodsVo.getCount());
            userOrderGoods.setAttrsText(goodsVo.getSkus());
            userOrderGoods.setFreight(goods.getFreight());
            userOrderGoods.setPrice(goods.getPrice());

            //计算总付款金额，使用BigDecimal ,避免进度缺失
            BigDecimal freight=new BigDecimal(userOrderGoods.getFreight().toString());
            BigDecimal goodsPrice=new BigDecimal(userOrderGoods.getPrice().toString());
            BigDecimal count =new BigDecimal(userOrderGoods.getCount().toString());


            //减库存
            goods.setInventory(goods.getInventory()-goodsVo.getCount());
            //增加销量
            goods.setSalesCount(goodsVo.getCount());
            BigDecimal price=goodsPrice.multiply(count).add(freight);
            totalPrice=totalPrice.add(price);
            totalCount+=goodsVo.getCount();
            totalFreight=totalFreight.add(freight);
            orderGoodsList.add(userOrderGoods);
            goodsMapper.updateById(goods);

        }
        userOrderGoodsService.batchUserOrderGoods(orderGoodsList);
        userOrder.setTotalPrice(totalPrice.doubleValue( ));
        userOrder.setTotalCount(totalCount);
        userOrder.setTotalFreight(totalFreight.doubleValue( ));
        baseMapper.updateById(userOrder);
        return userOrder.getId();

    }

    @Override
    public OrderDetailVO getOrderDetail(Integer id) {
        //1.订单信息
        UserOrder userOrder=baseMapper.selectById(id);
        if (userOrder ==null){
            throw  new ServerException("订单信息不存在");

        }
        OrderDetailVO orderDetailVO = UserOrderDetailConvert.INSTANCE.converToOrderDetail(userOrder);
        orderDetailVO.setTotalMoney(UserOrder.getTotalPrice());

        //收货人信息
        UserShippingAddress userShippingAddress=
                userShipngAddressMapper.selectById*(userOrder.getAddressId());

        if (userShippingAddress ==null){
            throw  new ServerException("收货地址信息不存在");
        }
        orderDetailVO.setReceiverContact(userShippingAddress.getReceiver());
        orderDetailVO.setReceiverMobile(userShippingAddress.getContact());
        orderDetailVO.setReceiverAddress(userShippingAddress.getAddress());

        //3.商品集合
        List<UserOrderGoods> list= userOrderGoodsMapper.selectList(new LambdaQueryWrapper<UserOrderGoods>()
                .eq(userOrderGoods::getOrderId ,id));

        o
        return null;
    }
}
