import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

export const SignUp = () => {
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    password: "",
    jobInterest: "",
    areaInterest: "",
    employmentType: "",
  });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = (e) => {
    e.preventDefault()
    setLoading(true)
    setError("")
    if(!/^(?=.*[A-Z])(?=.*\d)[a-zA-Z\d\w\W]{8,}$/.test(formData.password)) {
      setError("Password must be at least 8 digits long, and contain at least 1 uppercase letter and 1 number")
      setLoading(false)
      throw error
    }
    fetch('https://localhost:8443/auth/signup', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ 
        username: formData.email, 
        password: formData.password, 
        name: formData.name,
        keywords: formData.jobInterest,
        location: formData.areaInterest,
        jobType: formData.employmentType
      }),
    })
    .then(response => {
      if(response.ok) {
        alert("Sign up successful!")
        navigate("/login")
      } else {
        setError("Username has been taken already, please try again")
      }
      setLoading(false)
    })
    .catch(error => {
      setLoading(false)
      setError("An unexpected error occured")
      throw error
    })
  };

  return (
    <div className="sign-up-container">
      <h2>Sign Up</h2>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>Name:</label>
          <input
            type="text"
            name="name"
            value={formData.name}
            onChange={handleChange}
            required
          />
        </div>
        <div className="form-group">
          <label>Email:</label>
          <input
            type="email"
            name="email"
            value={formData.email}
            onChange={handleChange}
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
            required
          />
        </div>
        <div className="form-group">
          <label>Job search keywords:</label>
          <input
            type="text"
            name="jobInterest"
            value={formData.jobInterest}
            onChange={handleChange}
            required
          />
        </div>
        <div className="form-group">
          <label>Location for job search:</label>
          <input
            type="text"
            name="areaInterest"
            value={formData.areaInterest}
            onChange={handleChange}
            required
          />
        </div>
        <div className="form-group">
          <label>Employment Type:</label>
          <select
            name="employmentType"
            value={formData.employmentType}
            onChange={handleChange}
            required
          >
            <option value="" disabled>
              Select an option
            </option>
            <option value="full-time">Full-Time</option>
            <option value="part-time">Part-Time</option>
            <option value="internship">Internship</option>
            <option value=" ">Any</option>
          </select>
        </div>
        {error && <p className="error-message">{error}</p>}
        <button type="submit" disabled={loading}>
          {loading ? "Signing up..." : "Sign Up"}
        </button>
      </form>
    </div>
  );
};
