package com.shop.shoponline.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shop.shoponline.common.exception.ServerException;
import com.shop.shoponline.entity.Goods;
import com.shop.shoponline.entity.UserShoppingCart;
import com.shop.shoponline.mapper.GoodsMapper;
import com.shop.shoponline.mapper.UserShoppingCartMapper;
import com.shop.shoponline.query.CartQuery;
import com.shop.shoponline.query.EditCartQuery;
import com.shop.shoponline.service.UserShoppingCartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.shoponline.vo.CartGoodsVO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author sx
 * @since 2023-11-07
 */
@Service
@AllArgsConstructor
public class UserShoppingCartServiceImpl extends ServiceImpl<UserShoppingCartMapper, UserShoppingCart> implements UserShoppingCartService {

    private final GoodsMapper goodsMapper;

    @Override
    public CartGoodsVO addShopCart(CartQuery query) {
        //查询商品是否存在，以及库存是否存在
        Goods goods=goodsMapper.selectById(query.getId());
        if (goods ==null){
            throw  new ServerException("商品信息不存在");

        }
        if (query.getCount() >goods.getInventory()){
            throw  new ServerException("商品库存不存在");
        }

        //插入购物车信息
        UserShoppingCart userShoppingCart=new UserShoppingCart();
        userShoppingCart.setUserId(query.getUserId());
        userShoppingCart.setGoodsId(goods.getId());
        userShoppingCart.setPrice(goods.getPrice());
        userShoppingCart.setCount(query.getCount());
        userShoppingCart.setAttrsText(query.getAttrsText());
        baseMapper.insert(userShoppingCart);

        //返回购物车添加购物车信息
        CartGoodsVO goodsVO=new CartGoodsVO();
        goodsVO.setId(userShoppingCart.getId());
        goodsVO.setName(goods.getName());
        goodsVO.setAttrsText(query.getAttrsText());
        goodsVO.setPrice(userShoppingCart.getPrice());
        goodsVO.setNowPrice(goods.getPrice());
        goodsVO.setSelected(userShoppingCart.getSelected());
        goodsVO.setStock(goods.getInventory());
        goodsVO.setCount(query.getCount());
        goodsVO.setPicture(goods.getCover());
        goodsVO.setDiscount(goods.getDiscount());

        return goodsVO;
    }

    @Override
    public List<CartGoodsVO> shopCartList(Integer userId) {
        List<CartGoodsVO> list=baseMapper.getCartGoodsInfo(userId);
        return list;
    }

    @Override
    public CartGoodsVO editCart(EditCartQuery query) {
        UserShoppingCart userShoppingCart = baseMapper.selectById(query.getId());
        if(userShoppingCart == null){
            throw new ServerException("购物车信息不存在");
        }
        userShoppingCart.setCount(query.getCount());
        userShoppingCart.setSelected(query.getSelected());
        baseMapper.updateById(userShoppingCart);
//        查询购物车信息
        Goods goods=goodsMapper.selectById(userShoppingCart.getGoodsId());
        if(query.getCount()>goods.getInventory()){
            throw new ServerException(goods.getName()+"库存数量不足");
        }
        CartGoodsVO goodsVO = new CartGoodsVO();
        goodsVO.setId(userShoppingCart.getId());
        goodsVO.setName(goods.getName());
        goodsVO.setAttrsText(userShoppingCart.getAttrsText());
        goodsVO.setPrice(userShoppingCart.getPrice());
        goodsVO.setNowPrice(goods.getPrice());
        goodsVO.setSelected(userShoppingCart.getSelected());
        goodsVO.setStock(goods.getInventory());
        goodsVO.setCount(query.getCount());
        goodsVO.setPicture(goods.getCover());
        goodsVO.setDiscount(goods.getDiscount());
        return goodsVO;
    }

    @Override
    public void removeCartGoods(Integer userId, List<Integer> ids) {
        List<UserShoppingCart> cartList=baseMapper.selectList(new LambdaQueryWrapper<UserShoppingCart>()
                .eq(UserShoppingCart::getUserId,userId));
        if(cartList.size()==0){
            return;
        }
//        与需要删除的购物车取交集
        List<UserShoppingCart> deleteCartList=cartList.stream().filter(item->
                ids.contains(item.getId())).collect(Collectors.toList());
//        删除购物车信息
        removeBatchByIds(deleteCartList);
    }


}
