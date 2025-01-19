import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import ApiService from '../../service/ApiService';

const ProfilePage = () => {
    const [user, setUser] = useState(null);
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchUserProfile = async () => {
            try {
                const response = await ApiService.getUserProfile();
                // Fetch user bookings using the fetched user ID
                const userPlusBookings = await ApiService.getUserBookings(response.user.id);
                setUser(userPlusBookings.user)

            } catch (error) {
                setError(error.response?.data?.message || error.message);
            }
        };

        fetchUserProfile();
    }, []);

    const handleLogout = () => {
        ApiService.logout();
        navigate('/home');
    };

    const handleEditProfile = () => {
        navigate('/edit-profile');
    };

    return (
        <div className="profile-page">
            {user && <h2>Здраствуйте, {user.name}</h2>}
            <div className="profile-actions">
                <button className="edit-profile-button" onClick={handleEditProfile}>Изменить профиль</button>
                <button className="logout-button" onClick={handleLogout}>Выйти</button>
            </div>
            {error && <p className="error-message">{error}</p>}
            {user && (
                <div className="profile-details">
                    <h3>Детали моего профиля</h3>
                    <p><strong>Email:</strong> {user.email}</p>
                    <p><strong>Телефон:</strong> {user.phoneNumber}</p>
                </div>
            )}
            <div className="bookings-section">
                <h3>История бронирования</h3>
                <div className="booking-list">
                    {user && user.bookings.length > 0 ? (
                        user.bookings.map((booking) => (
                            <div key={booking.id} className="booking-item">
                                <p><strong>Код бронирования:</strong> {booking.bookingConfirmationCode}</p>
                                <p><strong>Дата начала:</strong> {booking.checkInDate}</p>
                                <p><strong>Дата окончания:</strong> {booking.checkOutDate}</p>
                                <p><strong>Тип пространства:</strong> {booking.room.roomType}</p>
                                <img src={booking.room.roomPhotoUrl} alt="Room" className="room-photo" />
                            </div>
                        ))
                    ) : (
                        <p>Бронирования не найдены.</p>
                    )}
                </div>
            </div>
        </div>
    );
};

export default ProfilePage;
