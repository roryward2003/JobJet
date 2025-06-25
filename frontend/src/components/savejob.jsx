import React from "react";
import { Link } from "react-router-dom";
import { getUserData } from "./getuserdata";
import Cookies from "js-cookie"

export const SaveJob = (props) => {
  
  const handleDelete = async (jobId) => {
    // props.onDeleteJob(jobId); // Call the onDeleteJob function passed as a prop
    await fetch('https://localhost:8443/jobjet/users/'+Cookies.get('username')+'/'+jobId, {
      method: 'DELETE',
      headers: {
        'Authorization': 'Bearer '+Cookies.get('token')
      },
      credentials: 'include',
    })
    props.onDeleteJob(jobId)
  };
  return (
    <div id="portfolio" className="text-center">
      <div className="container">
        <div className="section-title">
          <h2>Saved Jobs</h2>
        </div>
        {/* Show "No saved jobs yet" under the title */}
        {!props.data || props.data.length === 0 ? (
          <div className="no-jobs-message">
            <p>No saved jobs yet. Please go to</p>
          <Link to="/jobportal" className="btn btn-custom-job btn-lg">
          Job Portal
          </Link>
          <p>to discover jobs!</p>
          </div>
        ) : null}

        <div className="row">
          {props.data && props.data.length > 0 ? (
            props.data.map((d,i) => {
              if(d !== null) {
                return <div key={i} className="col-xs-6 col-md-3">
                  <div className="saved-job-card">
                  <i
                      className="fa fa-times-circle delete-icon" title="Delete" onClick={() => handleDelete(d.id)}
                  ></i>
                    <h3>{d.companyName}</h3>
                    <h6>{d.title}</h6>
                    <p>{d.location}</p>
                    <Link to={d.url}>
                      <span className="card" style={{"display": "block"}}>
                        Apply now
                      </span>
                    </Link>
                  </div>
                </div>
              } else {
                return <div key={i} className="col-xs-6 col-md-3">
                  <div className="saved-job-card">
                    <h3>{"Job not found"}</h3>
                    <h6>{""}</h6>
                  </div>
                </div>
              }
            })
          ):null }
        </div>
      </div>
    </div>
  );
};
