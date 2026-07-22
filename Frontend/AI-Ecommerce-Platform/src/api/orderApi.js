import { fetchClient, API_BASE_URL } from './apiClient';

const ORDER_API_BASE_URL = 'http://localhost:8090/order-service/api';

async function fetchOrderClient(endpoint, options = {}) {
  const url = `${ORDER_API_BASE_URL}${endpoint}`;
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

export async function placeOrder(payload) {
  return fetchOrderClient('/orders', {
    method: 'POST',
    body: payload,
  });
}

export async function getCustomerOrders() {
  return fetchOrderClient('/orders', { method: 'GET' });
}

export async function getOrderById(orderId) {
  return fetchOrderClient(`/orders/${orderId}`, { method: 'GET' });
}

export async function getSellerOrders() {
  return fetchOrderClient('/orders/seller', { method: 'GET' });
}

export async function cancelOrder(orderId) {
  return fetchOrderClient(`/orders/${orderId}/cancel`, { method: 'PUT' });
}
