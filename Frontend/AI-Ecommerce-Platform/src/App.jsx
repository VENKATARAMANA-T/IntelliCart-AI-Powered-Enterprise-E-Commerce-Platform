import { useState } from 'react'
import './App.css'
import { SellerApp } from './components/Seller/SellerApp'
import { CustomerApp } from './components/Customer/CustomerApp'

const AUTH_ENDPOINTS = {
  register: 'http://localhost:8090/auth-service/auth/register',
  login: 'http://localhost:8090/auth-service/auth/login',
  logout: 'http://localhost:8090/auth-service/auth/logout',
}

const initialRegisterForm = {
  firstName: '',
  lastName: '',
  username: '',
  email: '',
  password: '',
  phone: '',
  role: 'CUSTOMER',
}

const initialLoginForm = {
  usernameOrEmail: '',
  password: '',
}

function createFlash(type, title, message, status) {
  return {
    type,
    title,
    message,
    status,
  }
}

function resolveApiMessage(payload, fallbackMessage) {
  if (payload && typeof payload === 'object') {
    if (typeof payload.message === 'string' && payload.message.trim()) {
      return payload.message
    }

    if (typeof payload.error === 'string' && payload.error.trim()) {
      return payload.error
    }
  }

  return fallbackMessage
}

async function requestAuth(endpoint, payload) {
  const response = await fetch(endpoint, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    credentials: 'include',
    body: JSON.stringify(payload),
  })

  const responseData = await response.json().catch(() => null)

  if (!response.ok) {
    throw createFlash(
      'error',
      responseData?.error || 'Request failed',
      resolveApiMessage(responseData, 'Please check your details and try again.'),
      response.status,
    )
  }

  return {
    status: response.status,
    data: responseData,
  }
}

