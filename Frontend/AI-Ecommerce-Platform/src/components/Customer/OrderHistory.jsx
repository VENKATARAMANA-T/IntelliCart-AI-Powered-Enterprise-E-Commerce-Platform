import React, { useState, useEffect } from 'react';
import { getCustomerOrders, cancelOrder } from '../../api/orderApi';
import { Loader } from '../common/Loader';
import { Button } from '../common/Button';
import '../../styles/customer.css';

export function OrderHistory({ onBack }) {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [expandedOrderId, setExpandedOrderId] = useState(null);

  useEffect(() => {
    fetchOrders();
  }, []);

  const fetchOrders = async () => {
    setLoading(true);
    try {
      const data = await getCustomerOrders();
      setOrders(data);
    } catch (err) {
      setError(err.message || 'Failed to load orders');
    } finally {
      setLoading(false);
    }
  };

  const toggleExpand = (orderId) => {
    setExpandedOrderId(expandedOrderId === orderId ? null : orderId);
  };

  const handleCancel = async (e, orderId) => {
    e.stopPropagation();
    if (!window.confirm('Are you sure you want to cancel this order?')) return;
    
    setLoading(true);
    try {
      await cancelOrder(orderId);
      await fetchOrders(); // refresh the list
    } catch (err) {
      alert(err.message || 'Failed to cancel order');
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

  if (loading) return <Loader text="Loading your orders..." />;
  if (error) return <div className="flash flash-error">{error}</div>;

  return (
    <div className="order-history">
      <div className="order-history-header">
        <button className="back-link" onClick={onBack}>← Back to Store</button>
        <h2>Your Orders</h2>
      </div>

      {orders.length === 0 ? (
        <div className="empty-orders">
          <p>You haven't placed any orders yet.</p>
        </div>
      ) : (
        <div className="orders-list">
          {orders.map(order => (
            <div key={order.id} className="order-card" onClick={() => toggleExpand(order.id)}>
              <div className="order-card-header">
                <div className="order-meta">
                  <span className="order-id">Order #{order.id}</span>
                  <span className="order-date">{new Date(order.createdAt).toLocaleDateString('en-US', { year: 'numeric', month: 'long', day: 'numeric' })}</span>
                </div>
                <div className="order-summary-right">
                  <span className="order-status" style={{ backgroundColor: getStatusColor(order.status) }}>
                    {order.status}
                  </span>
                  <span className="order-total">${parseFloat(order.totalAmount).toFixed(2)}</span>
                  <span className="expand-icon">{expandedOrderId === order.id ? '▲' : '▼'}</span>
                </div>
              </div>

              {expandedOrderId === order.id && (
                <div className="order-details" onClick={e => e.stopPropagation()}>
                  <div className="order-address">
                    <strong>Shipping to:</strong> {order.shippingAddress}
                  </div>
                  <table className="order-items-table">
                    <thead>
                      <tr>
                        <th></th>
                        <th>Product</th>
                        <th>Price</th>
                        <th>Qty</th>
                        <th>Total</th>
                      </tr>
                    </thead>
                    <tbody>
                      {order.items.map(item => (
                        <tr key={item.id}>
                          <td className="item-thumb">
                            {item.thumbnailUrl ? <img src={item.thumbnailUrl} alt={item.productName} /> : '—'}
                          </td>
                          <td>{item.productName}</td>
                          <td>${parseFloat(item.productPrice).toFixed(2)}</td>
                          <td>{item.quantity}</td>
                          <td>${parseFloat(item.itemTotal).toFixed(2)}</td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                  {(order.status === 'PENDING' || order.status === 'CONFIRMED') && (
                    <div style={{ marginTop: '16px', textAlign: 'right' }}>
                      <Button variant="outline" onClick={(e) => handleCancel(e, order.id)} style={{ color: '#d9534f', borderColor: '#d9534f' }}>
                        Cancel Order
                      </Button>
                    </div>
                  )}
                </div>
              )}
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
