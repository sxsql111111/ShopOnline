package com.shop.shoponline.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shop.shoponline.common.exception.ServerException;
import com.shop.shoponline.common.result.PageResult;
import com.shop.shoponline.convert.AddressConvert;
import com.shop.shoponline.convert.GoodsConvert;
import com.shop.shoponline.entity.Goods;
import com.shop.shoponline.entity.IndexCarousel;
import com.shop.shoponline.entity.UserShippingAddress;
import com.shop.shoponline.enums.AddressDefaultEnum;
import com.shop.shoponline.enums.AddressDeleteFlagEnum;
import com.shop.shoponline.mapper.UserShippingAddressMapper;
import com.shop.shoponline.service.UserShippingAddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.shoponline.vo.AddressVO;
import com.shop.shoponline.vo.RecommendGoodsVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author sx
 * @since 2023-11-07
 */
@Service
public class UserShippingAddressServiceImpl extends ServiceImpl<UserShippingAddressMapper, UserShippingAddress> implements UserShippingAddressService {

    @Override
    public Integer saveShippingAddress(AddressVO addressVO) {
        UserShippingAddress convert = AddressConvert.INSTANCE.convert(addressVO);
        if (addressVO.getIsDefault() == AddressDefaultEnum.DEFAULT_ADDRESS.getValue()) {
            List<UserShippingAddress> list = baseMapper.selectList(new
                    LambdaQueryWrapper<UserShippingAddress>().eq(UserShippingAddress::getIsDefault,
                    AddressDefaultEnum.DEFAULT_ADDRESS.getValue()));
            if (list.size() > 0) {
                throw new ServerException("已经存在默认地址，请勿重复操作");
            }
        }
        save(convert);
        return convert.getId();
    }

    @Override
    public Integer editShippingAddress(AddressVO addressVO) {
        UserShippingAddress userShippingAddress = baseMapper.selectById(addressVO.getId());
        if (userShippingAddress == null) {
            throw new ServerException("地址不存在");
        }
        if (addressVO.getIsDefault() == AddressDefaultEnum.DEFAULT_ADDRESS.getValue()) {
            //查询该用户是否已经存在默认地址
            UserShippingAddress address = baseMapper.selectOne(new LambdaQueryWrapper<UserShippingAddress>()
                    .eq(UserShippingAddress::getId, addressVO.getUserId()).eq(UserShippingAddress::getIsDefault,
                            AddressDefaultEnum.DEFAULT_ADDRESS.getValue()));


            if (address != null) {
                address.setIsDefault(AddressDefaultEnum.NOT_DEFAULT_ADDRESS.getValue());
                updateById(address);
            }

        }
        UserShippingAddress address = AddressConvert.INSTANCE.convert(addressVO);
        updateById(address);
        return address.getId();
    }

    //地址列表
    @Override
    public List<AddressVO> getList(Integer userId) {
        LambdaQueryWrapper<UserShippingAddress> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserShippingAddress::getUserId, userId);
//        根据是否为默认地址和创建时间倒序排列
        wrapper.orderByDesc(UserShippingAddress::getIsDefault);
        wrapper.orderByDesc(UserShippingAddress::getCreateTime);
        List<UserShippingAddress> list = baseMapper.selectList(wrapper);
        List<AddressVO> results = AddressConvert.INSTANCE.convertToAddressVOList(list);
        return results;
    }

    //获取地址详情
    @Override
    public AddressVO getAddressInfo(Integer id) {
        UserShippingAddress userShippingAddress = baseMapper.selectById(id);
        if (userShippingAddress == null) {
            throw new ServerException("地址不存在");
        }
        AddressVO addressVO = AddressConvert.INSTANCE.convertToAddressVO(userShippingAddress);
        return addressVO;
    }

    @Override
    public void removeShippingAddress(Integer id) {
        removeById(id);
    }
}
