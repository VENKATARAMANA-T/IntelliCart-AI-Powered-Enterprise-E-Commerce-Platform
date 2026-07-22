import React from 'react';
import '../../styles/components.css';

export function Button({ 
  children, 
  variant = 'primary', 
  type = 'button', 
  disabled = false, 
  onClick, 
  className = '' 
}) {
  return (
    <button 
      type={type}
      className={`btn btn-${variant} ${className}`}
      disabled={disabled}
      onClick={onClick}
    >
      {children}
    </button>
  );
}
