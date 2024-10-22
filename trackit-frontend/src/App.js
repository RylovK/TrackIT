// src/App.js
import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import MainPage from './pages/MainPage';
import PartNumberPage from './pages/PartNumberPage';
import PartNumberEditPage from './pages/PartNumberEditPage';
import JobPage from './pages/JobPage';
import JobEditPage from './pages/JobEditPage';
import EquipmentPage from './pages/EquipmentPage';
import EquipmentEditPage from './pages/EquipmentEditPage';
import CertifiedEquipmentPage from './pages/CertifiedEquipmentPage';
import CertifiedEquipmentEditPage from './pages/CertifiedEquipmentEditPage';
import Navbar from "./components/Navbar";

const App = () => {
  return (
      <Router>
        <div style={{display: 'flex'}}>
          <Navbar />
          <Routes>
            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />
            <Route path="/main" element={<MainPage />} />
            <Route path="/partnumber" element={<PartNumberPage />} />
            <Route path="/partnumber/edit" element={<PartNumberEditPage />} />
            <Route path="/job" element={<JobPage />} />
            <Route path="/job/edit" element={<JobEditPage />} />
            <Route path="/equipment" element={<EquipmentPage />} />
            <Route path="/equipment/edit" element={<EquipmentEditPage />} />
            <Route path="/certified-equipment" element={<CertifiedEquipmentPage />} />
            <Route path="/certified-equipment/edit" element={<CertifiedEquipmentEditPage />} />
            <Route path="/" element={<MainPage />} />
          </Routes>
        </div>
      </Router>
  );
};

export default App;
