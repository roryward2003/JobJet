import React, { useState, useEffect } from "react";
import { useSwipeable } from "react-swipeable";
import Cookies from "js-cookie";

export const justAnAlert = () => {
  alert('hello');
};

export const JobPortal = (props) => {
  const [currentIndex, setCurrentIndex] = useState(0);
  const cleanHTML = (html) => {
    const tempElement = document.createElement("div");
    tempElement.innerHTML = html; // Decode HTML entities
    return tempElement.textContent || tempElement.innerText || "";
  };
  const handleSwipe = async (action) => {
    try {
      if (action === "choose" && props.data[currentIndex]) {
        // Save the job using the onSaveJob prop passed from App.js
        props.onSaveJob(props.data[currentIndex]);
        await fetch('http://localhost:8080/jobjet/users/'+Cookies.get('username')+'/'+props.data[currentIndex].id, {
          method: 'PUT',
          headers: {
            'Authorization': "Bearer "+Cookies.get('token'),
          },
          credentials: 'include',
        })
      } else if (action === "reject" && props.data[currentIndex]) {
        await fetch('http://localhost:8080/jobjet/users/reject/'+Cookies.get('username')+'/'+props.data[currentIndex].id, {
          method: 'PUT',
          headers: {
            'Authorization': "Bearer "+Cookies.get('token'),
          },
          credentials: 'include',
        })
      }
      props.onRemove()
    } catch (error) {
      alert("Error: "+error)
    }
  };

  // Handle keyboard events
  useEffect(() => {
    const handleKeyPress = (event) => {
      if (event.key === "ArrowRight") {
        // Right arrow key for choosing
        handleSwipe("choose");
      } else if (event.key === "ArrowLeft") {
        // Left arrow key for rejecting
        handleSwipe("reject");
      }
    };

    window.addEventListener("keydown", handleKeyPress);
    return () => {
      window.removeEventListener("keydown", handleKeyPress);
    };
  }, [currentIndex, props.data]); // Add dependencies to update the listener when data changes

  const swipeHandlers = useSwipeable({
    onSwipedLeft: () => handleSwipe("reject"),
    onSwipedRight: () => handleSwipe("choose"),
    preventDefaultTouchmoveEvent: true,
    trackMouse: true,
  });


  return (
    <div id="jobportal" className="text-center">
      <div className="container">
        <div className="section-title">
          <h2>Job Portal</h2>
          <p>Swipe to explore jobs or choose/reject below!</p>
        </div>

        {props.data && props.data.length > 0 ? (
          <div {...swipeHandlers} className="job-card-container">
            <div className="job-card">
              <div className="job-card-header">
                <h3>{props.data[currentIndex].companyName}</h3>
              </div>
              <div className="job-card-body">
                <h4>{props.data[currentIndex].title}</h4>
              </div>
              <div className="job-card-body">
                <p>{props.data[currentIndex].location}</p>
              </div>
              <div className="job-card-body">
                <p>{cleanHTML(props.data[currentIndex].description)}</p>
              </div>
            </div>
            <div className="action-buttons">
                <i
                className="fa fa-times-circle action-icon reject"
                aria-hidden="true"
                onClick={() => handleSwipe("reject")}
                ></i>
              <i
                className="fa fa-heart action-icon choose"
                aria-hidden="true"
                onClick={() => handleSwipe("choose")}
              ></i>
            </div>
          </div>
        ) : (
          <p>Loading jobs...</p>
        )}
      </div>
    </div>
  );
};
