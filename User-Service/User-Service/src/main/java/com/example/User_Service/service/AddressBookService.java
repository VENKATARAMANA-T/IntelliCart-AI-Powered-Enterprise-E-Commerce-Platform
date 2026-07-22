package com.example.User_Service.service;

import com.example.User_Service.dto.AddressDto;
import com.example.User_Service.model.AddressBook;

import java.util.List;

public interface AddressBookService {
    List<AddressBook> getUserAddresses(Long userId);
    AddressBook addAddress(Long userId, AddressDto dto);
    AddressBook updateAddress(Long userId, Long addressId, AddressDto dto);
    void deleteAddress(Long userId, Long addressId);
    AddressBook setDefaultAddress(Long userId, Long addressId);
}
