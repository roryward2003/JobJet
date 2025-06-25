import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import Cookies from 'js-cookie';
import { getUserData } from "./getuserdata";

export const Login = (props) => {
  const [formData, setFormData] = useState({
    email: "",
    password: "",
  });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };
  
  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError("");
    
    try {
      // Assume you're calling a login API
      const response = await fetch('https://localhost:8443/auth/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ username: formData.email, password: formData.password }),
      });
      const data = await response.json();

      if (response.ok) {
        // Store auth token and username in cookies
        // 0.0416 days = slightly less than one hour
        Cookies.set('username', formData.email, { expires: 0.0416, secure: false });
        Cookies.set('token', data.token, { expires: 0.0416, secure: false });

        await getUserData(props.onSaveJob, props.onSetJob, props.resetJobs)
        props.setIsAuthenticated(true);
        navigate("/jobportal")
      } else {
        setError(data.errorMsg);
      }
    } catch (error) {
      setError("An unexpected error occurred: " + error.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-container">
      <h2>Login</h2>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>Email:</label>
          <input
            type="email"
            name="email"
            value={formData.email}
            onChange={handleChange}
            placeholder="Enter your email"
            required
          />
        </div>
        <div className="form-group">
          <label>Password:</label>
          <input
            type="password"
            name="password"
            value={formData.password}
            onChange={handleChange}
            placeholder="Enter your password"
            required
          />
        </div>
        {error && <p className="error-message">{error}</p>}
        <button type="submit" disabled={loading}>
          {loading ? "Logging in..." : "Login"}
        </button>
      </form>
      <p>
        Don't have an account? <Link to="/signup">Sign Up</Link>
      </p>
    </div>
  );
};
