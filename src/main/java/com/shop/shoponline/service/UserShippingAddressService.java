package com.shop.shoponline.service;

import com.shop.shoponline.entity.UserShippingAddress;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.shoponline.vo.AddressVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sx
 * @since 2023-11-07
 */
public interface UserShippingAddressService extends IService<UserShippingAddress> {

    //添加收获地址
    Integer saveShippingAddress(AddressVO addressVO);
    //修改收获地址
    Integer  editShippingAddress(AddressVO addressVO);
    //获取收获地址
    List<AddressVO> getList(Integer userId);

}
