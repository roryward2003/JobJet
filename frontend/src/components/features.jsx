import React from "react";
import { Link } from "react-router-dom";
export const Features = (props) => {
  return (
    <div id="features" className="text-center">
      <div className="container">
        <div className="col-md-10 col-md-offset-1 section-title">
          <h2>How It works</h2>
        </div>
        <div className="row">
        {props.data
            ? props.data.map((d, i) => (
                <div key={`${d.name}-${i}`} className="col-md-4">
                  {" "}
                  <Link to={d.link}>
                  <i className={d.icon}></i>
                  </Link>
                  <div className="service-desc">
                    <h3>{d.name}</h3>
                    <p
                    dangerouslySetInnerHTML={{
                    __html: d.text.replace(/\n/g, '<br />')
                    }}
                  ></p>
                  </div>
                </div>
              ))
            : "loading"}
        </div>
      </div>
    </div>
  );
};
