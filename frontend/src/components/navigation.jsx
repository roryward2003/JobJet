import React from "react";
import Cookies from "js-cookie";
import { Link } from "react-router-dom";
export const Navigation = (props) => {
  const isAuthenticated = Cookies.get("token") !== null && Cookies.get("token") !== "";
  return (
    <nav id="menu" className="navbar navbar-default navbar-fixed-top">
      <div className="container-fluid">
        <div className="navbar-header" style={{ position: "relative", top: "-20px" }}>
          <button
            type="button"
            className="navbar-toggle collapsed"
            data-toggle="collapse"
            data-target="#bs-example-navbar-collapse-1"
          >
            {" "}
            <span className="sr-only">Toggle navigation</span>{" "}
            <span className="icon-bar"></span>{" "}
            <span className="icon-bar"></span>{" "}
            <span className="icon-bar"></span>{" "}
          </button>
          <img src="img/portfolio/JOBJET.jpg" style={{height: "110px",margin: "0 10px 0px 0",display: "inline-block"}} alt="Logo" className="navbar-logo"/>
          <Link to= "/header" className="navbar-brand page-scroll" href="#page-top">
            JobJet
          </Link>
        </div>

        <div
          className="collapse navbar-collapse"
          id="bs-example-navbar-collapse-1"
        >
          <ul className="nav navbar-nav navbar-right">
            <li>
              <Link to="/jobportal" className="page-scroll">
                Job Portal
              </Link>
            </li>
            <li>
              <Link to="/savejob" className="page-scroll">
                Saved Jobs
              </Link>
            </li>
            {!props.isAuthenticated ? (
              <li>
                <Link to="/login" className="page-scroll">
                  Log In
                </Link>
              </li>
            ) : (
              <li>
                <Link to="/accountdets" className="page-scroll">
                  Account Details
                </Link>
              </li>
            )}
          </ul>
        </div>
      </div>
    </nav>
  );
};
