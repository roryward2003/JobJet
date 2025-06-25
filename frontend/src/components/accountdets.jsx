import React, { useState } from "react";
import Cookies from "js-cookie";
import { useNavigate } from "react-router-dom";
import { getUserData } from "./getuserdata";

export const AccDets = (props) => {
  const [formData, setFormData] = useState({
    keywords: "",
    location: "",
    jobType: ""
  });
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);

  const logOut = async () => {
    Cookies.set("token", "", { expires: -1, secure: false });
    props.resetJobs()
    props.logOut()
    navigate("/header");
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      await fetch('http://localhost:8080/jobjet/users/params', {
        method: 'POST',
        headers: {
          'Authorization': 'Bearer '+Cookies.get('token'),
          'Content-Type': 'application/json'
        },
        credentials: 'include',
        body: JSON.stringify({ keywords: formData.keywords, location: formData.location, jobType: formData.jobType }),
      })
      await getUserData(props.onSaveJob, props.onSetJob, props.resetJobs)
      setLoading(false)
    } catch (error) {
      setLoading(false)
      alert("Error submitting changes")
    }
  };

  const resetRejectedJobs = async () => {
    if(Cookies.get('token')!==null && Cookies.get('username')!==null) {
      await fetch('http://localhost:8080/jobjet/users/reject/'+Cookies.get('username'), {
        method: 'DELETE',
        headers: {
        'Authorization': "Bearer "+Cookies.get('token'),
        },
        credentials: 'include',
      })
      window.location.reload()
    } else {
      alert("You are not logged in");
    }
  }

  return (
    <div id="account-details">
      <div className="container">
        <div className="section-title">
          <h2>Account Details</h2>
        </div>
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Job search keywords:</label>
            <input
              type="text"
              name="keywords"
              value={formData.jobInterest}
              onChange={handleChange}
              className="form-control"
              placeholder="Enter job keywords"
              required
            />
          </div>
          <div className="form-group">
            <label>Location for job search:</label>
            <input
              type="text"
              name="location"
              value={formData.areaInterest}
              onChange={handleChange}
              className="form-control"
              placeholder="Enter location"
              required
            />
          </div>
          <div className="form-group">
            <label>Employment Type:</label>
            <select
              name="jobType"
              value={formData.employmentType}
              onChange={handleChange}
              className="form-control"
              required
            >
              <option value="" disabled>
                Select an option
              </option>
              <option value="full-time">Full-Time</option>
              <option value="part-time">Part-Time</option>
              <option value="internship">Internship</option>
              <option value="any">Any</option>
            </select>
          </div>
          <button type="submit" className="btn btn-primary" disabled={loading}>
            {loading ? "Submitting..." : "Submit"}
          </button>
        </form>
        <div className="account-buttons">
          <button className="btn btn-info" onClick={resetRejectedJobs}>
            Clear Rejected Jobs
          </button>
          <button className="btn btn-danger" onClick={logOut}>
            Log Out
          </button>
        </div>
      </div>
    </div>
  );
};
