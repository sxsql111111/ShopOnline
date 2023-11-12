package com.shop.shoponline.controller;

import com.shop.shoponline.common.exception.ServerException;
import com.shop.shoponline.common.result.Result;
import com.shop.shoponline.service.UserShippingAddressService;
import com.shop.shoponline.service.impl.UserShippingAddressServiceImpl;
import com.shop.shoponline.vo.AddressVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.shop.shoponline.common.utils.ObtainUserIdUtils.getUserId;

/**
 * Controller层则负责处理用户请求和响应用户
 *Service层提供了业务逻辑的封装，Controller层通过调用Service层的方法来实现具体的业务需求，并将处理结果返回给客户端
 * @author sx
 * @since 2023-11-07
 */
@Tag(name = "地址管理")
@RestController
@RequestMapping("member")
@AllArgsConstructor
public class UserShippingAddressController {

    private final UserShippingAddressService userShippingAddressService;

    @Operation(summary = "添加收货地址")
    @PostMapping("address")
    public Result<Integer> saveAddress(@RequestBody @Validated AddressVO addressVO, HttpServletRequest request) {
        Integer userId = getUserId(request);
        addressVO.setUserId(userId);
        Integer addressId = userShippingAddressService.saveShippingAddress(addressVO);
        return Result.ok(addressId);
    }

    @Operation(summary = "修改收货地址")
    @PutMapping("address")
    public Result<Integer> editAddress(@RequestBody @Validated AddressVO addressVO, HttpServletRequest request) {
        addressVO.setUserId(getUserId(request));
        Integer addressId = userShippingAddressService.editShippingAddress(addressVO);
        return Result.ok(addressId);
    }



    @Operation(summary = "收货地址列表")
    @GetMapping("address")
    public Result<List<AddressVO>> getAddressList(@RequestParam Integer userId){
        List<AddressVO> list=userShippingAddressService.getList(userId);
        return Result.ok(list);
    }

    @Operation(summary = "收货地址详情")
    @GetMapping("address/detail")
    public Result<AddressVO> getAddressDetail(@RequestParam Integer id){
        AddressVO addressDetail=userShippingAddressService.getAddressDetail(id);
       return Result.ok(addressDetail);

    }
    @Operation(summary = "删除收货地址")
    @DeleteMapping("address")
    public Result deleteAddress(@RequestParam Integer id){
        userShippingAddressService.deleteAddress(id);
        return Result.ok();

    }
}
