import React from 'react';
import { Link } from 'react-router-dom';

const BookingResult = ({ bookingSearchResults }) => {
  return (
    <div className="booking-results">
      {bookingSearchResults.map((booking) => (
        <div key={booking.id} className="booking-result-item">
          <p>Коворкинг ID: {booking.roomId}</p>
          <p>Пользователь ID: {booking.userId}</p>
          <p>Дата начала: {booking.checkInDate}</p>
          <p>Конец: {booking.checkOutDate}</p>
          <p>Статус: {booking.status}</p>
          <Link to={`/admin/edit-booking/${booking.id}`} className="edit-link">Изменить</Link>
        </div>
      ))}
    </div>
  );
};


export default BookingResult;
