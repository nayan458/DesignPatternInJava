import * as React from 'react';
import Box from '@mui/material/Box';
import CssBaseline from '@mui/material/CssBaseline';
import Drawer from '@mui/material/Drawer';
import Toolbar from '@mui/material/Toolbar';
import AppBarNavigation from './AppBar';
import Page from './Page';
import DrawerComponent from './Drawer';
import { useLocation } from "react-router-dom";
import nav from "../../../../nav.json";

const drawerWidth = 240;

export default function ResponsiveDrawer() {
  const [mobileOpen, setMobileOpen] = React.useState(false);
  const [isClosing, setIsClosing] = React.useState(false);
  const [filePath, setFilePath] = React.useState("");

  const location = useLocation();

  const handleDrawerClose = () => {
    setIsClosing(true);
    setMobileOpen(false);
  };

  const handleDrawerTransitionEnd = () => {
    setIsClosing(false);
  };

  const handleDrawerToggle = () => {
    if (!isClosing) {
      setMobileOpen(!mobileOpen);
    }
  };

  React.useEffect(() => {
    const currentPath = location.pathname.startsWith("/")
      ? location.pathname.slice(1)
      : location.pathname;
    
    // Find the matching entry by label (nav key)
    const entries = Object.entries(nav) as [string, string][];
    const matchedEntry = entries.find(([label]) => label === currentPath);
    
    if (matchedEntry) {
      const navPath = matchedEntry[1];
      // If nav.json uses the @readmes alias, convert to a fetchable URL
      if (navPath.startsWith("@readmes")) {
        const rel = navPath.replace(/^@readmes\/?/, "");
        // produced URL (dev server can serve /src/...), adjust if you put files in public
        setFilePath(`/src/collected-readmes/${rel}`);
      } else {
        setFilePath(navPath);
      }
      console.log('Matched file path:', matchedEntry[1]);
    } else {
      console.log('No match found for:', currentPath);
      setFilePath("");
    }
  }, [location.pathname]);


  return (
    <Box sx={{ display: 'flex' }}>
      <CssBaseline />
      <AppBarNavigation drawerWidth={drawerWidth} handleDrawerToggle={handleDrawerToggle} />
      <Box
        component="nav"
        sx={{ width: { sm: drawerWidth }, flexShrink: { sm: 0 } }}
        aria-label="mailbox folders"
      >
        <Drawer
          variant="temporary"
          open={mobileOpen}
          onTransitionEnd={handleDrawerTransitionEnd}
          onClose={handleDrawerClose}
          sx={{
            display: { xs: 'block', sm: 'none' },
            '& .MuiDrawer-paper': { boxSizing: 'border-box', width: drawerWidth },
          }}
          slotProps={{
            root: {
              keepMounted: true,
            },
          }}
        >
          <DrawerComponent />
        </Drawer>
        <Drawer
          variant="permanent"
          sx={{
            display: { xs: 'none', sm: 'block' },
            '& .MuiDrawer-paper': { boxSizing: 'border-box', width: drawerWidth },
          }}
          open
        >
          <DrawerComponent />
        </Drawer>
      </Box>
      <Box
        component="main"
        sx={{ flexGrow: 1, p: 3, width: { sm: `calc(100% - ${drawerWidth}px)` } }}
      >
        <Toolbar />
        <Page filePath={filePath}/>
      </Box>
    </Box>
  );
}

export const fetchReadmeFromStatic = async (path: string): Promise<string> => {
  try {
    // Map @readmes alias (from nav.json) to a runtime URL that can be fetched.
    // If you move collected-readmes to /public, change this mapping to `/collected-readmes/${rel}`
    let fetchPath = path;
    if (path.startsWith("@readmes")) {
      const rel = path.replace(/^@readmes\/?/, "");
      fetchPath = `/src/collected-readmes/${rel}`;
    }
    // Ensure leading slash
    if (!fetchPath.startsWith("/")) fetchPath = `/${fetchPath}`;

    const response = await fetch(fetchPath);
    if (!response.ok) {
      throw new Error(`Failed to fetch README: ${response.statusText}`);
    }
    return await response.text();
  } catch (error) {
    console.error('Error fetching README:', error);
    throw error;
  }
};