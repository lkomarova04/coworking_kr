import React, { useState } from 'react';
import ApiService from '../../service/ApiService'; // Assuming your service is in a file called ApiService.js

const FindBookingPage = () => {
    const [confirmationCode, setConfirmationCode] = useState(''); // State variable for confirmation code
    const [bookingDetails, setBookingDetails] = useState(null); // State variable for booking details
    const [error, setError] = useState(null); // Track any errors

    const handleSearch = async () => {
        if (!confirmationCode.trim()) {
            setError("Please Enter a booking confirmation code");
            setTimeout(() => setError(''), 5000);
            return;
        }
        try {
            // Call API to get booking details
            const response = await ApiService.getBookingByConfirmationCode(confirmationCode);
            setBookingDetails(response.booking);
            setError(null); // Clear error if successful
        } catch (error) {
            setError(error.response?.data?.message || error.message);
            setTimeout(() => setError(''), 5000);
        }
    };

    return (
        <div className="find-booking-page">
            <h2>Найти бронирование</h2>
            <div className="search-container">
                <input
                    required
                    type="text"
                    placeholder="Введите ваш код бронирования"
                    value={confirmationCode}
                    onChange={(e) => setConfirmationCode(e.target.value)}
                />
                <button onClick={handleSearch}>Найти</button>
            </div>
            {error && <p style={{ color: 'red' }}>{error}</p>}
            {bookingDetails && (
                <div className="booking-details">
                    <h3>Детали бронирования</h3>
                    <p>Код подтверждения: {bookingDetails.bookingConfirmationCode}</p>
                    <p>Начало брони: {bookingDetails.checkInDate}</p>
                    <p>Конец брони: {bookingDetails.checkOutDate}</p>

                    <br />
                    <hr />
                    <br />
                    <h3>Контактные данные</h3>
                    <div>
                        <p> Имя: {bookingDetails.user.name}</p>
                        <p> Email: {bookingDetails.user.email}</p>
                        <p> Телефон: {bookingDetails.user.phoneNumber}</p>
                    </div>

                    <br />
                    <hr />
                    <br />
                    <h3>Детали пространтсва</h3>
                    <div>
                        <p> Тип команты: {bookingDetails.room.roomType}</p>
                        <img src={bookingDetails.room.roomPhotoUrl} alt="" sizes="" srcSet="" />
                    </div>
                </div>
            )}
        </div>
    );
};

export default FindBookingPage;
