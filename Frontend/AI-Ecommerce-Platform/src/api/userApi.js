import { fetchClient, API_BASE_URL } from './apiClient';

const USER_API_BASE_URL = 'http://localhost:8090/user-service/api';

async function fetchUserClient(endpoint, options = {}) {
  const url = `${USER_API_BASE_URL}${endpoint}`;
  const config = {
    ...options,
    headers: {
      'Content-Type': 'application/json',
      ...options.headers,
    },
    credentials: 'include',
  };

  if (config.body && typeof config.body === 'object') {
    config.body = JSON.stringify(config.body);
  }

  const response = await fetch(url, config);

  if (response.status === 204) {
    return null;
  }

  const data = await response.json().catch(() => null);

  if (!response.ok) {
    let errorMessage = 'An error occurred';
    if (data) {
      errorMessage = data.message || data.error || errorMessage;
    }
    throw new Error(errorMessage);
  }

  return data;
}

// Profile Endpoints
export async function getUserProfile() {
  return fetchUserClient('/users/profile', { method: 'GET' });
}

export async function updateUserProfile(profileData) {
  return fetchUserClient('/users/profile', {
    method: 'PUT',
    body: profileData,
  });
}

export async function updatePassword(passwordData) {
  return fetchUserClient('/users/profile/password', {
    method: 'PUT',
    body: passwordData,
  });
}

export async function deleteUserAccount() {
  return fetchUserClient('/users/profile', { method: 'DELETE' });
}

// Address Endpoints
export async function getUserAddresses() {
  return fetchUserClient('/users/addresses', { method: 'GET' });
}

export async function addAddress(addressData) {
  return fetchUserClient('/users/addresses', {
    method: 'POST',
    body: addressData,
  });
}

export async function updateAddress(addressId, addressData) {
  return fetchUserClient(`/users/addresses/${addressId}`, {
    method: 'PUT',
    body: addressData,
  });
}

export async function deleteAddress(addressId) {
  return fetchUserClient(`/users/addresses/${addressId}`, { method: 'DELETE' });
}

export async function setDefaultAddress(addressId) {
  return fetchUserClient(`/users/addresses/${addressId}/default`, { method: 'PUT' });
}
