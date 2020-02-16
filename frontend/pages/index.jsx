import React, { useEffect, useState } from 'react'
import AttendeeList from './components/AttendeeList'

export default function App() {
  const [attendees, setAttendees] = useState([])
  useEffect(() => {
    fetch('http://localhost:8080/users')
      .then(response => response.json())
      .then(setAttendees)
  }, [])


  return (
  <div className="container">
    <h1 className="title">TwitchCon better attendee list</h1>
    <AttendeeList attendees={attendees} />
  </div>
  )
}