function App() {
  const [page, setPage] = useState('register')
  const [registerForm, setRegisterForm] = useState(initialRegisterForm)
  const [loginForm, setLoginForm] = useState(initialLoginForm)
  const [user, setUser] = useState(null)
  const [flash, setFlash] = useState(null)
  const [isSubmitting, setIsSubmitting] = useState(false)

  const switchToLogin = () => {
    setPage('login')
  }

  const handleRegisterChange = (event) => {
    const { name, value } = event.target
    setRegisterForm((current) => ({
      ...current,
      [name]: value,
    }))
  }

  const handleLoginChange = (event) => {
    const { name, value } = event.target
    setLoginForm((current) => ({
      ...current,
      [name]: value,
    }))
  }

  const handleRegisterSubmit = async (event) => {
    event.preventDefault()
    setIsSubmitting(true)
    setFlash(null)

    try {
      const payload = {
        ...registerForm,
        firstName: registerForm.firstName.trim(),
        lastName: registerForm.lastName.trim(),
        username: registerForm.username.trim(),
        email: registerForm.email.trim(),
        phone: registerForm.phone.trim(),
      }

      const response = await requestAuth(AUTH_ENDPOINTS.register, payload)
      setFlash(
        createFlash(
          'success',
          'Registration successful',
          resolveApiMessage(response.data, 'Account created successfully. Please sign in.'),
          response.status,
        ),
      )
      setPage('login')
      setLoginForm((current) => ({
        ...current,
        usernameOrEmail: payload.username,
      }))
      setRegisterForm(initialRegisterForm)
    } catch (error) {
      if (error?.type === 'error') {
        setFlash(error)
      } else {
        setFlash(
          createFlash(
            'error',
            'Network error',
            'Unable to reach the server. Please ensure backend services are running.',
          ),
        )
      }
    } finally {
      setIsSubmitting(false)
    }
  }

  const handleLoginSubmit = async (event) => {
    event.preventDefault()
    setIsSubmitting(true)
    setFlash(null)

    try {
      const payload = {
        usernameOrEmail: loginForm.usernameOrEmail.trim(),
        password: loginForm.password,
      }

      const response = await requestAuth(AUTH_ENDPOINTS.login, payload)
      setUser({
        userId: response.data?.userId,
        username: response.data?.username,
        role: response.data?.role,
      })
      setPage('home')
      setLoginForm(initialLoginForm)
      setFlash(
        createFlash(
          'success',
          'Login successful',
          resolveApiMessage(response.data, 'Welcome back to IntelliCart.'),
          response.status,
        ),
      )
    } catch (error) {
      if (error?.type === 'error') {
        setFlash(error)
      } else {
        setFlash(
          createFlash(
            'error',
            'Network error',
            'Unable to reach the server. Please ensure backend services are running.',
          ),
        )
      }
    } finally {
      setIsSubmitting(false)
    }
  }

  const handleLogout = async () => {
    setIsSubmitting(true)
    setFlash(null)

    try {
      const response = await requestAuth(AUTH_ENDPOINTS.logout, {})
      setUser(null)
      setPage('login')
      setFlash(
        createFlash(
          'success',
          'Logged out',
          resolveApiMessage(response.data, 'You have been logged out successfully.'),
          response.status,
        ),
      )
    } catch (error) {
      if (error?.type === 'error') {
        setFlash(error)
      } else {
        setFlash(
          createFlash(
            'error',
            'Network error',
            'Unable to reach the server. Please ensure backend services are running.',
          ),
        )
      }
    } finally {
      setIsSubmitting(false)
    }
  }

  if (page === 'home' && user) {
    if (user.role === 'SELLER') {
      return <SellerApp user={user} onLogout={handleLogout} />
    }
    return <CustomerApp user={user} onLogout={handleLogout} />
  }

  return (
    <main className="app-shell">
      <section className="auth-card">
        <header className="card-header">
          <p className="eyebrow">IntelliCart</p>
          <h1>Authentication</h1>
          <p className="subtitle">
            {page === 'register' &&
              'Create your account to start your clean and secure shopping journey.'}
            {page === 'login' && 'Sign in to continue to your ecommerce dashboard.'}
          </p>
        </header>

        {flash && (
          <div className={`flash flash-${flash.type}`} role="status" aria-live="polite">
            <strong>{flash.title}</strong>
            <span>{flash.message}</span>
            {flash.status && <small>Status: {flash.status}</small>}
          </div>
        )}

        {page === 'register' && (
          <form className="form-grid" onSubmit={handleRegisterSubmit}>
            <label>
              First Name
              <input
                name="firstName"
                type="text"
                value={registerForm.firstName}
                onChange={handleRegisterChange}
                required
              />
            </label>
            <label>
              Last Name
              <input
                name="lastName"
                type="text"
                value={registerForm.lastName}
                onChange={handleRegisterChange}
              />
            </label>
            <label>
              Username
              <input
                name="username"
                type="text"
                value={registerForm.username}
                onChange={handleRegisterChange}
                required
              />
            </label>
            <label>
              Email
              <input
                name="email"
                type="email"
                value={registerForm.email}
                onChange={handleRegisterChange}
                required
              />
            </label>
            <label>
              Password
              <input
                name="password"
                type="password"
                value={registerForm.password}
                onChange={handleRegisterChange}
                required
              />
            </label>
            <label>
              Phone
              <input
                name="phone"
                type="tel"
                value={registerForm.phone}
                onChange={handleRegisterChange}
              />
            </label>
            <label className="full-width">
              Role
              <select name="role" value={registerForm.role} onChange={handleRegisterChange}>
                <option value="CUSTOMER">Customer</option>
                <option value="SELLER">Seller</option>
              </select>
            </label>
            <div className="action-row full-width">
              <button type="submit" className="btn btn-primary" disabled={isSubmitting}>
                {isSubmitting ? 'Creating account...' : 'Register'}
              </button>
              <button type="button" className="btn btn-secondary" onClick={switchToLogin}>
                Go to Login
              </button>
            </div>
          </form>
        )}

        {page === 'login' && (
          <form className="form-stack" onSubmit={handleLoginSubmit}>
            <label>
              Username or Email
              <input
                name="usernameOrEmail"
                type="text"
                value={loginForm.usernameOrEmail}
                onChange={handleLoginChange}
                required
              />
            </label>
            <label>
              Password
              <input
                name="password"
                type="password"
                value={loginForm.password}
                onChange={handleLoginChange}
                required
              />
            </label>
            <div className="action-row">
              <button type="submit" className="btn btn-primary" disabled={isSubmitting}>
                {isSubmitting ? 'Signing in...' : 'Login'}
              </button>
              <button
                type="button"
                className="btn btn-secondary"
                onClick={() => setPage('register')}
              >
                Create Account
              </button>
            </div>
          </form>
        )}

      </section>
    </main>
  )
}

export default App
