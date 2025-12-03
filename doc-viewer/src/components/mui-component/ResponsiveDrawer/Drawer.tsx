import type React from "react";
import nav from "../../../../nav.json";
import {
  Divider,
  List,
  ListItem,
  ListItemButton,
  ListItemText,
  Toolbar,
} from "@mui/material";
import { Link } from "react-router-dom";

const DrawerComponent: React.FC = () => {
  const items = Object.entries(nav);

  return (
    <div>
      <Toolbar />
      <Divider />

      <List>
        {items.map(([label]) => (
          <ListItem key={label} disablePadding>
            <ListItemButton
              component={Link}
              to={`/${label}`}     // ← correct react-router navigation
            >
              <ListItemText primary={label} />
            </ListItemButton>
          </ListItem>
        ))}
      </List>

      <Divider />
    </div>
  );
};

export default DrawerComponent;
