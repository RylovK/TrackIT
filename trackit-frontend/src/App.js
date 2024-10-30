import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Layout from './components/Layout';
import ProtectedRoute from './components/ProtectedRoute';
import MainPage from './pages/MainPage';
import PartNumberCreatingPage from './pages/PartNumber/PartNumberCreatingPage';
import PartNumberEditPage from './pages/PartNumber/PartNumberEditPage';
import JobPage from './pages/Job/JobPage';
import JobEditPage from './pages/Job/JobEditPage';
import JobCreatePage from './pages/Job/JobCreatePage';
import AllEquipmentPage from './pages/Equipment/AllEquipmentPage';
import EquipmentEditPage from './pages/Equipment/EquipmentEditPage';
import CertifiedEquipmentPage from './pages/Equipment/CertifiedEquipmentPage';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import PartNumberList from "./pages/PartNumber/PartNumberList";
import EquipmentCreatingPage from "./pages/Equipment/EquipmentCreatingPage";

const App = () => {
  return (
      <Router>
        <Routes>
          {/* Открытые маршруты без Navbar */}
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />

          {/* Защищенные маршруты с Navbar через Layout */}
          <Route element={<ProtectedRoute />}>
            <Route element={<Layout />}>
              <Route path="/main" element={<MainPage />} />
              <Route path="/partnumber/create" element={<PartNumberCreatingPage />} />
              <Route path="/partnumber/all" element={<PartNumberList />} />
              <Route path="/partnumber/:partNumber" element={<PartNumberEditPage />} />

              <Route path="/job" element={<JobPage />} />
              <Route path="/job/create" element={<JobCreatePage />} />
              <Route path="/job/:id" element={<JobEditPage />} />

              <Route path="/equipment" element={<AllEquipmentPage />} />
              <Route path="/equipment/:id" element={<EquipmentEditPage />} />
              <Route path="/equipment/create" element={<EquipmentCreatingPage />} />

              <Route path="/certified" element={<CertifiedEquipmentPage />} />
              <Route path="/" element={<MainPage />} />
            </Route>
          </Route>
        </Routes>
      </Router>
  );
};

export default App;
