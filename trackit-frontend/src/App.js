import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Layout from './components/Layout'; // Макет с Navbar
import MainPage from './pages/MainPage';
import PartNumberPage from './pages/PartNumberPage';
import PartNumberEditPage from './pages/PartNumberEditPage';
import JobPage from './pages/Job/JobPage';
import JobEditPage from './pages/Job/JobEditPage';
import JobCreatePage from './pages/Job/JobCreatePage';
import EquipmentPage from './pages/Equipment/EquipmentPage';
import EquipmentEditPage from './pages/Equipment/EquipmentEditPage';
import CertifiedEquipmentPage from './pages/CertifiedEquipmentPage';
import CertifiedEquipmentEditPage from './pages/CertifiedEquipmentEditPage';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';

const App = () => {
  return (
      <Router>
        <Routes>
          {/* Роуты без Navbar */}
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />

          {/* Роуты с Navbar через Layout */}
          <Route element={<Layout />}>
            <Route path="/main" element={<MainPage />} />
            <Route path="/partnumber" element={<PartNumberPage />} />
            <Route path="/partnumber/edit" element={<PartNumberEditPage />} />
            <Route path="/job" element={<JobPage />} />
            <Route path="/job/create" element={<JobCreatePage />} />
            <Route path="/job/:id" element={<JobEditPage />} />
            <Route path="/equipment" element={<EquipmentPage />} />
            <Route path="/equipment/:id" element={<EquipmentEditPage />} />
            <Route path="/certified" element={<CertifiedEquipmentPage />} />
            <Route path="/certified/:id" element={<CertifiedEquipmentEditPage />} />
            <Route path="/" element={<MainPage />} />
          </Route>
        </Routes>
      </Router>
  );
};

export default App;
