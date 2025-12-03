import React from "react";
import ResponsiveDrawer from "@/components/mui-component/ResponsiveDrawer/ResponsiveDrawer";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";

const App: React.FC = () => {
  return (
    <>
      <BrowserRouter>
        <ResponsiveDrawer />
        <Routes>
          <Route path="/" element={<Navigate to="/collected-readmes" replace />} />
        </Routes>
      </BrowserRouter>
    </>
  );
};

export default App;