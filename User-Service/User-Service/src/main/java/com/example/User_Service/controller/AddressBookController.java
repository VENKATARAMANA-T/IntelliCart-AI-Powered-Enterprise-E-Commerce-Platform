package com.example.User_Service.controller;

import com.example.User_Service.dto.AddressDto;
import com.example.User_Service.model.AddressBook;
import com.example.User_Service.security.AuthenticatedUser;
import com.example.User_Service.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/addresses")
@PreAuthorize("isAuthenticated()")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    private Long getUserIdFromAuth(Authentication authentication) {
        AuthenticatedUser user = (AuthenticatedUser) authentication.getPrincipal();
        return user.getUserId();
    }

    @GetMapping
    public ResponseEntity<List<AddressBook>> getUserAddresses(Authentication authentication) {
        return ResponseEntity.ok(addressBookService.getUserAddresses(getUserIdFromAuth(authentication)));
    }

    @PostMapping
    public ResponseEntity<AddressBook> addAddress(
            @RequestBody AddressDto dto,
            Authentication authentication) {
        return ResponseEntity.ok(addressBookService.addAddress(getUserIdFromAuth(authentication), dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressBook> updateAddress(
            @PathVariable Long id,
            @RequestBody AddressDto dto,
            Authentication authentication) {
        return ResponseEntity.ok(addressBookService.updateAddress(getUserIdFromAuth(authentication), id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(
            @PathVariable Long id,
            Authentication authentication) {
        addressBookService.deleteAddress(getUserIdFromAuth(authentication), id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/default")
    public ResponseEntity<AddressBook> setDefaultAddress(
            @PathVariable Long id,
            Authentication authentication) {
        return ResponseEntity.ok(addressBookService.setDefaultAddress(getUserIdFromAuth(authentication), id));
    }
}
