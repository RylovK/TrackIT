<!-- Не переводить это -->
<details>
<summary>
<strong> Прочитать это руководство на других языках </strong>
</summary>
    <ul>
        <li><a href="./README.md"> English </a></li>
        <li><a href="./README-RU.md"> Русский </a></li>
    </ul>
</details>
<!-- Не переводить это -->

<h1>TrackIT</h1>

<p><strong>TrackIT</strong> — это веб-приложение на Java для отслеживания оборудования, часть системы CMMS (Computerized Maintenance Management System). Проект включает функционал для управления жизненным циклом оборудования, учёта, логистики и инспекций.</p>

<h2>Технологии</h2>
<ul>
    <li><strong>Backend:</strong> Java, Spring Boot, Spring Data, Spring Security (JWT), Spring Web, Lombok, MapStruct, Swagger, Liquibase, Caffeine Cache, Apache POI.</li>
    <li><strong>Тестирование:</strong> JUnit, H2.</li>
    <li><strong>Frontend:</strong> Node.js (фронтенд часть разработана с помощью ChatGPT).</li>
</ul>

<h2>Установка</h2>

<h3>Клонировать репозиторий:</h3>
<pre><code>git clone https://github.com/RylovK/TrackIT.git</code></pre>
<pre><code>cd trackit</code></pre>

<h3>Запустить бэкенд:</h3>
<pre><code>./gradlew bootrun</code></pre>

<h3>Перейти в папку с фронтендом:</h3>
<pre><code>cd trackit-frontend</code></pre>
<pre><code>npm start</code></pre>

<h2>Функционал</h2>
<ul>
    <li>Просмотр оборудования</li>
    <li>Поиск по партийному или серийному номеру</li>
    <li>Фильтрация по статусам:
        <ul>
            <li><strong>Allocation Status:</strong> ON LOCATION, ON BASE</li>
            <li><strong>Certification Status:</strong> VALID, EXPIRED</li>
            <li><strong>Health Status:</strong> RITE, FXD, RONG, JUNKED</li>
        </ul>
    </li>
    <li>Просмотр оборудования, закреплённого к определённым работам</li>
    <li>Редактирование данных об оборудовании</li>
    <li>Загрузка и выгрузка сертификатов и фотографий</li>
    <li>Экспорт всех данных в формат Excel</li>
    <li>Каждое оборудование имеет log-файл, в котором фиксируются все изменения статуса и атрибутов</li>
</ul>

<h2>Статус</h2>
<p>Проект завершён и готов к использованию.</p>
