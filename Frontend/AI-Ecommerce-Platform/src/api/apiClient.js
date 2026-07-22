export const API_BASE_URL = 'http://localhost:8090/product-service/api';

/**
 * Custom error class for API errors
 */
export class ApiError extends Error {
  constructor(message, status, data) {
    super(message);
    this.status = status;
    this.data = data;
    this.name = 'ApiError';
  }
}

/**
 * Centralized fetch wrapper to handle JSON parsing, credentials, and error throwing.
 */
export async function fetchClient(endpoint, options = {}) {
  const url = `${API_BASE_URL}${endpoint}`;
  const config = {
    ...options,
    headers: {
      'Content-Type': 'application/json',
      ...options.headers,
    },
    credentials: 'include', // essential for cookie-based JWT
  };

  if (config.body && typeof config.body === 'object') {
    config.body = JSON.stringify(config.body);
  }

  let response;
  try {
    response = await fetch(url, config);
  } catch (error) {
    throw new ApiError('Network error: Unable to reach the server', 0, null);
  }

  // Handle No Content
  if (response.status === 204) {
    return null;
  }

  const data = await response.json().catch(() => null);

  if (!response.ok) {
    let errorMessage = 'An error occurred';
    if (data) {
      errorMessage = data.message || data.error || errorMessage;
    }
    throw new ApiError(errorMessage, response.status, data);
  }

  return data;
}
