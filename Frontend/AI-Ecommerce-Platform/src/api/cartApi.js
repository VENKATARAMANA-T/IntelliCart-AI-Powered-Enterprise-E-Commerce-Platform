import { fetchClient, API_BASE_URL } from './apiClient';

// Override the base URL since fetchClient defaults to product-service
const CART_API_BASE_URL = 'http://localhost:8090/cart-service/api';

async function fetchCartClient(endpoint, options = {}) {
  const url = `${CART_API_BASE_URL}${endpoint}`;
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

export async function getCart() {
  return fetchCartClient('/cart', { method: 'GET' });
}

export async function addToCart(productId, quantity = 1) {
  return fetchCartClient('/cart/items', {
    method: 'POST',
    body: { productId, quantity },
  });
}

export async function updateCartItem(productId, quantity) {
  return fetchCartClient(`/cart/items/${productId}`, {
    method: 'PUT',
    body: { quantity },
  });
}

export async function removeFromCart(productId) {
  return fetchCartClient(`/cart/items/${productId}`, { method: 'DELETE' });
}

export async function clearCart() {
  return fetchCartClient('/cart', { method: 'DELETE' });
}
