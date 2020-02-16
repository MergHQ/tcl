import React from 'react'

const AttendeeList = ({ attendees }) =>
  <div className="attendee-container">
    {attendees.map(attendee => <AttendeeItem attendee={attendee} />)}
  </div>

const AttendeeItem = ({ attendee }) => {
  return (
    <div className="attendee-item">
      <img className="attendee-pic" src={attendee.profile_image_url} />
      <a href={`https://twitch.tv/${attendee.login}`} className="attendee-header">{attendee.display_name}</a>
    </div>
  )
}

export default AttendeeList
