<!-- Do not translate this -->
<details>
<summary>
<strong> Read this guide in other languages </strong>
</summary>
    <ul>
        <li><a href="./README.md"> English </a></li>
        <li><a href="./README-RU.md"> Русский </a></li>
    </ul>
</details>
<!-- Do not translate this -->

<h1>TrackIT</h1>

<p><strong>TrackIT</strong> is a web application built in Java for equipment tracking, part of a CMMS (Computerized Maintenance Management System). The project includes functionality for product lifecycle management, accounting, logistics, and inspections.</p>

<h2>Technologies</h2>
<ul>
    <li><strong>Backend:</strong> Java, Spring Boot, Spring Data, Spring Security (JWT), Spring Web, Lombok, MapStruct, Swagger, Liquibase, Caffeine Cache, Apache POI.</li>
    <li><strong>Testing:</strong> JUnit, H2.</li>
    <li><strong>Frontend:</strong> Node.js (frontend part developed with the help of ChatGPT).</li>
</ul>

<h2>Installation</h2>

<h3>Clone the repository:</h3>
<pre><code>git clone https://github.com/RylovK/TrackIT.git</code></pre>
<pre><code>cd trackit</code></pre>

<h3>Run the backend:</h3>
To configure the project to connect to a database, update the `application.properties` file located in the `src/main/resources` folder of the project.

<pre><code>./gradlew bootrun</code></pre>

<h3>Run the frontend:</h3>
Install Node.js and npm

<pre><code>cd trackit-frontend</code></pre>
<pre><code>npm install</code></pre>
<pre><code>npm start</code></pre>

<h2>Features</h2>
<ul>
    <li>Viewing equipment</li>
    <li>Search by part number or serial number</li>
    <li>Filter by statuses:
        <ul>
            <li><strong>Allocation Status:</strong> ON LOCATION, ON BASE</li>
            <li><strong>Certification Status:</strong> VALID, EXPIRED</li>
            <li><strong>Health Status:</strong> RITE, FXD, RONG, JUNKED</li>
        </ul>
    </li>
    <li>View equipment assigned to specific jobs</li>
    <li>Edit equipment data</li>
    <li>Upload and download certification files and photos</li>
    <li>Export all data to Excel format</li>
    <li>Each piece of equipment has a log file that records all changes to its status and attributes</li>
</ul>

<h2>Status</h2>
<p>The project is complete and ready for use.</p>
