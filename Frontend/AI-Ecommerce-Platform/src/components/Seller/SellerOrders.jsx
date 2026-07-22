import React, { useState, useEffect } from 'react';
import { getSellerOrders } from '../../api/orderApi';
import { Loader } from '../common/Loader';
import '../../styles/seller.css';

export function SellerOrders() {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchOrders();
  }, []);

  const fetchOrders = async () => {
    setLoading(true);
    try {
      const data = await getSellerOrders();
      setOrders(data);
    } catch (err) {
      setError(err.message || 'Failed to load sales data');
    } finally {
      setLoading(false);
    }
  };

  const getStatusColor = (status) => {
    switch(status) {
      case 'PENDING': return '#f0ad4e';
      case 'CONFIRMED': return '#5bc0de';
      case 'SHIPPED': return '#337ab7';
      case 'DELIVERED': return '#5cb85c';
      case 'CANCELLED': return '#d9534f';
      default: return '#777';
    }
  };

  if (loading) return <Loader text="Loading sales data..." />;
  if (error) return <div className="flash flash-error">{error}</div>;

  const validOrders = orders.filter(item => item.orderStatus !== 'CANCELLED');
  const totalRevenue = validOrders.reduce((sum, item) => sum + parseFloat(item.itemTotal), 0);
  const totalSalesCount = validOrders.length;

  return (
    <div className="seller-orders">
      <div className="seller-orders-summary">
        <div className="summary-card">
          <span className="summary-label">Total Sales</span>
          <span className="summary-value">{totalSalesCount}</span>
        </div>
        <div className="summary-card">
          <span className="summary-label">Revenue</span>
          <span className="summary-value revenue">${totalRevenue.toFixed(2)}</span>
        </div>
      </div>

      {orders.length === 0 ? (
        <div className="empty-orders">
          <p>No sales yet. Products will appear here once customers place orders.</p>
        </div>
      ) : (
        <table className="seller-orders-table">
          <thead>
            <tr>
              <th>Order #</th>
              <th>Product</th>
              <th>Price</th>
              <th>Qty</th>
              <th>Total</th>
              <th>Status</th>
              <th>Date</th>
            </tr>
          </thead>
          <tbody>
            {orders.map((item, idx) => (
              <tr key={idx}>
                <td>#{item.orderId}</td>
                <td>{item.productName}</td>
                <td>${parseFloat(item.productPrice).toFixed(2)}</td>
                <td>{item.quantity}</td>
                <td className="total-cell">${parseFloat(item.itemTotal).toFixed(2)}</td>
                <td>
                  <span className="status-badge" style={{ backgroundColor: getStatusColor(item.orderStatus) }}>
                    {item.orderStatus}
                  </span>
                </td>
                <td>{new Date(item.orderDate).toLocaleDateString()}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}
