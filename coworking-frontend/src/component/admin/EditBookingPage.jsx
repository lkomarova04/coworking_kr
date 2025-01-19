import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import ApiService from '../../service/ApiService'; // Assuming your service is in a file called ApiService.js

const EditBookingPage = () => {
    const navigate = useNavigate();
    const { bookingCode } = useParams();
    const [bookingDetails, setBookingDetails] = useState(null); // State variable for booking details
    const [error, setError] = useState(null); // Track any errors
    const [success, setSuccessMessage] = useState(null); // Track any errors



    useEffect(() => {
        const fetchBookingDetails = async () => {
            try {
                const response = await ApiService.getBookingByConfirmationCode(bookingCode);
                setBookingDetails(response.booking);
            } catch (error) {
                setError(error.message);
            }
        };

        fetchBookingDetails();
    }, [bookingCode]);


    const acheiveBooking = async (bookingId) => {
        if (!window.confirm('Вы уверены что хотите отменить бронирование?')) {
            return; 
        }

        try {
            const response = await ApiService.cancelBooking(bookingId);
            if (response.statusCode === 200) {
                setSuccessMessage("Бронирование удалено")
                
                setTimeout(() => {
                    setSuccessMessage('');
                    navigate('/admin/manage-bookings');
                }, 3000);
            }
        } catch (error) {
            setError(error.response?.data?.message || error.message);
            setTimeout(() => setError(''), 5000);
        }
    };

    return (
        <div className="find-booking-page">
            <h2>Детали бронирования</h2>
            {error && <p className='error-message'>{error}</p>}
            {success && <p className='success-message'>{success}</p>}
            {bookingDetails && (
                <div className="booking-details">
                    <h3>Детали бронирования</h3>
                    <p>Код подтверждения: {bookingDetails.bookingConfirmationCode}</p>
                    <p>Начало брони: {bookingDetails.checkInDate}</p>
                    <p>Конец брони: {bookingDetails.checkOutDate}</p>
                    <p>Email: {bookingDetails.guestEmail}</p>

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
                    <h3>Детали пространства</h3>
                    <div>
                        <p> Тип пространства: {bookingDetails.room.roomType}</p>
                        <p> Цена: ${bookingDetails.room.roomPrice}</p>
                        <p> Описание: {bookingDetails.room.roomDescription}</p>
                        <img src={bookingDetails.room.roomPhotoUrl} alt="" sizes="" srcSet="" />
                    </div>
                    <button
                        className="acheive-booking"
                        onClick={() => acheiveBooking(bookingDetails.id)}>Удалить
                    </button>
                </div>
            )}
        </div>
    );
};

export default EditBookingPage;
