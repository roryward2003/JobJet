import React from "react";
import { Link } from "react-router-dom";

export const Header = (props) => {
  if (!props.data) {
    console.warn("Header component received no data");
  }
  return (
    <div id="header">
      <div className="circle-container">
       <img src="/circle.jpg" alt="Decorative Circle" className="circle-image" />
      </div>
    <header id="header">
    <div className="intro">
    <div className="container">
      <div className="intro-text">
        <h1>
          Welcome to <span style={{ color: "pink" }}>JOBJET</span>
        </h1>
        <p>Swipe your way to your dream career!</p>
        <Link to="/features" className="btn btn-custom btn-lg">
          Learn More
        </Link>
        </div>
        </div>
      </div>
    </header>
    </div>

  );
};

