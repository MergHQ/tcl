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
    <button onClick={() => {
      setAttendees([...attendees].sort((a, b) => b.view_count - a.view_count))
    }}>Sort viewer count asc</button>
    <AttendeeList attendees={attendees} />
  </div>
  )
}