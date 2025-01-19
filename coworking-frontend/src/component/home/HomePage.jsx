import React, { useState } from "react";
import RoomResult from "../common/RoomResult";
import RoomSearch from "../common/RoomSearch";

const HomePage = () => {
    const [roomSearchResults, setRoomSearchResults] = useState([]);

    // Функция для обработки результатов поиска
    const handleSearchResult = (results) => {
        setRoomSearchResults(results);
    };

    return (
        <div className="home">
            {/* HEADER / BANNER ROOM SECTION */}
            <section>
                <header className="header-banner">
                    <img src="./assets/images/home.jpg" alt="Phegon Hotel" className="header-image" />
                    <div className="overlay"></div>
                    <div className="animated-texts overlay-content">
                        <h1>
                            Добро пожаловать в <span className="phegon-color">Cowork</span>
                        </h1><br />
                    </div>
                </header>
            </section>

            {/* SEARCH/FIND AVAILABLE ROOM SECTION */}
            <RoomSearch handleSearchResult={handleSearchResult} />
            <RoomResult roomSearchResults={roomSearchResults} />

            <h4><a className="view-rooms-home" href="/rooms">Все комнаты</a></h4>

            <h2 className="home-services">Разработчик <span className="phegon-color">Cowork</span></h2>

            {/* Секция о разработчике */}
            <section className="developer-section">
                <div className="developer-info">
                    <img src="./assets/images/developer.jpg" alt="Developer Photo" className="developer-photo" />
                    <div className="developer-details">
                        <h3 className="developer-name">Комарова Любовь Андреевна</h3>
                        <p className="developer-group">Группа: ИКБО-13-22</p>
                        <p className="developer-email">Почта: <a href="mailto:lkomarova04@yandex.ru">lkomarova04@yandex.ru</a></p>
                        <p className="developer-phone">Телефон: <a href="tel:+789027212087">(902) 721-20-87</a></p>
                    </div>
                </div>
            </section>
            <section></section>
        </div>
    );
}

export default HomePage;
