import { fetchClient } from './apiClient';

/**
 * Product-Service API wrappers
 */

// -----------------------------------------------------------------------
// Customer / Public APIs
// -----------------------------------------------------------------------

/**
 * Browse all available products.
 * @returns {Promise<Array>} List of ProductSummaryResponse
 */
export async function getProducts() {
  return fetchClient('/products', { method: 'GET' });
}

/**
 * Get full details of a specific product.
 * @param {number} productId 
 * @returns {Promise<Object>} ProductResponse
 */
export async function getProductById(productId) {
  return fetchClient(`/products/${productId}`, { method: 'GET' });
}

/**
 * Get all reviews for a product.
 * @param {number} productId 
 * @returns {Promise<Array>} List of ReviewResponse
 */
export async function getProductReviews(productId) {
  return fetchClient(`/products/${productId}/reviews`, { method: 'GET' });
}

/**
 * Add a review for a product. (CUSTOMER role only)
 * @param {number} productId 
 * @param {Object} reviewData { rating, title, description }
 * @returns {Promise<Object>} ReviewResponse
 */
export async function addReview(productId, reviewData) {
  return fetchClient(`/products/${productId}/reviews`, {
    method: 'POST',
    body: reviewData,
  });
}

export async function editReview(productId, reviewId, reviewData) {
  return fetchClient(`/products/${productId}/reviews/${reviewId}`, {
    method: 'PUT',
    body: reviewData,
  });
}

export async function deleteReview(productId, reviewId) {
  return fetchClient(`/products/${productId}/reviews/${reviewId}`, {
    method: 'DELETE',
  });
}

// -----------------------------------------------------------------------
// Seller APIs
// -----------------------------------------------------------------------

/**
 * List products owned by the authenticated seller.
 * @returns {Promise<Array>} List of ProductSummaryResponse
 */
export async function getMyProducts() {
  return fetchClient('/products/my', { method: 'GET' });
}

/**
 * Create a new product. (SELLER role only)
 * @param {Object} productData CreateProductRequest payload
 * @returns {Promise<Object>} ProductResponse
 */
export async function createProduct(productData) {
  return fetchClient('/products', {
    method: 'POST',
    body: productData,
  });
}

/**
 * Update an existing product. (SELLER role and Owner only)
 * @param {number} productId 
 * @param {Object} updateData UpdateProductRequest payload (patch-style)
 * @returns {Promise<Object>} ProductResponse
 */
export async function updateProduct(productId, updateData) {
  return fetchClient(`/products/${productId}`, {
    method: 'PUT',
    body: updateData,
  });
}

/**
 * Delete a product. (SELLER role and Owner only)
 * @param {number} productId 
 * @returns {Promise<null>}
 */
export async function deleteProduct(productId) {
  return fetchClient(`/products/${productId}`, { method: 'DELETE' });
}
