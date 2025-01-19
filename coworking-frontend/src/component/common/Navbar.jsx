import React from 'react';
import { NavLink, useNavigate } from 'react-router-dom';
import ApiService from '../../service/ApiService';

function Navbar() {
    const isAuthenticated = ApiService.isAuthenticated();
    const isAdmin = ApiService.isAdmin();
    const isUser = ApiService.isUser();
    const navigate = useNavigate();

    const handleLogout = () => {
        const isLogout = window.confirm('Вы уверены, что хотите выйти из учетной записи?');
        if (isLogout) {
            ApiService.logout();
            navigate('/home');
        }
    };

    return (
        <nav className="navbar">
            <div className="navbar-brand">
                <NavLink to="/home">Cowork</NavLink>
            </div>
            <ul className="navbar-ul">
                <li><NavLink to="/home" activeclassname="active">Главная</NavLink></li>
                <li><NavLink to="/rooms" activeclassname="active">Пространства</NavLink></li>
                <li><NavLink to="/find-booking" activeclassname="active">Мои брони</NavLink></li>

                {isUser && <li><NavLink to="/profile" activeclassname="active">Профиль</NavLink></li>}
                {isAdmin && <li><NavLink to="/admin" activeclassname="active">Админ</NavLink></li>}

                {!isAuthenticated &&<li><NavLink to="/login" activeclassname="active">Войти</NavLink></li>}
                {!isAuthenticated &&<li><NavLink to="/register" activeclassname="active">Регистрация</NavLink></li>}
                {isAuthenticated && <li onClick={handleLogout}>Выйти</li>}
            </ul>
        </nav>
    );
}

export default Navbar;
