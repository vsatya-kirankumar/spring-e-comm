package com.ecommerce.project.service;

import com.ecommerce.project.dto.AddressDTO;
import com.ecommerce.project.exception.APIException;
import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.Address;
import com.ecommerce.project.model.UserInfo;
import com.ecommerce.project.repositories.AddressRepository;
import com.ecommerce.project.repositories.UserRepository;
import com.ecommerce.project.security.response.MessageResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public AddressDTO addNewAddress(AddressDTO addressDTO, UserInfo user) {
        Address address = modelMapper.map(addressDTO, Address.class);
        List<Address> userAddresses = user.getAddresses();
        userAddresses.add(address);
        user.setAddresses(userAddresses);
        address.setUser(user);

        Address savedAddress = addressRepository.save(address);

        return modelMapper.map(savedAddress, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getAllAddress() {
        List<Address> address = addressRepository.findAll();

        List<AddressDTO> addressDTOS = address.stream().map(addr -> modelMapper.map(addr, AddressDTO.class)).toList();
        return addressDTOS;
    }

    @Override
    public AddressDTO getAddressById(Long addressId) {
        if (addressId <= 0) {
            throw new APIException("Please provide a Valid address Id");
        }
        Address addressById = addressRepository.findById(addressId).orElseThrow(() -> new ResourceNotFoundException(
                "Address", "addressId", addressId));

        return modelMapper.map(addressById, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getUserAddresses(UserInfo user) {
        List<Address> address = user.getAddresses();

        List<AddressDTO> addressDTOS = address.stream().map(addr -> modelMapper.map(addr, AddressDTO.class)).toList();
        return addressDTOS;
    }

    @Override
    public AddressDTO updateAddressById(Long addressId, AddressDTO addressDTO) {
        Address existingAddress = addressRepository.findById(addressId).orElseThrow(() -> new ResourceNotFoundException(
                "Address", "addressId", addressId));

        existingAddress.setCity(addressDTO.getCity());
        existingAddress.setPincode(addressDTO.getPincode());
        existingAddress.setCountry(addressDTO.getCountry());
        existingAddress.setStreet(addressDTO.getStreet());
        existingAddress.setBuildingName(addressDTO.getBuildingName());

        Address newAddress = addressRepository.save(existingAddress);

        UserInfo user = existingAddress.getUser();
        user.getAddresses().removeIf(address -> address.getAddressId().equals(addressId));
        user.getAddresses().add(newAddress);

        userRepository.save(user);

        return modelMapper.map(newAddress, AddressDTO.class);
    }

    @Override
    public String deleteAddress(Long addressId) {
        Address existingAddress = addressRepository.findById(addressId).orElseThrow(() -> new ResourceNotFoundException(
                "Address", "addressId", addressId));
        UserInfo user = existingAddress.getUser();
        user.getAddresses().removeIf(address -> address.getAddressId().equals(addressId));
        userRepository.save(user);
        addressRepository.delete(existingAddress);

        return "Address Deleted Successfully";
    }
}