import React from 'react';
import '../../styles/customer.css';
import { StarRating } from '../common/StarRating';

export function Storefront({ products, onSelectProduct }) {
  if (!products || products.length === 0) {
    return (
      <div className="empty-state">
        <p>No products available at the moment. Please check back later.</p>
      </div>
    );
  }

  return (
    <div className="storefront-grid">
      {products.map((product) => (
        <div key={product.id} className="product-card" onClick={() => onSelectProduct(product.id)}>
          <div className="product-image-wrapper">
            {product.thumbnailUrl ? (
              <img src={product.thumbnailUrl} alt={product.name} />
            ) : (
              <div className="img-placeholder">No Image</div>
            )}
            {product.discountPercent > 0 && (
              <div className="discount-badge">Save {product.discountPercent}%</div>
            )}
          </div>
          <div className="product-info">
            <span className="product-brand">{product.brand || 'Generic'}</span>
            <h3 className="product-name">{product.name}</h3>
            <StarRating rating={product.averageRating} count={product.totalReviews} />
            <div className="product-price">
              ${product.price?.toFixed(2)}
            </div>
            <p className="seller-name">Sold by {product.sellerUsername}</p>
            {product.stockCount <= 0 && (
              <div className="stock-error">Out of Stock</div>
            )}
          </div>
        </div>
      ))}
    </div>
  );
}
