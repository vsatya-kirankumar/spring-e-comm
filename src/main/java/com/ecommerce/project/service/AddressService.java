package com.ecommerce.project.service;

import com.ecommerce.project.dto.AddressDTO;
import com.ecommerce.project.model.UserInfo;
import jakarta.validation.Valid;

import java.util.List;

public interface AddressService {
    AddressDTO addNewAddress(AddressDTO addressDTO, UserInfo user);

    List<AddressDTO> getAllAddress();

    AddressDTO getAddressById(Long addressId);

    List<AddressDTO> getUserAddresses(UserInfo user);

    AddressDTO updateAddressById(Long addressId, @Valid AddressDTO addressDTO);

    String deleteAddress(Long addressId);
}