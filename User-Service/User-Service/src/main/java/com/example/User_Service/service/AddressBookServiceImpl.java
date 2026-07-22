package com.example.User_Service.service;

import com.example.User_Service.dto.AddressDto;
import com.example.User_Service.model.AddressBook;
import com.example.User_Service.model.User;
import com.example.User_Service.repository.AddressBookRepository;
import com.example.User_Service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AddressBookServiceImpl implements AddressBookService {

    @Autowired
    private AddressBookRepository addressBookRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<AddressBook> getUserAddresses(Long userId) {
        return addressBookRepository.findByUserId(userId);
    }

    @Override
    public AddressBook addAddress(Long userId, AddressDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<AddressBook> existing = addressBookRepository.findByUserId(userId);
        
        AddressBook address = new AddressBook();
        address.setUser(user);
        address.setFullName(dto.getFullName() != null ? dto.getFullName() : user.getFirstName() + " " + user.getLastName());
        address.setAddressLine1(dto.getAddressLine1());
        address.setAddressLine2(dto.getAddressLine2());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setZipCode(dto.getZipCode());
        address.setCountry(dto.getCountry());
        
        if (existing.isEmpty() || dto.isDefault()) {
            if (dto.isDefault()) {
                for (AddressBook addr : existing) {
                    addr.setDefault(false);
                    addressBookRepository.save(addr);
                }
            }
            address.setDefault(true);
        } else {
            address.setDefault(false);
        }

        return addressBookRepository.save(address);
    }

    @Override
    public AddressBook updateAddress(Long userId, Long addressId, AddressDto dto) {
        AddressBook address = addressBookRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (!address.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        if (dto.getFullName() != null) address.setFullName(dto.getFullName());
        if (dto.getAddressLine1() != null) address.setAddressLine1(dto.getAddressLine1());
        if (dto.getAddressLine2() != null) address.setAddressLine2(dto.getAddressLine2());
        if (dto.getCity() != null) address.setCity(dto.getCity());
        if (dto.getState() != null) address.setState(dto.getState());
        if (dto.getZipCode() != null) address.setZipCode(dto.getZipCode());
        if (dto.getCountry() != null) address.setCountry(dto.getCountry());

        if (dto.isDefault() && !address.isDefault()) {
            List<AddressBook> existing = addressBookRepository.findByUserId(userId);
            for (AddressBook addr : existing) {
                addr.setDefault(false);
                addressBookRepository.save(addr);
            }
            address.setDefault(true);
        }

        return addressBookRepository.save(address);
    }

    @Override
    public void deleteAddress(Long userId, Long addressId) {
        AddressBook address = addressBookRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (!address.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        addressBookRepository.delete(address);
    }

    @Override
    public AddressBook setDefaultAddress(Long userId, Long addressId) {
        AddressBook address = addressBookRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (!address.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        List<AddressBook> existing = addressBookRepository.findByUserId(userId);
        for (AddressBook addr : existing) {
            addr.setDefault(false);
            addressBookRepository.save(addr);
        }

        address.setDefault(true);
        return addressBookRepository.save(address);
    }
}
