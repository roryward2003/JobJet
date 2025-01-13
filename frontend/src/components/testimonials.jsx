import React from "react"
import Cookies from "js-cookie"

export const Testimonials = (props) => {
  const alertToken = () => {
    alert(Cookies.get('token'))
  }
  const logOut = () => {
    Cookies.set('token', '', '')
  }
  return (
    <div id="testimonials">
      <div className="container">
        <div className="section-title text-center">
          <h2>Account Details</h2>
        </div>
        <button type="button" onClick={alertToken}>
          DEBUG: Alert current auth token
        </button>
        <button type="button" onClick={logOut}>
          DEBUG: Log out
        </button>
        <div className="row">
          {props.data
            ? props.data.map((d, i) => (
                <div key={`${d.name}-${i}`} className="col-md-4">
                  <div className="testimonial">
                    <div className="testimonial-content">
                      <p>"{d.text}"</p>
                      <div className="testimonial-meta"> - {d.name} </div>
                    </div>
                  </div>
                </div>
              ))
            : "loading"}
        </div>
      </div>
    </div>
  );
};
