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
    //收货人地址列表
    List<AddressVO> getList(Integer userId);
    //收货地址详情
    AddressVO getAddressDetail(Integer id);
    //删除收货地址
    void deleteAddress(Integer id);


}
