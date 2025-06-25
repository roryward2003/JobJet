import Cookies from "js-cookie"

export const getUserData = async (onSaveJob, onSetJob, resetJobs) => {
    if(Cookies.get('token')!==null && Cookies.get('token')!=="") {
        resetJobs()
        const savedJobs = await fetch('https://localhost:8443/jobjet/jobs', {  
            method: 'GET',
            headers: {
            'Authorization': "Bearer "+Cookies.get('token'),
            },
            credentials: 'include',
        })
        const savedJobData = await savedJobs.json()
        savedJobData.map((job) => onSaveJob(job))
    
        const allJobs = await fetch('https://localhost:8443/jobjet/users/jobs', {
            method: 'GET',
            headers: {
            'Authorization': "Bearer "+Cookies.get('token'),
            },
            credentials: 'include',
        })
        const allJobData = await allJobs.json()
        allJobData.map((job) => onSetJob(job))
    }
};