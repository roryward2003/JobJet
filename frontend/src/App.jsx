import React, { useState, useEffect } from "react";
import { Navigation } from "./components/navigation";
import { Header } from "./components/header";
import { Features } from "./components/features";
import { JobPortal } from "./components/jobportal";
import { SaveJob } from "./components/savejob";
import { AccDets } from "./components/accountdets";
import { SignUp } from "./components/signup";
import { Login } from "./components/login";
import { getUserData } from "./components/getuserdata";
import JsonData from "./data/data.json";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Cookies from "js-cookie";
import "./App.css";

const App = () => {
  const [landingPageData, setLandingPageData] = useState({});
  const [savedJobs, setSavedJobs] = useState([]); // Define the saved jobs state
  const [allJobs, setAllJobs] = useState([]); // Define the saved jobs state
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  const onSaveJob = (job) => { setSavedJobs((prevJobs) => [...prevJobs, job]) };
  const onSetJob = (job) => { setAllJobs((prevJobs) => [...prevJobs, job]) };
  const resetJobs = () => { setSavedJobs(() => []); setAllJobs(() => []) }
  const onDeleteJob = (jobId) => {
    setSavedJobs((prevJobs) => prevJobs.filter((job) => job.id !== jobId));
  };


  useEffect(() => {
    const fetchData = async () => {
      // Call getUserData when the component mounts or page is refreshed
      await getUserData(onSaveJob, onSetJob, resetJobs);
    };

    if(Cookies.get('token')!==null && Cookies.get('token')!== "") {
      setIsAuthenticated(true);
      fetchData();
    }
    setLandingPageData(JsonData);
    setIsAuthenticated(Cookies.get('token') ? true : false);
  }, []);

  return (
    <Router>
      <Navigation isAuthenticated={isAuthenticated} setIsAuthenticated={setIsAuthenticated}/>
      <div className="app-container">
        <Routes>
        <Route path="/" element={<Header data={landingPageData.Header} />} />
        <Route path="/header" element={<Header data={landingPageData.Header} />} />
        <Route
          path="/features"
          element={<Features data={landingPageData.Features} />}
        />
        <Route path="/jobportal" element={ <JobPortal data={allJobs}
            onSaveJob={(job) => setSavedJobs((prevJobs) => [...prevJobs, job])}
            onRemove={() => setAllJobs((prevJobs) => [...prevJobs.slice(1)])}
          />}
        />
          <Route path="/savejob" element={<SaveJob 
            data={savedJobs}
            onDeleteJob={onDeleteJob}
          />} />
          <Route
            path="/accountdets"
            element={<AccDets 
              data={landingPageData.AccDets}
              savedJobs={savedJobs}
                onSaveJob={onSaveJob}
              allJobs={allJobs}
                onSetJob={onSetJob}
              resetJobs={resetJobs}
              isAuthenticated={isAuthenticated}logOut={() => setIsAuthenticated(false)}
            />}
          />
          <Route
            path="/signup"
            element={<SignUp data={landingPageData.contact} />}
          />
          <Route path="/login" 
            element={<Login 
              savedJobs={savedJobs}onSaveJob={onSaveJob}
              allJobs={allJobs}onSetJob={onSetJob}
              resetJobs={resetJobs}
              setIsAuthenticated={setIsAuthenticated}
            />}
          />
        </Routes>
      </div>
    </Router>
  );
};

export default App;
